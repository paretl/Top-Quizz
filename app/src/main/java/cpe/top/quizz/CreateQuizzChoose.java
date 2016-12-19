package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import cpe.top.quizz.asyncTask.GetQuestionsByThemesAndUserTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

public class CreateQuizzChoose extends AppCompatActivity implements AsyncUserResponse {

    Button validate = null;

    final String THEME = "THEME";
    final String USER = "USER";
    final String QUIZZNAME = "QUIZZNAME";
    final String TIMER = "TIMER";
    final String QUESTIONS = "QUESTIONS";

    private User user = new User();
    private ArrayList<Theme> themes = new ArrayList<Theme>();
    private int timer;
    private String quizzName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz_choose);

        validate = (Button)findViewById(R.id.validate);
        validate.setOnClickListener(validateListener);
        if (getIntent() != null) {
            themes = (ArrayList<Theme>) getIntent().getSerializableExtra(THEME);
            user = (User) getIntent().getSerializableExtra(USER);
        }

        System.out.println(themes.get(1).getName());

        // TODO : Récupérer questions par rapport aux thèmes et à l'utilisateur
        GetQuestionsByThemesAndUserTask u = new GetQuestionsByThemesAndUserTask(CreateQuizzChoose.this);
        u.execute(user, themes);

        // TODO : Afficher questions avec checkbox - à partir d'une liste de question
    }

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(CreateQuizzChoose.this, CreateQuizz.class);
            intent.putExtra(QUIZZNAME, getIntent().getStringExtra(QUIZZNAME));
            intent.putExtra(THEME, themes);
            //intent.putExtra(QUESTIONS, getIntent().getStringExtra(QUESTIONS));
            intent.putExtra(TIMER, getIntent().getIntExtra(TIMER, 1));
            intent.putExtra(USER, user);
            startActivity(intent);

            // Need : liste question

            //TODO : Récup liste question selectionnées
            //TODO : Envoie API

        }
    };

    @Override
    public void processFinish(Object obj) {

    }
}
