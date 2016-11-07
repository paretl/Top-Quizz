package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * @author Louis Paret
 * @since 06/11/2016
 * @version 0.1
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isValid()) {
                    //Intent intent = new Intent(MainActivity.this, Questionnaire.class);
                    //startActivity(intent);
                }

            }
        });

        final TextView createAccount = (TextView) findViewById(R.id.createAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Inscription.class);
                startActivity(intent);
            }
        });

        final TextView renvoiMdp = (TextView) findViewById(R.id.mdpForgot);

        renvoiMdp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });
    }

    protected boolean isValid() {

        final TextView pseudoView = (TextView) findViewById(R.id.pseudo);
        final TextView passwordView = (TextView) findViewById(R.id.password);

        final String pseudo = (pseudoView.getText()).toString();
        final String password = (passwordView.getText()).toString();

        // Test login - Not empty
        if("".equals(pseudo)) {
            Toast.makeText(MainActivity.this, "Le pseudo n'est pas renseigné.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test password - Not empty
        if("".equals(password)) {
            Toast.makeText(MainActivity.this, "Le mot de passe n'est pas renseigné.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}