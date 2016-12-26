package cpe.top.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cpe.top.quizz.asyncTask.GetAllThemesTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterThemes;

/**
 * Created by lparet on 29/11/16.
 */

public class ChooseTheme extends AppCompatActivity implements SearchView.OnQueryTextListener, AsyncUserResponse {
    private static final String THEME = "THEME";
    private static final String STATE = "STATE";
    private static final String USER = "USER";

    private Bundle bundle;
    private String state;
    private User connectedUser = null;

    ListViewAdapterThemes adapter;
    SearchView editsearch;
    private ListView list;

    // List of themes - to add multiple themes
    private ArrayList<Theme> myThemes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_theme);

        Intent intent = getIntent();
        // take connectedUser and state
        // If you was in CreateQuestion or Create Quizz before : you have themes, question, explanation, quizz name, number of questions choosed
        bundle = intent.getExtras();
        if(bundle != null) {
            myThemes = (ArrayList<Theme>) bundle.getSerializable(THEME);
            state = bundle.getString(STATE);
            connectedUser = (User) bundle.getSerializable(USER);
        }

        if(connectedUser==null) {
            Intent i = new Intent(ChooseTheme.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // change the title
        TextView title = (TextView) findViewById(R.id.title);
        if("Quizz".equals(state)) {
            title.setText("Choisis le thème de ton quizz");
        }

        // AsyncTask to take all Themes
        GetAllThemesTask getAllThemesTask = new GetAllThemesTask(ChooseTheme.this);
        getAllThemesTask.execute();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    @Override
    public void processFinish(Object obj) {

        Collection<Theme> themes = null ;
        if (obj != null && ((ReturnObject) obj).getObject() != null){
            themes = (Collection<Theme>) ((ReturnObject) obj).getObject();
        }
        ArrayList<Theme> resultsList = new ArrayList<>();

        // This algo is to delete theme already choose in the list of theme, like that, you can't choose a theme you have already choose
        if(themes != null && themes.size() != 0) {
            for (Theme t : themes) {
                if (myThemes != null) {
                    for(Theme theme : myThemes) {
                        if (!t.getName().equals(theme.getName())) {
                            resultsList.add(t);
                        }
                    }
                } else {
                    resultsList.add(t);
                }
            }
        }

        Set set = new HashSet() ;
        set.addAll(resultsList) ;
        resultsList = new ArrayList(set);


        // Take connected user to send to ListViewAdapter class
        // Take all things in bundle and add in new intent to transfer to ListViewAdapterThemes.class
        Bundle bundle = getIntent().getExtras();
        Intent intent = new Intent(ChooseTheme.this, ListViewAdapterThemes.class);
        intent.putExtras(bundle);
        intent.putExtra(THEME, myThemes);

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapterThemes(this, resultsList, intent);

        list = (ListView) findViewById(R.id.listViewTheme);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in activity_choose_theme
        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);
    }

    public void onBackPressed(){
        Intent intent = new Intent(ChooseTheme.this, Home.class);
        // Go to Home to prevent beug
        // Add connectedUser and list of Quizz
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}