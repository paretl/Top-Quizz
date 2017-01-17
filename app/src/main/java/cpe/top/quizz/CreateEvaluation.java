package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.List;

import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 17/01/17.
 */

public class CreateEvaluation extends AppCompatActivity implements AsyncResponse {

    private static final String USER = "USER";
    private static final String QUIZZ = "QUIZZ";

    private Bundle bundle;
    private User connectedUser = null;
    private Quizz myQuizz = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_evaluation);

        Intent intent = getIntent();
        // take connectedUser and state
        // If you was in CreateQuestion or Create Quizz before : you have themes, question, explanation, quizz name, number of questions choosed
        bundle = intent.getExtras();
        if (bundle != null) {
            connectedUser = (User) bundle.getSerializable(USER);
            myQuizz = (Quizz) bundle.getSerializable(QUIZZ);
        }

        if (connectedUser == null) {
            Intent i = new Intent(CreateEvaluation.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        TextView tvQuizz = (TextView) findViewById(R.id.quizzName);
        tvQuizz.setText(myQuizz.getName());


    }
    @Override
    public void processFinish(Object obj) {

    }

    public void onBackPressed(){
        Intent intent = new Intent(CreateEvaluation.this, Home.class);
        // Go to Home to prevent beug
        // Add connectedUser and list of Quizz
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }
}
