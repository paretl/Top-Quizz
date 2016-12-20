package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

import cpe.top.quizz.asyncTask.GetAllThemesTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterThemes;

/**
 * Created by lparet on 29/11/16.
 */

public class ChooseTheme extends AppCompatActivity implements SearchView.OnQueryTextListener, AsyncUserResponse {

    private static final String USER = "USER";
    private static final String THEME = "THEME";

    private User connectedUser = null;

    ListViewAdapterThemes adapter;
    SearchView editsearch;
    private ListView list;

    // List of themes - to add multiple themes
    ArrayList<Theme> myThemes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_theme);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) intent.getSerializableExtra(USER);
            myThemes = (ArrayList<Theme>) intent.getSerializableExtra(THEME);
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
        Collection<Theme> themes = (Collection<Theme>) ((ReturnObject) obj).getObject();
        ArrayList<Theme> resultsList = new ArrayList<>();

        // This algo is to delete theme already choose in the list of theme, like that, you can't choose a theme you have already choose
        if (themes != null && themes.size() != 0) {
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

        // Take connected user to send to ListViewAdapter class
        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) intent.getSerializableExtra(USER);
            if(connectedUser == null) {
                Intent i = new Intent(ChooseTheme.this, MainActivity.class);
                startActivity(i);
            }
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapterThemes(this, resultsList, connectedUser, myThemes);

        list = (ListView) findViewById(R.id.listViewTheme);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in activity_choose_theme
        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChooseTheme.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
