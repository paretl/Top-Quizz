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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.User;

public class FriendsDisplay extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String USER = "USER";
    private static final String LIST_FRIENDS = "LIST_FRIENDS";
    private User connectedUser;
    private List<User> listFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_display);
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
        if (intent != null && intent.getSerializableExtra(USER) != null && intent.getSerializableExtra(LIST_FRIENDS) != null && ((List<User>) intent.getSerializableExtra(LIST_FRIENDS)).size() != 0) {
            this.listFriends = (List<User>) intent.getSerializableExtra(LIST_FRIENDS);
            this.connectedUser = (User) intent.getSerializableExtra(USER);

            // Adapter
            FriendsAdapter adapter = new FriendsAdapter(this, listFriends, connectedUser);

            // The list (IHM)
            ListView list = (ListView) findViewById(R.id.listFriends);

            // Initialization of the list
            list.setAdapter(adapter);
        } else { // No Friends
            LinearLayout divFriends = (LinearLayout) findViewById(R.id.divFriends);
            divFriends.removeAllViews();

            TextView noQuiz = new TextView(this);
            noQuiz.setText("Aucun ami dans votre liste...");
            noQuiz.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            noQuiz.setTextSize(20);
            noQuiz.setGravity(Gravity.CENTER);
            noQuiz.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            divFriends.addView(noQuiz);
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
                FriendsTask friends = new FriendsTask(FriendsDisplay.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.findFriend:
                intent = new Intent(FriendsDisplay.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(FriendsDisplay.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(FriendsDisplay.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.evalMode:
                intent = new Intent(FriendsDisplay.this, EvalMode.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(FriendsDisplay.this, MainActivity.class);
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
