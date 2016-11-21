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

import java.util.Random;

import javax.mail.MessagingException;

import cpe.top.quizz.utils.Mail;

/**
 *
 * @author Louis Paret
 * @since 06/11/2016
 * @version 0.1
 */

public class ResetPassword extends AppCompatActivity {
    final String EMAIL = "Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final Button validButton = (Button) findViewById(R.id.validate);
        final TextView emailView = (TextView) findViewById(R.id.email);

        validButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String email = (emailView.getText()).toString();

                if(isValid()) {
                    sendEmailAsync sendEmail = new sendEmailAsync();
                    sendEmail.execute();

                    Intent intent = new Intent(ResetPassword.this, ResetPasswordConfirm.class);
                    intent.putExtra(EMAIL, email);
                    startActivity(intent);
                }
            }
        });
    }

    protected boolean isValid() {

        final TextView emailView = (TextView) findViewById(R.id.email);
        final String email = (emailView.getText()).toString();

        // Test email - Not empty and good email
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if("".equals(email)) {
                Toast.makeText(ResetPassword.this, "L'email n'est pas renseigné.", Toast.LENGTH_LONG).show();
                return false;
            }
            Toast.makeText(ResetPassword.this, "L'email n'est pas valide.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Rajouter tests pour vérifier si le mail est dans la base

        return true;
    }

    // This method send an email in background
    private class sendEmailAsync extends AsyncTask<Void, Void, Void> {

        final TextView emailView = (TextView) findViewById(R.id.email);
        final String email = (emailView.getText()).toString();

        final String newPassword = generatePassword();

        final String subject = "Top Quizz - Nouveau mot de passe";
        final String body = "Bonjour,\n\nVotre nouveau mot de passe est : " + newPassword + "\n\nA bientôt sur Top Quizz";

        protected Void doInBackground(Void... arg0) {
            try {
                // Send email
                Mail.sendEmail(email, subject, body);
                // appel api 54.93.98.119:8090/user/changePassword?password=newPassword&email=email
            } catch (MessagingException em) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(ResetPassword.this, "Impossible d'envoyer l'email.", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        // Method that create a random password of 10 str
        private String generatePassword() {
            String str = "ABCDEFGHJKLMNOPQRSTUVWXYZ123456789";
            String ar="";
            Random r = new Random();
            int te=0;
            for(int i=1;i<=10;i++){
                te=r.nextInt(34);
                ar=ar+str.charAt(te);
            }
            System.out.println(ar);
            return ar;
        }
    }
}
