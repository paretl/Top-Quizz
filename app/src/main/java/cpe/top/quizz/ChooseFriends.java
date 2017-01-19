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
import java.util.List;

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.GetFriendsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterUsers;

/**
 * Created by lparet on 08/01/17.
 */

public class ChooseFriends extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {
    private static final String USER = "USER";
    private static final String GET_FRIENDS_TASK = "GET_FRIENDS_TASK";
    private static final String FRIENDS_TASK = "FRIENDS_TASK";
    private static final String LIST_FRIENDS = "LIST_FRIENDS";

    private User connectedUser = null;

    private TextView textViewAction;

    private SearchView searchView;

    private List<User> listF = null;

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

        try {
            if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(GET_FRIENDS_TASK)) {
                // Case of QuizzTask
                processFinishGetFriendsTask(obj);
            } else if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(FRIENDS_TASK)) {
                // Case of FriendsTask
                processFinishFriendsTask(obj);
            }
        } catch (ClassCastException e) {
            processFinishExceptionCast(obj);
        }

    }

    private void processFinishFriendsTask(Object obj) {
        switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
            case ERROR_000:
                Intent myIntent = new Intent(ChooseFriends.this, FriendsDisplay.class);
                this.listF = (List<User>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                myIntent.putExtra(USER, (User) connectedUser);
                myIntent.putExtra(LIST_FRIENDS, (ArrayList<User>) listF);
                startActivity(myIntent);
                break;
            case ERROR_200:
                Toast.makeText(ChooseFriends.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            // Temporarily - When no data found - ERROR_50 is ok?
            case ERROR_050:
                // No friends for the user but we want to access to FriendsDisplay
                Intent intentFriends = new Intent(ChooseFriends.this, FriendsDisplay.class);
                this.listF = (List<User>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                intentFriends.putExtra(USER, (User) connectedUser);
                intentFriends.putExtra(LIST_FRIENDS, (ArrayList<User>) listF);
                startActivity(intentFriends);
                break;
            case ERROR_100:
                // No friends for the user but we want to access to FriendsDisplay
                Intent intentFriends_100 = new Intent(ChooseFriends.this, FriendsDisplay.class);
                this.listF = (List<User>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                intentFriends_100.putExtra(USER, (User) connectedUser);
                intentFriends_100.putExtra(LIST_FRIENDS, (ArrayList<User>) listF);
                startActivity(intentFriends_100);
                break;
            default:
                Toast.makeText(ChooseFriends.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void processFinishExceptionCast(Object obj) {
        switch (((ReturnObject) obj).getCode()) {
            case ERROR_200:
                Toast.makeText(ChooseFriends.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_100:
            default:
                Toast.makeText(ChooseFriends.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void processFinishGetFriendsTask(Object obj) {

        if (obj != null && ((ReturnObject) ((List<Object>) obj).get(1)).getCode() != null) {
            textViewAction.setText("Résultats de la recherche pour : " + searchView.getQuery());


            String[] pseudo = ((ReturnObject) ((List<Object>) obj).get(1)).getObject().toString().replace("\"", "").replace("]", "").replace("[", "").split(",");
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(ChooseFriends.this, Home.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findFriend:
                intent = new Intent(ChooseFriends.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(ChooseFriends.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(ChooseFriends.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.createEvaluation:
                intent = new Intent(ChooseFriends.this, ChooseQuizzEval.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(ChooseFriends.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            /*case R.id.settings:
                //TODO
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;*/
            default:
                //Unreachable statement
                break;
        }
        return true;
    }
}
