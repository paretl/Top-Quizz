package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;


public class CreateQuizz extends AppCompatActivity {

    final String THEME = "THEME";
    final String USER = "USER";
    final String STATE = "STATE";
    final String QUIZZNAME = "QUIZZNAME";
    final String TIMER = "TIMER";

    // Max themes by quizz
    final static int MAXTHEMESBYQUIZZ = 2;

    // Themes list took by intent
    ArrayList<Theme> myThemes = new ArrayList<>();

    // User took by intent
    private User user = new User();
    String state = "";

    private String quizzName;
    private Boolean timerOn;
    private Boolean timerOff;
    private RadioButton chooseQuestionButton;
    private RadioButton randomQuestionButton;
    private Boolean chooseQuestion;
    private Boolean randomQuestion;
    private String nbQuestion;
    private Button validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz);

        quizzName = (((EditText) findViewById(R.id.name)).getText()).toString();
        timerOn = ((RadioButton) findViewById(R.id.timerOn)).isChecked();
        timerOff = ((RadioButton) findViewById(R.id.timerOff)).isChecked();
        chooseQuestionButton = (RadioButton) findViewById(R.id.chooseQuest);
        chooseQuestion = chooseQuestionButton.isChecked();
        randomQuestionButton = (RadioButton) findViewById(R.id.randomQuest);
        randomQuestion = randomQuestionButton.isChecked();
        nbQuestion = (((EditText) findViewById(R.id.nbQuest)).getText()).toString();
        validate = (Button) findViewById(R.id.validate);

        chooseQuestionButton.setOnClickListener(chooseQListener);
        validate.setOnClickListener(validateListener);

        final TextView textViewTheme = (TextView) findViewById(R.id.textViewTheme);
        Intent intent = getIntent();
        if (intent != null) {
            user = (User) intent.getSerializableExtra(USER);
            myThemes = (ArrayList<Theme>) intent.getSerializableExtra(THEME);
            if(myThemes.size() > 1) {
                textViewTheme.setText("Thèmes");
            }
        }

        // TextView to see which themes are choosed
        final TextView themesView = (TextView) findViewById(R.id.themes);
        String themesChar = "";
        for(Theme t : myThemes) {
            if("".equals(themesChar)) {
                themesChar = t.getName();
            } else {
                themesChar = themesChar + " - " + t.getName();
            }
        }
        themesView.setText(themesChar);

        // Bouton to add theme
        final Button addTheme = (Button) findViewById(R.id.addTheme);
        addTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myThemes.size() < MAXTHEMESBYQUIZZ) {
                    Intent intent = new Intent(CreateQuizz.this, ChooseTheme.class);
                    state = "Quizz";
                    intent.putExtra(USER, user);
                    intent.putExtra(STATE, state);
                    intent.putExtra(THEME, myThemes);
                    intent.putExtra(USER, user);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateQuizz.this, "Tu ne peux mettre que " + MAXTHEMESBYQUIZZ + " thèmes au maximum", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private View.OnClickListener chooseQListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            quizzName = (((EditText) findViewById(R.id.name)).getText()).toString();

            //Check param
            if ("".equals(quizzName)){
                Toast.makeText(CreateQuizz.this,"Choissisez un nom", Toast.LENGTH_LONG).show();
                randomQuestionButton.setChecked(true);
                return;
            }

            // All check ok
            Intent intent = new Intent(CreateQuizz.this, CreateQuizzChoose.class);
            intent.putExtra(QUIZZNAME, quizzName);
            intent.putExtra(THEME, myThemes);
            intent.putExtra(TIMER, timerOn? 0 : 1 );
            intent.putExtra(USER, user);
            startActivity(intent);
        }
    };

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            quizzName = (((EditText) findViewById(R.id.name)).getText()).toString();
            nbQuestion = (((EditText) findViewById(R.id.nbQuest)).getText()).toString();

            //Check param
            if ("".equals(quizzName)){
                Toast.makeText(CreateQuizz.this,"Choissisez un nom", Toast.LENGTH_LONG).show();
                return;
            }

            if ("".equals(nbQuestion)){
                Toast.makeText(CreateQuizz.this,"Choissisez un nombre de questions", Toast.LENGTH_LONG).show();
                return;
            }

            //TODO : Random question par rapport aux thèmes -> Récup ID questions
            //TODO : Envoie API

        }
    };

}
