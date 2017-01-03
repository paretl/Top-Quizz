package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpe.top.quizz.asyncTask.GetAllQuizzsTask;
import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;
import cpe.top.quizz.utils.ListViewAdapterQuizz;

/**
 * Created by lparet on 03/01/17.
 */

public class FindQuizz extends AppCompatActivity implements AsyncStatisticResponse {

    private static final String USER = "USER";

    private User connectedUser;
    private List<Quizz> listQ = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectedUser = (User) getIntent().getSerializableExtra(USER);

        final GetAllQuizzsTask getAllQuizzs = new GetAllQuizzsTask(FindQuizz.this);
        getAllQuizzs.execute("Maxence");

        display();
    }

    private void display(){
        setContentView(R.layout.activity_find_quizz);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        if (listQ != null && !listQ.isEmpty()) {
            // Adapter
            ListViewAdapterQuizz adapter = new ListViewAdapterQuizz(this, listQ, connectedUser);

            // The list (IHM)
            ListView list = (ListView) findViewById(R.id.listQuizz);

            // Initialization of the list
            list.setAdapter(adapter);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        display();
    }

    @Override
    public void processFinish(Object obj) {
        if(((List<Object>) obj).get(0) != null) {
            switch (((ReturnObject) ((List<Object>) obj).get(0)).getCode()) {
                case ERROR_000:
                    listQ = new ArrayList<>();
                    listQ.addAll((Collection<Quizz>) ((ReturnObject) ((List<Object>) obj).get(0)).getObject());
                    onRestart();
                    break;
                case ERROR_200:
                    Toast.makeText(FindQuizz.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(FindQuizz.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(FindQuizz.this, "Aucun quiz trouvé", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(FindQuizz.this, Home.class);
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }
}
