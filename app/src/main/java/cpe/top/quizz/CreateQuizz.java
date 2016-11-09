package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Arrays;


public class CreateQuizz extends AppCompatActivity {

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain", "BelgIum", "FRance", "Itttaly", "Germmany", "SpaAain", "FRANCE", "FRANCE", "FRANCE", "FRANCE", "FRANCE", "FRANCE", "FRANCE", "FRANCE", "FRANCE"
    };

    EditText name = null;
    MultiAutoCompleteTextView theme = null;
    RadioButton timerOn = null;
    RadioButton timerOff = null;
    RadioButton chooseQ = null;
    RadioButton randomQ = null;
    EditText nbQ = null;
    Button validate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz);

        name = (EditText) findViewById(R.id.name);
        theme = (MultiAutoCompleteTextView) findViewById(R.id.theme);
        timerOn = (RadioButton) findViewById(R.id.timerOn);
        timerOff = (RadioButton) findViewById(R.id.timerOff);
        chooseQ = (RadioButton) findViewById(R.id.chooseQuest);
        randomQ = (RadioButton) findViewById(R.id.randomQuest);
        nbQ = (EditText) findViewById(R.id.nbQuest);
        validate = (Button) findViewById(R.id.validate);

        chooseQ.setOnClickListener(chooseQListener);
        validate.setOnClickListener(validateListener);

        if (getIntent() != null) {

            name.setText(getIntent().getStringExtra("name"));
            theme.setText(getIntent().getStringExtra("theme"));
            if (getIntent().getIntExtra("timer", 1) == 1) {
                timerOff.setChecked(true);
            } else {
                timerOn.setChecked(true);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.theme);
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private View.OnClickListener chooseQListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Split theme to have a list
            String[] arrayTheme = theme.getText().toString().split(",");

            //Check param
            if (name.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un nom", Toast.LENGTH_LONG).show();
                randomQ.setChecked(true);
                return;
            }
            if (theme.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un thème", Toast.LENGTH_LONG).show();
                randomQ.setChecked(true);
                return;
            }

            //Check if theme exists
            for (int i=0; i < arrayTheme.length; i++){
                //Clean space
                arrayTheme[i] = arrayTheme[i].replace(" ", "");
                //If empty String, don't manage and next
                if (arrayTheme[i] == "" || arrayTheme[i].isEmpty()){
                    if (i==0){
                        Toast.makeText(CreateQuizz.this,"Problème thème, veuillez vérifier", Toast.LENGTH_LONG).show();
                        randomQ.setChecked(true);
                        return;
                    }
                    else{
                        continue;
                    }
                }
                if (!(Arrays.asList(COUNTRIES).contains(arrayTheme[i]))){
                    Toast.makeText(CreateQuizz.this,"Le thème '" + String.valueOf(arrayTheme[i]) + "' n'existe pas", Toast.LENGTH_LONG).show();
                    randomQ.setChecked(true);
                    return;
                }
            }

            //All check ok
            Intent intent = new Intent(CreateQuizz.this, CreateQuizzChoose.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("theme", theme.getText().toString());
            intent.putExtra("timer", (timerOn.isChecked())? 0 : 1 );
            startActivity(intent);

        }
    };

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Split theme to have a list
            String[] arrayTheme = theme.getText().toString().split(",");

            //Check param
            if (name.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un nom", Toast.LENGTH_LONG).show();
                return;
            }
            if (theme.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un thème", Toast.LENGTH_LONG).show();
                return;
            }

            //Check if theme exists
            for (int i=0; i < arrayTheme.length; i++){
                //Clean space
                arrayTheme[i] = arrayTheme[i].replace(" ", "");
                //If empty String, don't manage and next
                if (arrayTheme[i] == "" || arrayTheme[i].isEmpty()){
                    if (i==0){
                        Toast.makeText(CreateQuizz.this,"Problème thème, veuillez vérifier", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        continue;
                    }
                }
                if (!(Arrays.asList(COUNTRIES).contains(arrayTheme[i]))){
                    Toast.makeText(CreateQuizz.this,"Le thème '" + String.valueOf(arrayTheme[i]) + "' n'existe pas", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (nbQ.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un nombre de questions", Toast.LENGTH_LONG).show();
                return;
            }
            //All check ok
            for (int i=0; i < arrayTheme.length; i++){
                if (arrayTheme[i] == ""){
                    continue;
                }
                //TODO : Récup ID theme dans un array
            }
            //TODO : Random question par rapport aux thèmes -> Récup ID questions
            //TODO : Envoie API

        }
    };

}
