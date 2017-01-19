package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cpe.top.quizz.asyncTask.EvaluationStatisticTask;
import cpe.top.quizz.asyncTask.EvaluationTask;
import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Evaluation;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterThemes;

public class EvalResult extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String USER = "USER";
    private static final String EVAL_ID = "EVAL_ID";

    private User connectedUser = null;
    private int evalId;

    EvaluationStatisticAdapter adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval_result);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getWindow().setBackgroundDrawableResource(R.drawable.background);

        if(getIntent()!=null) {
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            evalId = getIntent().getIntExtra(EVAL_ID, 0);
        }

        if(connectedUser==null) {
            Intent i = new Intent(EvalResult.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        EvaluationStatisticTask task = new EvaluationStatisticTask(EvalResult.this);
        task.execute(connectedUser.getPseudo(), Integer.toString(evalId));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.friends:
                FriendsTask friends = new FriendsTask(EvalResult.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.findFriend:
                intent = new Intent(EvalResult.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(EvalResult.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(EvalResult.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.evalMode:
                intent = new Intent(EvalResult.this, EvalMode.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(EvalResult.this, MainActivity.class);
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
        List<Statistic> stats = null;
        if (obj != null && ((ReturnObject) obj).getObject() != null) {
            stats = (List<Statistic>) ((ReturnObject) obj).getObject();
        }

        // Pass results to ListViewAdapter Class
        adapter = new EvaluationStatisticAdapter(this, stats, connectedUser);
        list = (ListView) findViewById(R.id.listEval);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
    }
}
