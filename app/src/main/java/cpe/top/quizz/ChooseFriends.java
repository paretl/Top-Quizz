package cpe.top.quizz;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
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

public class ChooseFriends extends AppCompatActivity implements AsyncUserResponse {
    private static final String USER = "USER";

    private User connectedUser = null;

    private TextView tx;

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        connectedUser = (User) getIntent().getSerializableExtra(USER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() < 3) {
                    Toast.makeText(getBaseContext(), "Tape au moins 3 caractères", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String partialPseudo) {
                tx = (TextView) findViewById(R.id.result);
                if (partialPseudo.length() > 2) {
                    final GetFriendsTask getFriends = new GetFriendsTask(ChooseFriends.this);
                    getFriends.execute(partialPseudo);
                    return true;
                } else {
                    tx.setText("Tape au moins 3 caractères");
                    ArrayList<String> resultsList = new ArrayList<>();
                    // Pass results to ListViewAdapter Class
                    ListViewAdapterUsers adapter = new ListViewAdapterUsers(ChooseFriends.this, resultsList, connectedUser);

                    ListView list = (ListView) findViewById(R.id.listViewFriend);
                    // Binds the Adapter to the ListView
                    list.setAdapter(adapter);
                }
                return false;
            }
        });

        return true;
    }


    @Override
    public void processFinish(Object obj) {
        if (obj != null && ((ReturnObject) obj).getObject() != null) {
            tx.setText("Résultats de la recherche pour : " + searchView.getQuery());
            String[] pseudo = ((ReturnObject) obj).getObject().toString().replace("\"", "").replace("]", "").replace("[", "").split(",");
            ArrayList<String> resultsList = new ArrayList<>();

            for (int i = 0; i < pseudo.length; i++) {
                resultsList.add(pseudo[i]);
            }

            // Pass results to ListViewAdapter Class
            ListViewAdapterUsers adapter = new ListViewAdapterUsers(this, resultsList, connectedUser);

            ListView list = (ListView) findViewById(R.id.listViewFriend);
            // Binds the Adapter to the ListView
            list.setAdapter(adapter);
        } else {
            tx.setText("Pas de résultat pour cette recherche : " + searchView.getQuery());
            // To delete all pseudo on the listView
            ArrayList<String> resultsList = new ArrayList<>();
            // Pass results to ListViewAdapter Class
            ListViewAdapterUsers adapter = new ListViewAdapterUsers(this, resultsList, connectedUser);

            ListView list = (ListView) findViewById(R.id.listViewFriend);
            // Binds the Adapter to the ListView
            list.setAdapter(adapter);
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(ChooseFriends.this, Home.class);
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }
}
