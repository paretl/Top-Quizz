package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class CreateQuizz extends AppCompatActivity {

    EditText name = null;
    Spinner theme = null;
    RadioButton chooseQ = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz);

        name = (EditText)findViewById(R.id.name);
        theme = (Spinner)findViewById(R.id.theme);
        chooseQ = (RadioButton)findViewById(R.id.chooseQuest);

        chooseQ.setOnClickListener(chooseQListener);

        if (getIntent() != null){

            name.setText(getIntent().getStringExtra("name"));
            theme.setSelection(getIntent().getIntExtra("theme", 0));
        }

    }

    private View.OnClickListener chooseQListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(CreateQuizz.this, CreateQuizzChoose.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("theme", (int)theme.getSelectedItemId());
            startActivity(intent);
        }
    };

}
