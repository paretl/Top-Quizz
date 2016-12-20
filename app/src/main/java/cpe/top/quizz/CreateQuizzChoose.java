package cpe.top.quizz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

import cpe.top.quizz.asyncTask.GetQuestionsByThemesAndUserTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

public class CreateQuizzChoose extends AppCompatActivity implements AsyncUserResponse {
    private static final String THEME = "THEME";
    private static final String USER = "USER";
    private static final String QUIZZNAME = "QUIZZNAME";
    private static final String TIMER = "TIMER";
    private static final String QUESTIONS = "QUESTIONS";
    private static final String RANDOM = "RANDOM";

    // Graphic element
    private Button validate;
    private TextView nbQuestions_view;

    // global variables
    private User connectedUser = new User();
    // List of themes already choosed
    private ArrayList<Theme> myThemes = new ArrayList<Theme>();
    // List of question already choosed
    private ArrayList<Question> questionsChoosed = new ArrayList<>();
    // List of questions in database
    private ArrayList<Question> questionsDatabase = new ArrayList<>();
    // List of questions showed
    public ArrayList questionsList = new ArrayList();
    // Nb questions
    private int nbQuestions = 0;
    // adapter to see questions
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz_choose);

        // Initialize graphic element
        nbQuestions_view = (TextView) findViewById(R.id.nbQuestions);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(validateListener);

        if (getIntent() != null) {
            // get intents
            myThemes = (ArrayList<Theme>) getIntent().getSerializableExtra(THEME);
            connectedUser = (User) getIntent().getSerializableExtra(USER);
            questionsChoosed = (ArrayList<Question>) getIntent().getSerializableExtra(QUESTIONS);
            // get nb of questions choosed
            if(questionsChoosed != null) {
                if (questionsChoosed.size() > 0) {
                    nbQuestions_view.setText(Integer.toString(questionsChoosed.size()));
                    nbQuestions = questionsChoosed.size();
                }
            }
            // if user is not connected
            if(connectedUser == null) {
                Intent t = new Intent(CreateQuizzChoose.this, MainActivity.class);
                startActivity(t);
            }

            // asyncTask to get all questions
            GetQuestionsByThemesAndUserTask u = new GetQuestionsByThemesAndUserTask(CreateQuizzChoose.this);
            u.execute(connectedUser, myThemes);
        }
    }

    // Listener to validate
    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(CreateQuizzChoose.this, CreateQuizz.class);
            intent.putExtra(QUIZZNAME, getIntent().getStringExtra(QUIZZNAME));
            intent.putExtra(THEME, myThemes);
            intent.putExtra(TIMER, getIntent().getIntExtra(TIMER, 0));
            intent.putExtra(RANDOM, getIntent().getIntExtra(RANDOM, 0));
            intent.putExtra(USER, connectedUser);
            intent.putExtra(QUESTIONS, questionsChoosed);
            startActivity(intent);
        }
    };

    @Override
    public void processFinish(Object obj) {
        Collection<Question> questions = (Collection<Question>) ((ReturnObject) obj).getObject();
        if(questions != null) {
            questionsDatabase.addAll(questions);
            final ListView listView = (ListView) findViewById(R.id.listView);
            listView.setItemsCanFocus(true);
            myAdapter = new CreateQuizzChoose.MyAdapter(questionsDatabase);
            listView.setAdapter(myAdapter);
        } else {
            Toast.makeText(CreateQuizzChoose.this,"Pas de questions pour le(s) thème(s) choisi(s)", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CreateQuizzChoose.this, CreateQuizz.class);
            intent.putExtra(QUIZZNAME, getIntent().getStringExtra(QUIZZNAME));
            intent.putExtra(THEME, myThemes);
            intent.putExtra(TIMER, getIntent().getIntExtra(TIMER, 0));
            intent.putExtra(RANDOM, 0);
            intent.putExtra(USER, connectedUser);
            intent.putExtra(QUESTIONS, questionsChoosed);
            startActivity(intent);
        }
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView caption;
        Question question;
    }

    class ListItem {
        Boolean checked;
        String caption;
        Question question;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(ArrayList<Question> questionsDatabase) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (Question q : questionsDatabase) {
                CreateQuizzChoose.ListItem listItem = new CreateQuizzChoose.ListItem();
                listItem.caption = q.getLabel();
                listItem.checked = false;
                listItem.question = q;
                // Need to do a second for because 2 questions hasn't same address
                for(Question qChoosed : questionsChoosed) {
                    if((qChoosed.getLabel()).equals(q.getLabel())) {
                        listItem.checked = true;
                    }
                }
                questionsList.add(listItem);
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return questionsList.size();
        }

        public Object getItem(int position) {
            return getView(position, null, null);
        }

        public long getItemId(int position) {
            return getView(position, null, null).getId();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final CreateQuizzChoose.ViewHolder holder;
            if (convertView == null) {
                holder = new CreateQuizzChoose.ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_questions, null);
                holder.caption = (TextView) convertView.findViewById(R.id.textView);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (CreateQuizzChoose.ViewHolder) convertView.getTag();
            }
            //Fill EditText with the value you have in data source
            holder.caption.setText(((CreateQuizzChoose.ListItem) questionsList.get(position)).caption);
            holder.caption.setId(position);

            holder.checkBox.setChecked(((CreateQuizzChoose.ListItem) questionsList.get(position)).checked);
            holder.checkBox.setId(position);

            holder.question = ((ListItem) questionsList.get(position)).question;

            // we need to update adapter once we check a box
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CheckBox Caption = (CheckBox) v;
                    if (Caption.isChecked()) {
                        questionsChoosed.add(((ListItem) questionsList.get(v.getId())).question);
                        nbQuestions++;
                        nbQuestions_view.setText(Integer.toString(nbQuestions));
                    } else {
                        for(Question qChoosed : questionsChoosed) {
                            if((qChoosed.getLabel()).equals(((ListItem) questionsList.get(v.getId())).question.getLabel())) {
                                questionsChoosed.remove(qChoosed);
                            }
                        }
                        nbQuestions--;
                        nbQuestions_view.setText(Integer.toString(nbQuestions));
                    }
                }
            });
            return convertView;
        }
    }
}
