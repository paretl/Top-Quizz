package cpe.top.quizz;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        Bundle extras = thisIntent.getExtras();
        connectedUser = (User) getIntent().getSerializableExtra(USER);
        Quizz quizz = (Quizz) getIntent().getSerializableExtra(QUIZZ);
        int goodQuestions = extras.getInt(GOODQUESTIONS);
        int badQuestions = extras.getInt(BADQUESTIONS);
        scoreView.setText("Fin du Quizz. Score de " + goodQuestions + "/" + (goodQuestions + badQuestions));

        SaveScoreTask sendEmail = new SaveScoreTask(EndGame.this);
        sendEmail.execute(connectedUser.getPseudo(), Integer.toString(quizz.getId()), quizz.getName(), Integer.toString(goodQuestions + badQuestions), Integer.toString(goodQuestions));
    }

    @Override
    public void processFinish(Object obj) {
        if(obj != null){
            switch (((ReturnObject) obj).getCode()){
                case ERROR_000:
                    Intent myIntent = new Intent(EndGame.this, StartQuizz.class);
                    //myIntent.putExtra(QUIZZ, (cpe.top.quizz.beans.Quizz) ((ReturnObject) obj).getObject());
                    startActivity(myIntent);
                    finish();
                    break;
                case ERROR_200:
                    Toast.makeText(EndGame.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_100:
                default:
                    Toast.makeText(EndGame.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else{
            Toast.makeText(EndGame.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(EndGame.this, Home.class);
            startActivity(myIntent);
        }

    }
}
