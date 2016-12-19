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

import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;


public class CreateQuizz extends AppCompatActivity {

    final String STATE = "STATE";
    final String THEME = "THEME";
    final String USER = "USER";
    final String QUIZZNAME = "QUIZZNAME";
    final String TIMER = "TIMER";
    final String QUESTIONS = "QUESTIONS";
    final String RANDOM = "RANDOM";

    // Max themes by quizz
    final static int MAXTHEMESBYQUIZZ = 2;

    // Themes list took by intent
    ArrayList<Theme> myThemes = new ArrayList<>();
    ArrayList<Question> myQuestions = new ArrayList<>();

    // User took by intent
    private User user = new User();
    String state = "";

    private EditText quizzEditText;
    private RadioButton timerOn;
    private RadioButton timerOff;
    private RadioButton chooseQuestionButton;
    private RadioButton randomQuestionButton;
    private EditText nbQuestionsEditText;
    private String nbQuestion;
    private String quizzName;
    private Button validate;
    private TextView themesView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz);

        Intent intent = getIntent();
        final TextView textViewTheme = (TextView) findViewById(R.id.textViewTheme);

        if (intent != null) {

            // Take private variables
            myThemes = (ArrayList<Theme>) intent.getSerializableExtra(THEME);

            user = (User) intent.getSerializableExtra(USER);
            timerOn = (RadioButton) findViewById(R.id.timerOn);
            timerOff = (RadioButton) findViewById(R.id.timerOff);
            quizzEditText = (EditText) findViewById(R.id.name);
            chooseQuestionButton = (RadioButton) findViewById(R.id.chooseQuest);
            randomQuestionButton = (RadioButton) findViewById(R.id.randomQuest);
            themesView = (TextView) findViewById(R.id.themes);
            nbQuestionsEditText = (EditText) findViewById(R.id.nbQuest);

            // Take intent variables
            if(intent.getIntExtra(TIMER, 0) == 1) {
                timerOn.setChecked(true);
            }

            if(!"".equals(intent.getStringExtra(QUIZZNAME))) {
                quizzEditText.setText(intent.getStringExtra(QUIZZNAME));
            }

            if(intent.getIntExtra(RANDOM, 0) == 1) {
                chooseQuestionButton.setChecked(true);
            }
            
            myQuestions = (ArrayList<Question>) intent.getSerializableExtra(QUESTIONS);
            nbQuestionsEditText.setText(Integer.toString(myQuestions.size()));

            if(myThemes.size() != 0) {
                String themesChar = "";
                for(Theme t : myThemes) {
                    if("".equals(themesChar)) {
                        themesChar = t.getName();
                    } else {
                        themesChar = themesChar + " - " + t.getName();
                    }
                }
                themesView.setText(themesChar);
            }

            if(myThemes.size() > 1) {
                textViewTheme.setText("Thèmes");
            }
        }

        nbQuestion = (((EditText) findViewById(R.id.nbQuest)).getText()).toString();
        validate = (Button) findViewById(R.id.validate);

        chooseQuestionButton.setOnClickListener(chooseQListener);
        validate.setOnClickListener(validateListener);

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

            quizzName = (quizzEditText.getText()).toString();

            //Check param
            if ("".equals(quizzEditText)){
                Toast.makeText(CreateQuizz.this,"Choissisez un nom", Toast.LENGTH_LONG).show();
                randomQuestionButton.setChecked(true);
                return;
            }

            // All check ok
            Intent intent = new Intent(CreateQuizz.this, CreateQuizzChoose.class);
            intent.putExtra(QUIZZNAME, quizzName);
            intent.putExtra(THEME, myThemes);
            intent.putExtra(QUESTIONS, myQuestions);
            intent.putExtra(TIMER, timerOff.isChecked() ? 0 : 1);
            intent.putExtra(RANDOM, randomQuestionButton.isChecked() ? 0 : 1);
            intent.putExtra(USER, user);
            startActivity(intent);
        }
    };

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            quizzName = (quizzEditText.getText()).toString();
            nbQuestion = (((EditText) findViewById(R.id.nbQuest)).getText()).toString();

            //Check param
            if ("".equals(quizzEditText)){
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
