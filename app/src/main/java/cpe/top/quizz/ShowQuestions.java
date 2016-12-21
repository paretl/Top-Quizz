package cpe.top.quizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        final TextView themeView = (TextView) findViewById(R.id.theme);

        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            theme = (Theme) getIntent().getSerializableExtra(THEME);
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
}
