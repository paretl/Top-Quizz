package cpe.top.quizz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.GetQuestionsByThemesAndUserTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

public class CreateQuizzChoose extends AppCompatActivity implements AsyncResponse, NavigationView.OnNavigationItemSelectedListener {
    private static final String THEME = "THEME";
    private static final String USER = "USER";
    private static final String QUIZZNAME = "QUIZZNAME";
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

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quizz_choose);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Take connectedUser, themes, quizzName, nbQuestions, questionsChoosed...
        bundle = getIntent().getExtras();

        // Initialize graphic element
        nbQuestions_view = (TextView) findViewById(R.id.nbQuestions);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(validateListener);

        if (bundle != null) {
            // get intents
            myThemes = (ArrayList<Theme>) bundle.getSerializable(THEME);
            connectedUser = (User) bundle.getSerializable(USER);

            // get nb of questions choosed
            if(bundle.getSerializable(QUESTIONS) != null) {
                questionsChoosed = (ArrayList<Question>) bundle.getSerializable(QUESTIONS);
                if (questionsChoosed.size() > 0) {
                    nbQuestions_view.setText(Integer.toString(questionsChoosed.size()));
                    nbQuestions = questionsChoosed.size();
                }
            }

            // if user is not connected
            if(connectedUser == null) {
                Intent t = new Intent(CreateQuizzChoose.this, MainActivity.class);
                startActivity(t);
                finish();
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
            intent.putExtras(bundle);
            // to overwrite previous array of questions
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
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
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
                        String label = ((ListItem) questionsList.get(v.getId())).question.getLabel();
                        for(Question qChoosed : questionsChoosed) {
                            if((qChoosed.getLabel()).equals(label)) {
                                questionsChoosed.remove(qChoosed);
                                break;
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

    public void onBackPressed(){
        Intent intent = new Intent(CreateQuizzChoose.this, CreateQuizz.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.friends:
                FriendsTask friends = new FriendsTask(CreateQuizzChoose.this);
                friends.execute(connectedUser.getPseudo());
                break;
            case R.id.findFriend:
                intent = new Intent(CreateQuizzChoose.this, ChooseFriends.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(CreateQuizzChoose.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.findQuiz:
                intent = new Intent(CreateQuizzChoose.this, FindQuizz.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.evalMode:
                intent = new Intent(CreateQuizzChoose.this, EvalMode.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                intent = new Intent(CreateQuizzChoose.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            /*case R.id.settings:
                //TODO
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;*/
            default:
                //Unreachable statement
                break;
        }
        return true;
    }
}
