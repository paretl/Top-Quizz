package cpe.top.quizz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpe.top.quizz.asyncTask.AddUserTask;

/**
 * Created by lparet on 22/11/16.
 */

public class CreateQuestion extends AppCompatActivity {

    private LinearLayout mLayout;
    private int nbReponses = 2;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        final TextView textView_reponse1 = (TextView) findViewById(R.id.response1);
        final TextView textView_reponse2 = (TextView) findViewById(R.id.response2);
        final TextView questionLabel = (TextView) findViewById(R.id.questionLabel);

        questionLabel.setSingleLine(false);

        final Context context = textView_reponse1.getContext();

        mLayout = (LinearLayout) findViewById(R.id.reponsesLayout);

        String reponse1 = (textView_reponse1.getText()).toString();
        String reponse2 = (textView_reponse2.getText()).toString();

        Map<String, TextView> reponses = new HashMap<>();
        reponses.put(reponse1, textView_reponse1);
        reponses.put(reponse2, textView_reponse2);

        final Button responseLessButton = (Button) findViewById(R.id.responseLessButton);
        responseLessButton.setVisibility(View.INVISIBLE);

        final Button responseMoreButton = (Button) findViewById(R.id.responseMoreButton);
        responseMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbReponses==3) {
                    responseMoreButton.setVisibility(View.INVISIBLE);
                }
                nbReponses++;
                responseLessButton.setVisibility(View.VISIBLE);
                mLayout.addView(createEditText(nbReponses, context));
            }
        });

        final Button validQuestion = (Button) findViewById(R.id.validQuestion);
        validQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        responseLessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbReponses==3) {
                    responseLessButton.setVisibility(View.INVISIBLE);
                }
                mLayout.removeViewAt(nbReponses-1);
                responseMoreButton.setVisibility(View.VISIBLE);
                nbReponses--;
            }
        });
    }

    private TextView createEditText(int nbReponses, Context context) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(context);
        editText.setHint("Réponse " + nbReponses);
        editText.setId(nbReponses);
        editText.setSingleLine(true);
        editText.setLayoutParams(lparams);
        return editText;
    }
}
