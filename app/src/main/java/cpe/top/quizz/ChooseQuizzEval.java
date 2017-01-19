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
import java.util.List;

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.GetOwnQuizzsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterQuizzEval;

/**
 * Created by lparet on 17/01/17.
 */

public class ChooseQuizzEval extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String USER = "USER";

    private Bundle bundle;
    private User connectedUser = null;
    private List<Quizz> myQuizz = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quizz_eval);

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

        Intent intent = getIntent();
        // take connectedUser and state
        // If you was in CreateQuestion or Create Quizz before : you have themes, question, explanation, quizz name, number of questions choosed
        bundle = intent.getExtras();
        if(bundle != null) {
            if(bundle.getSerializable(USER) != null) {
                connectedUser = (User) bundle.getSerializable(USER);
            }
        }

        if(connectedUser==null) {
            Intent i = new Intent(ChooseQuizzEval.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        final GetOwnQuizzsTask getMyQuizzs = new GetOwnQuizzsTask(ChooseQuizzEval.this);
        getMyQuizzs.execute(connectedUser.getPseudo());
    }

    @Override
    public void processFinish(Object obj) {
        Object myObj = ((ReturnObject) obj).getObject();
        if (myObj != null && obj != null) {
            myQuizz = (ArrayList<Quizz>) ((ReturnObject) obj).getObject();
            if (!myQuizz.isEmpty()) {
                // Pass results to ListViewAdapter Class
                ListViewAdapterQuizzEval adapter = new ListViewAdapterQuizzEval(this, myQuizz, connectedUser);

                ListView list = (ListView) findViewById(R.id.listViewChooseQuizz);
                // Binds the Adapter to the ListView
                list.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Pas de quizz créé", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ChooseQuizzEval.this, Home.class);
                i.putExtra(USER, connectedUser);
                startActivity(i);
                finish();
            }
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(ChooseQuizzEval.this, Home.class);
        // Go to Home to prevent beug
        // Add connectedUser and list of Quizz
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.friends:
                FriendsTask friends = new FriendsTask(ChooseQuizzEval.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.chat:
                intent = new Intent(ChooseQuizzEval.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(ChooseQuizzEval.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.createEvaluation:
                intent = new Intent(ChooseQuizzEval.this, ChooseQuizzEval.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(ChooseQuizzEval.this, MainActivity.class);
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
