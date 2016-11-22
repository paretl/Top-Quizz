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
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 22/11/16.
 */

public class CreateQuestion extends AppCompatActivity implements AsyncQuestionResponse {

    final String THEME = "THEME";
    final String USER = "USER";

    // Max themes by questions
    final static int MAXTHEMESBYQUESTION = 2;

    // Nb responses for a question
    final static int NBRESPONSES = 4;

    // List of responses showed
    public ArrayList responsesList = new ArrayList();

    // Define if one checkbox is checked
    public Boolean oneChecked = false;

    // Themes list took by intent
    ArrayList<Theme> myThemes = new ArrayList<>();

    // User took by intent
    private User connectedUser = null;

    private String explanation, question, pseudo;
    private MyAdapter myAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        final TextView textViewTheme = (TextView) findViewById(R.id.textViewTheme);
        // Take extras in intent
        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) intent.getSerializableExtra(USER);
            if(connectedUser == null) {
                Intent i = new Intent(CreateQuestion.this, MainActivity.class);
                startActivity(i);
            }
            pseudo = connectedUser.getPseudo();
            myThemes = (ArrayList<Theme>) intent.getSerializableExtra(THEME);
            if(myThemes.size() > 1) {
                textViewTheme.setText("Thèmes");
            }
        }

        // Bouton to add theme
        final Button addTheme = (Button) findViewById(R.id.addTheme);
        addTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myThemes.size() < MAXTHEMESBYQUESTION) {
                    Intent intent = new Intent(CreateQuestion.this, ChooseTheme.class);
                    intent.putExtra(THEME, myThemes);
                    intent.putExtra(USER, connectedUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateQuestion.this, "Tu ne peux mettre que " + MAXTHEMESBYQUESTION + " thèmes au maximum", Toast.LENGTH_LONG).show();
                }
            }
        });

        // TextView to see which themes are choosed
        final TextView themesView = (TextView) findViewById(R.id.themes);
        String themesChar = "";
        for(Theme t : myThemes) {
            if("".equals(themesChar)) {
                themesChar = t.getName();
            } else {
                themesChar = themesChar + " - " + t.getName();
            }
        }
        themesView.setText(themesChar);

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

                    explanation = (explanationView.getText()).toString();
                    question = (questionView.getText()).toString();

                    // Create response in first
                    Response reponse1 = new Response(myAdapter.getTextContent(0), myAdapter.isCheckedContent(0));
                    Response reponse2 = new Response(myAdapter.getTextContent(1), myAdapter.isCheckedContent(1));
                    Response reponse3 = new Response(myAdapter.getTextContent(2), myAdapter.isCheckedContent(2));
                    Response reponse4 = new Response(myAdapter.getTextContent(3), myAdapter.isCheckedContent(3));

                    // Create List of responses
                    ArrayList<Response> myResponses = new ArrayList<>();
                    myResponses.add(reponse1);
                    myResponses.add(reponse2);
                    myResponses.add(reponse3);
                    myResponses.add(reponse4);

                    // Async Task to add responses in BDD
                    CreateResponseTask createResponsesTask = new CreateResponseTask(CreateQuestion.this);
                    createResponsesTask.execute(myResponses, pseudo);

                    // Create Question after
                    Question myQuestion = new Question(question, explanation, pseudo, myThemes);

                    // Async Task to add question in BDD
                    CreateQuestionTask createQuestionTask = new CreateQuestionTask(CreateQuestion.this);
                    createQuestionTask.execute(myQuestion);

                    Intent intent = new Intent(CreateQuestion.this, Home.class);
                    intent.putExtra(USER, connectedUser);
                    startActivity(intent);
                } else {
                    System.out.println("Formulaire non valide");
                }
            }
        });
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
        for (int i = 1; i <= NBRESPONSES; i++) {
            rep = myAdapter.getTextContent(i - 1);
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

        // test if one of checkbox is choosed
        if (!(checkBox1 || checkBox2 || checkBox3 || checkBox4)) {
            Toast.makeText(CreateQuestion.this, "Vous n'avez pas renseigné de bonne réponse", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void processFinish(Object obj) {

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
            for (int i = 1; i <= NBRESPONSES; i++) {
                ListItem listItem = new ListItem();
                listItem.caption = "Rep " + i;
                listItem.checked = false;
                responsesList.add(listItem);
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return responsesList.size();
        }

        // used to get the text into an EditText
        public String getTextContent(int position) {
            final LinearLayout en = (LinearLayout) myAdapter.getView(position, null, null);
            final EditText ed = (EditText) en.getChildAt(1);
            String rep = (ed.getText()).toString();
            return rep;
        }

        // used to get the value of a CheckBox
        public Boolean isCheckedContent(int position) {
            final LinearLayout en = (LinearLayout) myAdapter.getView(position, null, null);
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
            holder.caption.setText(((ListItem) responsesList.get(position)).caption);
            holder.caption.setId(position);

            holder.checkBox.setChecked(((ListItem) responsesList.get(position)).checked);
            holder.checkBox.setId(position);

            // we need to update adapter once we finish with editing
            holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ((ListItem) responsesList.get(position)).caption = Caption.getText().toString();
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
                    if (oneChecked && Caption.isChecked()) {
                        ((ListItem) responsesList.get(position)).checked = false;
                        notifyDataSetChanged();
                        Toast.makeText(CreateQuestion.this, "Vous ne pouvez cocher qu'une seule réponse", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (Caption.isChecked()) {
                        ((ListItem) responsesList.get(position)).checked = Caption.isChecked();
                        oneChecked = true;
                    } else {
                        ((ListItem) responsesList.get(position)).checked = false;
                        oneChecked = false;
                    }
                }
            });
            return convertView;
        }
    }
}