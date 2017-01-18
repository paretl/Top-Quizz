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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.GetAllQuizzsTask;
import cpe.top.quizz.asyncTask.StatisticTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.Utility;

public class Home extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener  {

    private static final String USER = "USER";
    private static final String QUIZZ = "QUIZZ";
    private static final String LIST_QUIZZ = "LIST_QUIZZ";
    private static final String STATISTICS_TASKS = "STATISTICS_TASKS";
    private static final String QUIZZS_TASKS = "QUIZZS_TASKS";
    private static final String STATISTICS = "STATISTICS";
    private static final String STATE = "STATE";
    private static final String FRIENDS_TASK = "FRIENDS_TASK";
    private static final String LIST_FRIENDS = "LIST_FRIENDS";
    private static final String USER_FRIEND = "USER_FRIEND";
    private static final String PROFIL_TASK = "PROFIL_TASK";

    private User connectedUser;
    private String state;
    private List<User> listF = null;
    private List<Quizz> myListQ = null;
    private List<Quizz> listQShared = null;

    private TextView textViewQuizzSharred;
    private TextView textViewMyQuizz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectedUser = (User) getIntent().getSerializableExtra(USER);

        final GetAllQuizzsTask getQuizzs = new GetAllQuizzsTask(Home.this);
        getQuizzs.execute(connectedUser.getPseudo());

