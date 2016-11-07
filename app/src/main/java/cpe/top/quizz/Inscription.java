package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * @author Louis Paret
 * @since 06/11/2016
 * @version 0.1
 */

public class Inscription extends AppCompatActivity {

    final String EMAIL = "Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        final Button validButton = (Button) findViewById(R.id.validate);
        final TextView email = (TextView) findViewById(R.id.email);

        validButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inscription.this, InscriptionConfirm.class);
                intent.putExtra(EMAIL, email.getText().toString());
                startActivity(intent);
            }
        });
    }
}