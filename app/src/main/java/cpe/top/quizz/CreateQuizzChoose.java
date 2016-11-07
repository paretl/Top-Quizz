package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreateQuizzChoose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz_choose);
    }

    public void onBackPressed() {

        Intent intent = new Intent(CreateQuizzChoose.this, CreateQuizz.class);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        intent.putExtra("theme", getIntent().getIntExtra("theme", 0));
        startActivity(intent);
    }
}
