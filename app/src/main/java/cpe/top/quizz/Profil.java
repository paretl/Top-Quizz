package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
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

import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.User;

public class Profil extends AppCompatActivity {

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
                QuizzAdapter adapter = new QuizzAdapter(this, listQuizz, connectedUser);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Profil.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}