        display();
    }

    private void display(){
        setContentView(R.layout.activity_home);
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

        textViewQuizzSharred = (TextView) findViewById(R.id.tVSharedQuizz);
        textViewMyQuizz = (TextView) findViewById(R.id.tVmyQuizz);

        if (myListQ != null && !myListQ.isEmpty()) {
            // Adapter
            QuizzAdapter adapter = new QuizzAdapter(this, myListQ, connectedUser);

            // The list (IHM)
            ListView list = (ListView) findViewById(R.id.listQuizz);

            // Initialization of the list
            list.setAdapter(adapter);

            // To accept scroll
            Utility.setListViewHeightBasedOnChildren(list);
        } else {
            LinearLayout myQuizz = (LinearLayout) findViewById(R.id.QuizzView);
            myQuizz.removeView(findViewById(R.id.LlmyQuiz));
            textViewMyQuizz.setText("Tu n'as pas de quizz");
        }

        if (listQShared != null && !listQShared.isEmpty()) {
            textViewQuizzSharred.setVisibility(View.VISIBLE);
            // Adapter
            QuizzAdapter adapter = new QuizzAdapter(this, listQShared, connectedUser);

            // The list (IHM)
            ListView list = (ListView) findViewById(R.id.listQuizzShared);

            // Initialization of the list
            list.setAdapter(adapter);

            // To accept scroll
            Utility.setListViewHeightBasedOnChildren(list);
        } else {
            textViewQuizzSharred.setVisibility(View.INVISIBLE);
        }

        final ImageView stats = (ImageView) findViewById(R.id.stats);
        final ImageView theme = (ImageView) findViewById(R.id.theme);
        final ImageView questionButton = (ImageView) findViewById(R.id.questionButton);

        stats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticTask u = new StatisticTask(Home.this);
                myListQ.addAll(listQShared);
                if (myListQ != null && myListQ.size() != 0 && myListQ.get(0) != null) {
                    u.execute(connectedUser.getPseudo(), String.valueOf(myListQ.get(0).getId()));
                } else {
                    Toast.makeText(Home.this, "Pas de statistiques disponibles (0 quiz) !", Toast.LENGTH_SHORT).show();
                }
            }

        });

        theme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ThemesDisplay.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
            }

        });

        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ChooseTheme.class);
                state = "Question";
                intent.putExtra(STATE, state);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
            }
        });

        final Button createQuizz = (Button) findViewById(R.id.createQuizz);
        createQuizz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ChooseTheme.class);
                state = "Quizz";
                intent.putExtra(STATE, state);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        display();
    }

    @Override
    public void processFinish(Object obj) {
        try {
            if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(QUIZZS_TASKS)) {
                // Case of QuizzTask
                processFinishQuizzTask(obj);
            } else if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(STATISTICS_TASKS)) {
                // Case of StatisticTask
                processFinishStatisticsTask(obj);
            } else if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(FRIENDS_TASK)) {
                // Case of FriendsTask
                processFinishFriendsTask(obj);
            } else if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(PROFIL_TASK)) {
                // Case of ProfilTask
                processFinishProfilTask(obj);
            }
        } catch (ClassCastException e) {
            processFinishExceptionCast(obj);
        }
    }

    /** Implementations of functions processFinish...() */

    private void processFinishQuizzTask(Object obj) {
        switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
            case ERROR_000:
                myListQ = new ArrayList<>();
                listQShared = new ArrayList<>();
                for(Quizz q : (Collection<Quizz>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject()) {
                    if(((((ArrayList<Question>) q.getQuestions()).get(0)).getPseudo()).equals(connectedUser.getPseudo())) {
                        myListQ.add(q);
                    } else {
                        listQShared.add(q);
                    }
                }
                onRestart();
                break;
            case ERROR_200:
                Toast.makeText(Home.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            // Temporarily - When no data found - ERROR_50 is ok?
            case ERROR_050:
                break;
            case ERROR_100:
                break;
            default:
                Toast.makeText(Home.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void processFinishStatisticsTask(Object obj) {
        switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
            case ERROR_000:
                Intent myIntent = new Intent(Home.this, StatsGraphics.class);
                List<Statistic> stats = (List<Statistic>) ((List<ReturnObject>) obj).get(1).getObject();
                myIntent.putExtra(STATISTICS, (ArrayList<Statistic>) stats);
                myIntent.putExtra(USER, (User) connectedUser);
                myIntent.putExtra(LIST_QUIZZ, (ArrayList<Quizz>) myListQ);
                startActivity(myIntent);
                break;
            case ERROR_200:
                Toast.makeText(Home.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_100:
                // No statistic for the 1st quizz but we want to access to Statistic
                Intent myIntent_100 = new Intent(Home.this, StatsGraphics.class);
                myIntent_100.putExtra(USER, (User) connectedUser);
                myIntent_100.putExtra(LIST_QUIZZ, (ArrayList<Quizz>) myListQ);
                startActivity(myIntent_100);
                break;
            default:
                Toast.makeText(Home.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void processFinishFriendsTask(Object obj) {
        switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
            case ERROR_000:
                Intent myIntent = new Intent(Home.this, FriendsDisplay.class);
                this.listF = (List<User>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                myIntent.putExtra(USER, (User) connectedUser);
                myIntent.putExtra(LIST_FRIENDS, (ArrayList<User>) listF);
                startActivity(myIntent);
                break;
            case ERROR_200:
                Toast.makeText(Home.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            // Temporarily - When no data found - ERROR_50 is ok?
            case ERROR_050:
                // No friends for the user but we want to access to FriendsDisplay
                Intent intentFriends = new Intent(Home.this, FriendsDisplay.class);
                this.listF = (List<User>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                intentFriends.putExtra(USER, (User) connectedUser);
                intentFriends.putExtra(LIST_FRIENDS, (ArrayList<User>) listF);
                startActivity(intentFriends);
                break;
            case ERROR_100:
                // No friends for the user but we want to access to FriendsDisplay
                Intent intentFriends_100 = new Intent(Home.this, FriendsDisplay.class);
                this.listF = (List<User>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                intentFriends_100.putExtra(USER, (User) connectedUser);
                intentFriends_100.putExtra(LIST_FRIENDS, (ArrayList<User>) listF);
                startActivity(intentFriends_100);
                break;
            default:
                Toast.makeText(Home.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void processFinishProfilTask(Object obj) {
        switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
            case ERROR_000:
                Intent myIntent = new Intent(Home.this, Profil.class);
                myIntent.putExtra(USER, (User) connectedUser);
                User userFriend = (User) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                if (((ReturnObject) ((List<Object>) obj).get(2)).getObject() != null) {
                    userFriend.setQuizz((List<Quizz>) (((ReturnObject) ((List<Object>) obj).get(2)).getObject()));
                }
                myIntent.putExtra(USER_FRIEND, userFriend);
                startActivity(myIntent);
                break;
            case ERROR_200:
                Toast.makeText(Home.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_100:
                Toast.makeText(Home.this, "Ce profil n'existe pas...", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(Home.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void processFinishExceptionCast(Object obj) {
        switch (((ReturnObject) obj).getCode()) {
            case ERROR_000:
                Intent myIntent = new Intent(Home.this, StartQuizz.class);
                myIntent.putExtra(QUIZZ, (Quizz) ((ReturnObject) obj).getObject());
                myIntent.putExtra(USER, connectedUser);
                startActivity(myIntent);
                finish();
                break;
            case ERROR_200:
                Toast.makeText(Home.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_100:
            default:
                Toast.makeText(Home.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(Home.this, Home.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.friends:
                FriendsTask friends = new FriendsTask(Home.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.findFriend:
                intent = new Intent(Home.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(Home.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(Home.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(Home.this, MainActivity.class);
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
