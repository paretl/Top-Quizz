package cpe.top.quizz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cpe.top.quizz.Utils.UserUtils;
import cpe.top.quizz.beans.User;

/**
 *
 * @author Louis Paret
 * @since 06/11/2016
 * @version 0.1
 */

public class MainActivity extends AppCompatActivity {

private final static String USER = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView pseudo = (TextView) findViewById(R.id.pseudo);
        final TextView password = (TextView) findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckUserTask u = new CheckUserTask(MainActivity.this, pseudo.getText().toString(), password.getText().toString());
                u.execute(pseudo.getText().toString(), password.getText().toString());
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

    public class CheckUserTask extends AsyncTask<String, Integer, User>

    {
        private WeakReference<MainActivity> mActivity = null;

        private String pseudo, password;


        public void link (MainActivity pActivity) {
            mActivity = new WeakReference<MainActivity>(pActivity);
        }

        public CheckUserTask(MainActivity pActivity, String pseudo, String password){
            this.pseudo = pseudo;
            this.password = password;
            link(pActivity);
        }

        @Override
        protected User doInBackground(String... voids) {
            User u = UserUtils.userExist(pseudo, password);
            return (u != null)? u:null;
        }

        @Override
        protected void onPostExecute (User result) {
            if (mActivity.get() != null) {
                if(result != null){
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.putExtra(USER, result);
                    startActivity(intent);
                }else{
                    Toast.makeText(mActivity.get(), "Erreur login/password", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}