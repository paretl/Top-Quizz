package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpe.top.quizz.asyncTask.GetAllQuizzsTask;
import cpe.top.quizz.asyncTask.StatisticTask;
import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.Utility;

public class Home extends AppCompatActivity implements AsyncStatisticResponse {

    private static final String USER = "USER";
    private static final String QUIZZ = "QUIZZ";
    private static final String LIST_QUIZZ = "LIST_QUIZZ";
    private static final String STATISTICS_TASKS = "STATISTICS_TASKS";
    private static final String QUIZZS_TASKS = "QUIZZS_TASKS";
    private static final String STATISTICS = "STATISTICS";
    private static final String STATE = "STATE";

    private User connectedUser;
    private String state;
    private List<Quizz> myListQ = null;
    private List<Quizz> listQShared = null;

    private TextView textViewThemeSharred;

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

        textViewThemeSharred = (TextView) findViewById(R.id.tVSharedQuizz);

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
            LinearLayout divQuestion = (LinearLayout) findViewById(R.id.LlmyQuiz);
            divQuestion.removeAllViews();

            TextView noQuiz = new TextView(this);
            noQuiz.setText("Aucun quiz de créé !");
            noQuiz.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            noQuiz.setTextSize(20);
            noQuiz.setGravity(Gravity.CENTER);

            divQuestion.addView(noQuiz);
            noQuiz.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            noQuiz.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            divQuestion.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
            divQuestion.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        }

        if (listQShared != null && !listQShared.isEmpty()) {
            textViewThemeSharred.setVisibility(View.VISIBLE);
            // Adapter
            QuizzAdapter adapter = new QuizzAdapter(this, listQShared, connectedUser);

            // The list (IHM)
            ListView list = (ListView) findViewById(R.id.listQuizzShared);

            // Initialization of the list
            list.setAdapter(adapter);

            // To accept scroll
            Utility.setListViewHeightBasedOnChildren(list);
        } else {
            textViewThemeSharred.setVisibility(View.INVISIBLE);
        }

        final ImageView stats = (ImageView) findViewById(R.id.stats);
        final ImageView theme = (ImageView) findViewById(R.id.theme);
        final ImageView questionButton = (ImageView) findViewById(R.id.questionButton);

        stats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticTask u = new StatisticTask(Home.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(Home.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuizz:
                intent = new Intent(Home.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.friends:
                intent = new Intent(Home.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
            default:
                break;
        }
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
            if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(QUIZZS_TASKS)) { // Case of QuizzTask
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
                    case ERROR_100:
                        // No statistic for the 1st quizz but we want to access to Statistic
                        Intent myIntent_100 = new Intent(Home.this, StatsGraphics.class);
                        myIntent_100.putExtra(USER, (User) connectedUser);
                        myListQ.addAll(listQShared);
                        myIntent_100.putExtra(LIST_QUIZZ, (ArrayList<Quizz>) myListQ);
                        startActivity(myIntent_100);
                        break;
                    default:
                        Toast.makeText(Home.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                        break;
                }
            }else if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(STATISTICS_TASKS)) { // Case of StatisticTas
                switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
                    case ERROR_000:
                        Intent myIntent = new Intent(Home.this, StatsGraphics.class);
                        List<Statistic> stats = (List<Statistic>) ((List<ReturnObject>) obj).get(1).getObject();
                        myIntent.putExtra(STATISTICS, (ArrayList<Statistic>) stats);
                        myIntent.putExtra(USER, (User) connectedUser);
                        myListQ.addAll(listQShared);
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
        } catch (ClassCastException e) {
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
    }
}
