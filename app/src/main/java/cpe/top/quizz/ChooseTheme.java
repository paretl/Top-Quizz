package cpe.top.quizz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.utils.ListViewAdapterThemes;

/**
 * Created by lparet on 29/11/16.
 */

public class ChooseTheme extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapterThemes adapter;
    SearchView editsearch;
    String[] themeList;
    ArrayList<Theme> arraylist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_theme);

        themeList = new String[]{"Java", "Sexe", "Téléphonie",
                "Alcool", "Musique", "Technologie", "Art", "Géographie",
                "Histoire","Littérature","Droit", "Economie", "Divers", "Android", "Informatique", "Football", "Sport", "Basketball", "Hockey Sur Gazon", "Pétanque", "Oenologie", "Beer Pong"};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listViewTheme);

        for (int i = 0; i < themeList.length; i++) {
            Theme theme = new Theme(themeList[i]);
            // Binds all strings into an array
            arraylist.add(theme);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapterThemes(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in activity_choose_theme
        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);
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
}
