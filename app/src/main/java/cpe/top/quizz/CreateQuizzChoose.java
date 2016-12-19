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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cpe.top.quizz.asyncTask.GetQuestionsByThemesAndUserTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

public class CreateQuizzChoose extends AppCompatActivity implements AsyncUserResponse {



    final String THEME = "THEME";
    final String USER = "USER";
    final String QUIZZNAME = "QUIZZNAME";
    final String TIMER = "TIMER";
    final String QUESTIONS = "QUESTIONS";
    final String RANDOM = "RANDOM";

    private Button validate;
    private TextView nbQuestions_view;
    private User user = new User();
    private ArrayList<Theme> themes = new ArrayList<Theme>();
    private ArrayList<Question> questionsChoosed = new ArrayList<>();
    private int timer;
    private String quizzName;
    private int nbQuestions = 0;

    private MyAdapter myAdapter;
    // List of responses showed
    public ArrayList questionsList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz_choose);

        nbQuestions_view = (TextView) findViewById(R.id.nbQuestions);

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(validateListener);
        if (getIntent() != null) {
            themes = (ArrayList<Theme>) getIntent().getSerializableExtra(THEME);
            user = (User) getIntent().getSerializableExtra(USER);
            if((getIntent().getSerializableExtra(QUESTIONS)) != null) {
                if (((ArrayList<Question>) getIntent().getSerializableExtra(QUESTIONS)).size() > 0) {
                    questionsChoosed = (ArrayList<Question>) getIntent().getSerializableExtra(QUESTIONS);
                    nbQuestions_view.setText(Integer.toString(questionsChoosed.size()));
                    nbQuestions = questionsChoosed.size();
                }
            }

            if(user==null) {
                Intent t = new Intent(CreateQuizzChoose.this, MainActivity.class);
                startActivity(t);
            }
        }

        // TODO : Récupérer questions par rapport aux thèmes et à l'utilisateur
        //GetQuestionsByThemesAndUserTask u = new GetQuestionsByThemesAndUserTask(CreateQuizzChoose.this);
        //u.execute(user, themes);
        ArrayList<Question> questionsView = new ArrayList<Question>();
        questionsView.add(new Question("test", "test", user.getPseudo(), null, null, null));
        questionsView.add(new Question("test2", "test2", user.getPseudo(), null, null, null));
        questionsView.add(new Question("test3", "test3", user.getPseudo(), null, null, null));
        questionsView.add(new Question("test4", "test10", user.getPseudo(), null, null, null));
        questionsView.add(new Question("test5", "test5", user.getPseudo(), null, null, null));
        questionsView.add(new Question("test6", "test8", user.getPseudo(), null, null, null));
        questionsView.add(new Question("test7", "test8", user.getPseudo(), null, null, null));
        questionsView.add(new Question("test8", "test8", user.getPseudo(), null, null, null));

        // TODO : Afficher questions avec checkbox - à partir d'une liste de question
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setItemsCanFocus(true);
        myAdapter = new CreateQuizzChoose.MyAdapter(questionsView);
        listView.setAdapter(myAdapter);
    }

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(CreateQuizzChoose.this, CreateQuizz.class);
            intent.putExtra(QUIZZNAME, getIntent().getStringExtra(QUIZZNAME));
            intent.putExtra(THEME, themes);
            intent.putExtra(TIMER, getIntent().getIntExtra(TIMER, 0));
            intent.putExtra(RANDOM, getIntent().getIntExtra(RANDOM, 0));
            intent.putExtra(USER, user);
            intent.putExtra(QUESTIONS, questionsChoosed);
            startActivity(intent);
        }
    };

    @Override
    public void processFinish(Object obj) {

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
        public MyAdapter(ArrayList<Question> questions) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (Question q : questions) {
                CreateQuizzChoose.ListItem listItem = new CreateQuizzChoose.ListItem();
                listItem.caption = q.getLabel();
                listItem.checked = false;
                System.out.println(q.getLabel());
                // Need to do a second for because 2 questions hasn't same address
                System.out.println(questionsChoosed);
                for(Question qChoosed : questionsChoosed) {
                    System.out.println(qChoosed.getLabel());
                    if((qChoosed.getLabel()).equals(q.getLabel())) {
                        listItem.checked = true;
                    }
                }
                listItem.question = q;
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
                        questionsChoosed.remove(((ListItem) questionsList.get(v.getId())).question);
                        nbQuestions--;
                        nbQuestions_view.setText(Integer.toString(nbQuestions));
                    }
                }
            });
            return convertView;
        }
    }
}
