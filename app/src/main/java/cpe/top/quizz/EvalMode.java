package cpe.top.quizz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpe.top.quizz.asyncTask.EvaluationTask;
import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Evaluation;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterThemes;

public class EvalMode extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {
    private static final String USER = "USER";
    private static final String EVALUATION_TASKS = "EVALUATION_TASKS";

    private User connectedUser = null;

    EvaluationAdapter adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval_mode);

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

        if(connectedUser==null) {
            Intent i = new Intent(EvalMode.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        EvaluationTask task = new EvaluationTask(EvalMode.this);
        task.execute(connectedUser.getPseudo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.friends:
                FriendsTask friends = new FriendsTask(EvalMode.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.findFriend:
                intent = new Intent(EvalMode.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(EvalMode.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(EvalMode.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.evalMode:
                intent = new Intent(EvalMode.this, EvalMode.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(EvalMode.this, MainActivity.class);
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

    @Override
    public void processFinish(Object obj) {
        System.out.println("test");
        System.out.println(((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(EVALUATION_TASKS));
        if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(EVALUATION_TASKS)) {
            System.out.println("test1");
            switch (((ReturnObject) ((List<Object>) obj).get(0)).getCode()) {
                case ERROR_000:
                    System.out.println("test2");
                    List<Evaluation> evaluations = (List<Evaluation>) ((List<ReturnObject>) obj).get(1).getObject();
                    adapter = new EvaluationAdapter(this, evaluations, connectedUser);
                    list = (ListView) findViewById(R.id.listEval);
                    // Binds the Adapter to the ListView
                    list.setAdapter(adapter);
                    break;
                case ERROR_200:
                    Toast.makeText(EvalMode.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(EvalMode.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}