package cpe.top.quizz;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cpe.top.quizz.beans.User;

import cpe.top.quizz.asyncTask.SaveScoreTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;

public class EndGame extends AppCompatActivity implements AsyncUserResponse {

    private static final String GOODQUESTIONS = "GOODQUESTIONS";
    private static final String BADQUESTIONS = "BADQUESTIONS";
    private static final String USER = "USER";
    private static final String QUIZZ = "QUIZZ";

    private User connectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);


        Button closeEnd = (Button) findViewById(R.id.CloseEnd);

        closeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Score (IHM)
        TextView scoreView = (TextView) findViewById(R.id.scoreView);
        Intent thisIntent = getIntent();
        if (thisIntent != null && thisIntent.getSerializableExtra(USER) != null && thisIntent.getSerializableExtra(GOODQUESTIONS) != null && thisIntent.getSerializableExtra(BADQUESTIONS) != null) {
            Bundle extras = thisIntent.getExtras();
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            Quizz quizz = (Quizz) getIntent().getSerializableExtra(QUIZZ);
            int goodQuestions = extras.getInt(GOODQUESTIONS);
            int badQuestions = extras.getInt(BADQUESTIONS);
            scoreView.setText("Quiz achevé avec succès !\n Score de " + goodQuestions + "/" + (goodQuestions + badQuestions));

            SaveScoreTask save = new SaveScoreTask(EndGame.this);
            save.execute(connectedUser.getPseudo(), Integer.toString(quizz.getId()), quizz.getName(), Integer.toString(goodQuestions + badQuestions), Integer.toString(goodQuestions));
        }
    }

    @Override
    public void processFinish(Object obj) {
        if(obj != null){
            switch (((ReturnObject) obj).getCode()){
                case ERROR_000:
                    //Nothing to do
                    break;
                case ERROR_200:
                    Toast.makeText(EndGame.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(EndGame.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else{
            Toast.makeText(EndGame.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(EndGame.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
         }
        return true;
    }
}
