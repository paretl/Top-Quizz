package cpe.top.quizz;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cpe.top.quizz.asyncTask.GetFriendsTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterUsers;

/**
 * Created by lparet on 08/01/17.
 */

public class ChooseFriends extends AppCompatActivity implements AsyncUserResponse, NavigationView.OnNavigationItemSelectedListener {
    private static final String USER = "USER";

    private User connectedUser = null;

    private TextView textViewAction;

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        connectedUser = (User) getIntent().getSerializableExtra(USER);

        if (connectedUser == null) {
            Intent i = new Intent(ChooseFriends.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView.getQuery().toString().contains("%")){
                    searchView.setQuery(searchView.getQuery().toString().replace("%", ""), true);
                    return true;
                }

                if (query.length() < 3) {
                    Toast.makeText(getBaseContext(), "Tape au moins 3 caractères", Toast.LENGTH_LONG).show();
                    return true;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String partialPseudo) {

                if (searchView.getQuery().toString().contains("%")){
                    searchView.setQuery(searchView.getQuery().toString().replace("%", ""), true);
                    return true;
                }

                textViewAction = (TextView) findViewById(R.id.result);
                if (partialPseudo.length() > 2) {
                    final GetFriendsTask getFriends = new GetFriendsTask(ChooseFriends.this);
                    getFriends.execute(partialPseudo, connectedUser.getPseudo());
                    return true;
                } else {
                    textViewAction.setText("Tape au moins 3 caractères");
                    ArrayList<String> resultsList = new ArrayList<>();
                    // Pass results to ListViewAdapter Class
                    ListViewAdapterUsers adapter = new ListViewAdapterUsers(ChooseFriends.this, resultsList, connectedUser);

                    ListView list = (ListView) findViewById(R.id.listViewFriend);
                    // Binds the Adapter to the ListView
                    list.setAdapter(adapter);
                }
                return true;
            }
        });

        return true;
    }


    @Override
    public void processFinish(Object obj) {
        if (obj != null && ((ReturnObject) obj).getObject() != null) {
            textViewAction.setText("Résultats de la recherche pour : " + searchView.getQuery());


            String[] pseudo = ((ReturnObject) obj).getObject().toString().replace("\"", "").replace("]", "").replace("[", "").split(",");
            ArrayList<String> resultsList = new ArrayList<>();

            for (int i = 0; i < pseudo.length; i++) {
                if (pseudo[i] != "") {
                    resultsList.add(pseudo[i]);
                }
            }

            // Pass results to ListViewAdapter Class
            ListViewAdapterUsers adapter = new ListViewAdapterUsers(this, resultsList, connectedUser);

            ListView list = (ListView) findViewById(R.id.listViewFriend);
            // Binds the Adapter to the ListView
            list.setAdapter(adapter);
        } else {
            textViewAction.setText("Pas de résultat pour cette recherche : " + searchView.getQuery());
            // To delete all pseudo on the listView
            ArrayList<String> resultsList = new ArrayList<>();
            // Pass results to ListViewAdapter Class
            ListViewAdapterUsers adapter = new ListViewAdapterUsers(this, resultsList, connectedUser);

            ListView list = (ListView) findViewById(R.id.listViewFriend);
            // Binds the Adapter to the ListView
            list.setAdapter(adapter);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(ChooseFriends.this, Home.class);
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
