package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cpe.top.quizz.beans.User;

public class Home extends AppCompatActivity {

    private static final String USER = "USER";

    private User connectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            Toast.makeText(Home.this, connectedUser.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
