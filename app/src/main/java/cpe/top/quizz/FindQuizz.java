package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.GetAllFriendsQuizzsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterQuizz;

/**
 * Created by lparet on 03/01/17.
 */

public class FindQuizz extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String USER = "USER";

    private User connectedUser;
    private List<Quizz> listQ = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectedUser = (User) getIntent().getSerializableExtra(USER);

        if(connectedUser==null) {
            Intent i = new Intent(FindQuizz.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        final GetAllFriendsQuizzsTask getAllQuizzs = new GetAllFriendsQuizzsTask(FindQuizz.this);
        getAllQuizzs.execute(connectedUser.getPseudo());

        display();
    }

    private void display(){
        setContentView(R.layout.activity_find_quizz);
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

        if (listQ != null && !listQ.isEmpty()) {
            // Adapter
            ListViewAdapterQuizz adapter = new ListViewAdapterQuizz(this, listQ, connectedUser);

            // The list (IHM)
            ListView list = (ListView) findViewById(R.id.listQuizz);

            // Initialization of the list
            list.setAdapter(adapter);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        display();
    }

    @Override
    public void processFinish(Object obj) {
        ReturnObject rO = (ReturnObject) obj;
        if(rO.getObject() != null) {
            switch (((ReturnObject) obj).getCode()) {
                case ERROR_000:
                    listQ = new ArrayList<>();
                    listQ.addAll((Collection<Quizz>) ((ReturnObject) obj).getObject());
                    if(listQ.isEmpty()) {
                        Toast.makeText(FindQuizz.this, "Vos amis n'ont pas de quiz", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FindQuizz.this, Home.class);
                        intent.putExtra(USER, connectedUser);
                        startActivity(intent);
                        finish();
                    }
                    onRestart();
                    break;
                case ERROR_200:
                    Toast.makeText(FindQuizz.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(FindQuizz.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(FindQuizz.this, "Vous n'avez pas d'amis...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FindQuizz.this, Home.class);
            intent.putExtra(USER, connectedUser);
            startActivity(intent);
            finish();
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(FindQuizz.this, Home.class);
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(FindQuizz.this, Home.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.friends:
                FriendsTask friends = new FriendsTask(FindQuizz.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.findFriend:
                intent = new Intent(FindQuizz.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(FindQuizz.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(FindQuizz.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(FindQuizz.this, MainActivity.class);
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
