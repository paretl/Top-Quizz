package cpe.top.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cpe.top.quizz.asyncTask.QuizzTask;
import cpe.top.quizz.asyncTask.responses.AsyncQuizzResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;

public class Home extends AppCompatActivity implements AsyncQuizzResponse {

    private static final String USER = "USER";
    private static final String QUIZZ = "QUIZZ";

    private User connectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            Toast.makeText(Home.this, connectedUser.toString(), Toast.LENGTH_SHORT).show();
        }

        final Button startQuizz = (Button) findViewById(R.id.buttonStartQuizz);

        startQuizz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizzTask cT = new QuizzTask(Home.this);
                // Test avec données en dur => tâche assignée à qqun d'autre
                cT.execute("QuizzJava1");
            }
        });
    }

    @Override
    public void processFinish(Object obj) {
        switch (((ReturnObject) obj).getCode()){
            case ERROR_000:
                Intent myIntent = new Intent(Home.this, StartQuizz.class);
                myIntent.putExtra(QUIZZ, (Quizz) ((ReturnObject) obj).getObject());
                startActivity(myIntent);
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
