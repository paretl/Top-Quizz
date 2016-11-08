package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateQuizz extends AppCompatActivity {

    EditText name = null;
    Spinner theme = null;
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

        name = (EditText)findViewById(R.id.name);
        theme = (Spinner)findViewById(R.id.theme);
        timerOn = (RadioButton)findViewById(R.id.timerOn);
        timerOff = (RadioButton)findViewById(R.id.timerOff);
        chooseQ = (RadioButton)findViewById(R.id.chooseQuest);
        randomQ = (RadioButton)findViewById(R.id.randomQuest);
        nbQ = (EditText)findViewById(R.id.nbQuest);
        validate = (Button)findViewById(R.id.validate);

        chooseQ.setOnClickListener(chooseQListener);
        validate.setOnClickListener(validateListener);

        if (getIntent() != null){

            name.setText(getIntent().getStringExtra("name"));
            theme.setSelection(getIntent().getIntExtra("theme", 0));
            if (getIntent().getIntExtra("timer", 1) == 1) {
                timerOff.setChecked(true);
            }
            else {
                timerOn.setChecked(true);
            }
        }

    }

    private View.OnClickListener chooseQListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (name.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un nom", Toast.LENGTH_LONG).show();
                randomQ.setChecked(true);
            }
            else if (theme.getSelectedItemPosition() == 0){
                Toast.makeText(CreateQuizz.this,"Choissisez un thème", Toast.LENGTH_LONG).show();
                randomQ.setChecked(true);
            }
            else {
                Intent intent = new Intent(CreateQuizz.this, CreateQuizzChoose.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("theme", (int)theme.getSelectedItemId());
                intent.putExtra("timer", (timerOn.isChecked())? 0 : 1 );
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (name.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un nom", Toast.LENGTH_LONG).show();
            }
            else if (theme.getSelectedItemPosition() == 0) {
                Toast.makeText(CreateQuizz.this,"Choissisez un thème", Toast.LENGTH_LONG).show();
            }
            else if (nbQ.getText().toString().equals("")){
                Toast.makeText(CreateQuizz.this,"Choissisez un nombre de questions", Toast.LENGTH_LONG).show();
            }

            //TODO : Envoie API
        }
    };

}
