package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpe.top.quizz.asyncTask.ShowQuestionTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterQuestions;

public class ShowQuestions extends AppCompatActivity implements AsyncUserResponse {

    private static final String USER = "USER";
    private static final String THEME = "THEME";

    private Theme theme;
    private User connectedUser;

    ListViewAdapterQuestions adapter;

    private Collection<Question> questionCollection = null;
    private ArrayList<Question> questionsDatabase = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        final TextView themeView = (TextView) findViewById(R.id.theme);

        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            theme = (Theme) getIntent().getSerializableExtra(THEME);
        }

        if(connectedUser == null) {
            Intent i = new Intent(ShowQuestions.this, MainActivity.class);
            startActivity(i);
        }

        themeView.setText(theme.getName());

        ShowQuestionTask u = new ShowQuestionTask(ShowQuestions.this);
        u.execute(theme.getName(), connectedUser.getPseudo());

    }

    @Override
    public void processFinish(Object obj) {
        //Object cannot be null
        switch (((ReturnObject) obj).getCode()){
            case ERROR_000:
                questionCollection = (Collection<Question>) ((ReturnObject) obj).getObject();

                questionsDatabase.addAll(questionCollection);
                final ListView listQuestions = (ListView) findViewById(R.id.listQuestion);
                listQuestions.setItemsCanFocus(true);

                adapter = new ListViewAdapterQuestions(this, questionsDatabase);
                listQuestions.setAdapter(adapter);

                break;
            case ERROR_200:
                Toast.makeText(ShowQuestions.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_100:
            default:
                Toast.makeText(ShowQuestions.this, "Ce thème ne contient pas de question", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ShowQuestions.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

}
