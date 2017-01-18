package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.User;

public class Profil extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String USER = "USER";
    private static final String USER_FRIEND = "USER_FRIEND";

    private User connectedUser;
    private User friendProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
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
        if (intent != null && intent.getSerializableExtra(USER) != null && intent.getSerializableExtra(USER_FRIEND) != null) {
            // connectedUser && friendProfil
            this.connectedUser = (User) intent.getSerializableExtra(USER);
            this.friendProfil = (User) intent.getSerializableExtra(USER_FRIEND);

            // Textfields from the IHM
            TextView pseudoFriend = (TextView) findViewById(R.id.pseudoUserProfil);
            pseudoFriend.setText(friendProfil.getPseudo());

            TextView firstLetter = (TextView) findViewById(R.id.firstLetterPseudo);
            firstLetter.setText(String.valueOf(friendProfil.getPseudo().charAt(0)));

            TextView nbQuizzFriend = (TextView) findViewById(R.id.nbQuizzUserProfil);
            if (friendProfil.getQuizz() != null) {
                nbQuizzFriend.setText("Quizz : " + String.valueOf(friendProfil.getQuizz().size()));
            } else {
                nbQuizzFriend.setText("Quizz : 0");
            }

            // Adapter
            if (friendProfil.getQuizz() != null) {
                List<Quizz> listQuizz = new ArrayList<Quizz>(friendProfil.getQuizz());
                QuizzAdapter adapter = new QuizzAdapter(this, listQuizz, connectedUser, false);

                // The list (IHM)
                ListView list = (ListView) findViewById(R.id.listQuizzFriend);

                // Initialization of the list
                list.setAdapter(adapter);
            } else {
                LinearLayout listQuizzContainer = (LinearLayout) findViewById(R.id.listQuizzContainer);
                listQuizzContainer.removeAllViews();

                TextView noQuiz = new TextView(this);
                noQuiz.setText("Aucun quiz...");
                noQuiz.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                noQuiz.setTextSize(20);
                noQuiz.setGravity(Gravity.CENTER);
                noQuiz.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                listQuizzContainer.addView(noQuiz);
            }
        }
    }

    @Override
    public void processFinish(Object obj) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.friends:
                FriendsTask friends = new FriendsTask(Profil.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.findFriend:
                intent = new Intent(Profil.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(Profil.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(Profil.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(Profil.this, MainActivity.class);
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
