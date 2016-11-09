package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateQuizzChoose extends AppCompatActivity {

    Button validate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz_choose);

        validate = (Button)findViewById(R.id.validate);
        validate.setOnClickListener(validateListener);

        //TODO : Afficher questions avec checkbox
    }

    public void onBackPressed() {

        Intent intent = new Intent(CreateQuizzChoose.this, CreateQuizz.class);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        intent.putExtra("theme", getIntent().getStringExtra("theme"));
        intent.putExtra("timer", getIntent().getIntExtra("timer", 1));
        startActivity(intent);
    }

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String name = getIntent().getStringExtra("name");
            String theme = getIntent().getStringExtra("theme");
            int timer = getIntent().getIntExtra("timer", 1);

            String[] arrayTheme = theme.toString().split(",");

            for (int i=0; i < arrayTheme.length; i++){
                //TODO : Récup ID theme dans un array
            }
            //TODO : Afficher questions par rapport aux thèmes
            //TODO : Récup questions sélectionnées
            //TODO : Envoie API

        }
    };
}
