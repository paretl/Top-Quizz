package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
        return true;
    }
}
