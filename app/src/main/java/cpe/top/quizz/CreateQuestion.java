package cpe.top.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import cpe.top.quizz.asyncTask.CreateQuestionTask;
import cpe.top.quizz.asyncTask.CreateResponseTask;
import cpe.top.quizz.asyncTask.responses.AsyncQuestionResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Response;
import cpe.top.quizz.beans.Theme;

/**
 * Created by lparet on 22/11/16.
 */

public class CreateQuestion extends AppCompatActivity implements AsyncQuestionResponse {

    final String THEME = "Theme";
    final String PSEUDO = "Pseudo";

    private int nbReponses = 4;
    String explanation, question, pseudo;
    private MyAdapter myAdapter;
    public ArrayList myItems = new ArrayList();
    public Boolean oneChecked = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        Intent intent = getIntent();
        pseudo = intent.getStringExtra(PSEUDO);
        Toast.makeText(CreateQuestion.this, "Crée une question au thème de : " + intent.getStringExtra(THEME), Toast.LENGTH_LONG).show();

        // Initialise listView
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setItemsCanFocus(true);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        // Button to valid question
        final Button validQuestion = (Button) findViewById(R.id.validQuestion);
        validQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(myAdapter)) {
                    final TextView questionView = (TextView) findViewById(R.id.questionLabel);
                    final TextView explanationView = (TextView) findViewById(R.id.explanationLabel);

                    // ajout de la question à la BDD
                    pseudo = "louis"; // Pseudo à récupérer dans l'intent
                    explanation = (explanationView.getText()).toString();
                    question = (questionView.getText()).toString();

                    Response reponse1 = new Response(myAdapter.getTextContent(0), myAdapter.isCheckedContent(0));
                    Response reponse2 = new Response(myAdapter.getTextContent(1), myAdapter.isCheckedContent(1));
                    Response reponse3 = new Response(myAdapter.getTextContent(2), myAdapter.isCheckedContent(2));
                    Response reponse4 = new Response(myAdapter.getTextContent(3), myAdapter.isCheckedContent(3));

                    ArrayList<Theme> myThemes = new ArrayList<>();
                    Theme themes = new Theme(getIntent().getStringExtra(THEME));
                    myThemes.add(themes);

                    ArrayList<Response> myResponses = new ArrayList<>();
                    myResponses.add(reponse1);
                    myResponses.add(reponse2);
                    myResponses.add(reponse3);
                    myResponses.add(reponse4);

                    Question myQuestion = new Question(question, explanation, pseudo, myThemes);

                    CreateQuestionTask createQuestionTask = new CreateQuestionTask(CreateQuestion.this);
                    createQuestionTask.execute(myQuestion);

                    CreateResponseTask createResponsesTask = new CreateResponseTask(CreateQuestion.this);
                    createResponsesTask.execute(myResponses);

                    Intent intent = new Intent(CreateQuestion.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    System.out.println("Form not valid");
                }
            }
        });
    }

    class ViewHolder {
        CheckBox checkBox;
        EditText caption;
    }

    class ListItem {
        Boolean checked;
        String caption;
    }


    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 1; i <= nbReponses; i++) {
                ListItem listItem = new ListItem();
                listItem.caption = "Réponse " + i;
                listItem.checked = false;
                myItems.add(listItem);
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return myItems.size();
        }

        // used to get the text into an EditText
        public String getTextContent(int position) {
            final LinearLayout en = (LinearLayout) myAdapter.getView(position,null, null);
            final EditText ed = (EditText) en.getChildAt(1);
            String rep = (ed.getText()).toString();
            return rep;
        }

        // used to get the value of a CheckBox
        public Boolean isCheckedContent(int position) {
            final LinearLayout en = (LinearLayout) myAdapter.getView(position,null, null);
            final CheckBox ed = (CheckBox) en.getChildAt(0);
            Boolean checked = ed.isChecked();
            return checked;
        }

        public Object getItem(int position) {
            return getView(position, null, null);
        }
        public long getItemId(int position) {
            return getView(position, null, null).getId();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_reponses, null);
                holder.caption = (EditText) convertView.findViewById(R.id.editText);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Fill EditText with the value you have in data source
            holder.caption.setText(((ListItem) myItems.get(position)).caption);
            holder.caption.setId(position);

            holder.checkBox.setChecked(((ListItem) myItems.get(position)).checked);
            holder.checkBox.setId(position);

            // we need to update adapter once we finish with editing
            holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ((ListItem)myItems.get(position)).caption = Caption.getText().toString();
                    }
                }
            });

            // we need to update adapter once we check a box
            // impossible to check a second CheckBox
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = v.getId();
                    final CheckBox Caption = (CheckBox) v;
                    System.out.println(oneChecked);
                    if(oneChecked && Caption.isChecked()) {
                        ((ListItem)myItems.get(position)).checked = false;
                        notifyDataSetChanged();
                        Toast.makeText(CreateQuestion.this, "Vous ne pouvez cocher qu'une seule réponse", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Caption.isChecked()) {
                        ((ListItem) myItems.get(position)).checked = Caption.isChecked();
                        oneChecked = true;
                    } else {
                        ((ListItem) myItems.get(position)).checked = false;
                        oneChecked = false;
                    }
                }
            });
            return convertView;
        }
    }

    private Boolean isValid(MyAdapter myAdapter) {

        // Test question is not empty
        question = (((TextView) findViewById(R.id.questionLabel)).getText()).toString();
        if ("".equals(question)) {
            Toast.makeText(CreateQuestion.this, "Veuillez rentrer une question", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test question is not empty
        explanation = (((TextView) findViewById(R.id.explanationLabel)).getText()).toString();
        if ("".equals(explanation)) {
            Toast.makeText(CreateQuestion.this, "Veuillez rentrer une explication", Toast.LENGTH_LONG).show();
            return false;
        }

        // Rep 1 & 2
        String rep;
        for(int i=1;i<=nbReponses;i++) {
            rep = myAdapter.getTextContent(i-1);
            System.out.println("Réponse " + i + " : " + rep);
            if ("".equals(rep)) {
                Toast.makeText(CreateQuestion.this, "Réponse " + i + " non renseignée", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        // checked
        Boolean checkBox1 = myAdapter.isCheckedContent(0);
        Boolean checkBox2 = myAdapter.isCheckedContent(1);
        Boolean checkBox3 = myAdapter.isCheckedContent(2);
        Boolean checkBox4 = myAdapter.isCheckedContent(3);
        System.out.println("Checkbox 1 " + checkBox1);
        System.out.println("Checkbox 2 " + checkBox2);
        System.out.println("Checkbox 3 " + checkBox3);
        System.out.println("Checkbox 4 " + checkBox4);


        if (!(checkBox1 || checkBox2 || checkBox3 || checkBox4)) {
            Toast.makeText(CreateQuestion.this, "Vous n'avez pas renseigné de bonne réponse", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void processFinish(Object obj) {

    }
}