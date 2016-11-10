package cpe.top.quizz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.MessagingException;

import cpe.top.quizz.utils.Mail;

/**
 *
 * @author Louis Paret
 * @since 06/11/2016
 * @version 0.1
 */

public class Inscription extends AppCompatActivity {

    final static String EMAIL = "Email";
    final static int MINIMALSIZEPASSWORD = 4;
    final static int MINIMALSIZELOGIN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Email text
        final TextView emailView = (TextView) findViewById(R.id.email);
        final Button validButton = (Button) findViewById(R.id.validate);

        validButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isValid()) {
                    sendEmailAsync sendEmail = new sendEmailAsync();
                    sendEmail.execute();
                    Intent intent = new Intent(Inscription.this, InscriptionConfirm.class);
                    intent.putExtra(EMAIL, emailView.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }

    private class sendEmailAsync extends AsyncTask<Void, Void, Void> {

        final String email = (((TextView) findViewById(R.id.email)).getText()).toString();

        final String pseudo = (((TextView) findViewById(R.id.pseudo)).getText()).toString();
        String lien="";

        final String subject = "Top Quizz - Inscription";
        final String body = "Bonjour " + pseudo + ",\n\nBienvenue sur Top Quizz\nVeuillez ouvrir ce lien pour valider votre inscription :\n " + lien + "\n\nA bientôt sur Top Quizz";

        protected Void doInBackground(Void... arg0) {
            try {
                // Send email
                Mail.sendEmail(email, subject, body);
                // Change password on the database
            } catch (MessagingException em) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(Inscription.this, "Impossible d'envoyer l'email.", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }

    // Test method
    public boolean isValid() {

        // Widgets
        final TextView pseudoView = (TextView) findViewById(R.id.pseudo);
        final TextView passwordView = (TextView) findViewById(R.id.password);
        final TextView confirmPasswordView = (TextView) findViewById(R.id.confirmPassword);
        final TextView emailView = (TextView) findViewById(R.id.email);

        // Contents
        final String pseudo = (pseudoView.getText()).toString();
        final String password = (passwordView.getText()).toString();
        final String confirmPassword = (confirmPasswordView.getText()).toString();
        final String email = (emailView.getText()).toString();

        // Test login - Not empty and short
        if(pseudo.length() < MINIMALSIZELOGIN) {
            if("".equals(pseudo)) {
                Toast.makeText(Inscription.this, "Le pseudo n'est pas renseigné.", Toast.LENGTH_LONG).show();
                return false;
            }
            Toast.makeText(Inscription.this, "Le pseudo doit contenir au moins " + MINIMALSIZELOGIN + " caractères.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test password - Not empty and short
        if(password.length() < MINIMALSIZEPASSWORD) {
            if("".equals(password)) {
                Toast.makeText(Inscription.this, "Le mot de passe n'est pas renseigné.", Toast.LENGTH_LONG).show();
                return false;
            }
            Toast.makeText(Inscription.this, "Le mot de passe doit contenir au moins " + MINIMALSIZEPASSWORD + " caractères.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test equals password and confirm
        if(!(password.equals(confirmPassword))) {
            Toast.makeText(Inscription.this, "Le mot de passe et sa confirmation ne sont pas similaires.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test confirm password - Not empty and short
        if(confirmPassword.length() < MINIMALSIZEPASSWORD) {
            if("".equals(confirmPassword)) {
                Toast.makeText(Inscription.this, "Le champ confirmation mot de passe n'est pas renseigné.", Toast.LENGTH_LONG).show();
                return false;
            }
            Toast.makeText(Inscription.this, "Le champ confirmation mot de passe doit contenir au moins " + MINIMALSIZEPASSWORD + " caractères.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test email - Not empty and good email
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if("".equals(email)) {
                Toast.makeText(Inscription.this, "L'email n'est pas renseigné.", Toast.LENGTH_LONG).show();
                return false;
            }
            Toast.makeText(Inscription.this, "L'email n'est pas valide.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Rajouter verif si le mail et le login n'existe pas deja

        return true;
    }
}