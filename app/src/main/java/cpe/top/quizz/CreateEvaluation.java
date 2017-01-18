package cpe.top.quizz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Calendar;
import java.util.List;

import cpe.top.quizz.asyncTask.CreateEvalTask;
import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 17/01/17.
 */

public class CreateEvaluation extends AppCompatActivity implements AsyncResponse {

    private static final String USER = "USER";
    private static final String QUIZZ = "QUIZZ";
    private static final String FRIENDS_TASK = "FRIENDS_TASK";

    private Bundle bundle;
    private User connectedUser = null;
    private Quizz myQuizz = null;
    private int h,m;
    private long t;
    private String myPseudo, targetPseudo, quizzName, quizzId, day, hour, timer;

    // All friends
    public ArrayList<User> myFriends = new ArrayList<>();

    // Friends choosed to sent eval
    public ArrayList<String> friendsChoosed = new ArrayList<>();

    private ListViewAdapterFriendsEval myAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_evaluation);

        // Get quizz and user
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            connectedUser = (User) bundle.getSerializable(USER);
            myQuizz = (Quizz) bundle.getSerializable(QUIZZ);
        }

        // test if user is null
        if (connectedUser == null) {
            Intent i = new Intent(CreateEvaluation.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        TextView tvQuizz = (TextView) findViewById(R.id.quizzName);
        tvQuizz.setText("Création d'évaluation : " + myQuizz.getName());

        // Button to valid question
        final Button validQuestion = (Button) findViewById(R.id.createEval);
        validQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isValid()) {
                myPseudo = connectedUser.getPseudo();
                // Friends to sent eval
                targetPseudo = "";
                for(String s : friendsChoosed) {
                    if("".equals(targetPseudo)) {
                        targetPseudo = s;
                    } else {
                        targetPseudo = targetPseudo + "|" + s;
                    }
                }

                // quizz infos
                quizzId = Integer.toString(myQuizz.getId());
                quizzName = myQuizz.getName();

                // Deadline
                Calendar deadLine = Calendar.getInstance();
                deadLine.add(Calendar.DATE, Integer.parseInt(day));
                deadLine.set(Calendar.HOUR_OF_DAY, h);
                deadLine.set(Calendar.MINUTE, m);
                t = deadLine.getTimeInMillis()/1000;

                // Message confirmation
                int month = deadLine.get(Calendar.MONTH) + 1;
                String message = "Veux-tu vraiment évaluer " + targetPseudo.replace("|", ", ") + " au quizz " + quizzName + " avant le " + deadLine.get(Calendar.DATE)+ "/" + month + " à " + hour + " ?";

                // alertDialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvaluation.this);
                builder.setMessage(message);
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateEvalTask createEval = new CreateEvalTask(CreateEvaluation.this);
                        createEval.execute(myPseudo, targetPseudo, quizzId, quizzName, Long.toString(t), timer);
                    }
                })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            }
        });

        // task to get friends
        FriendsTask friends = new FriendsTask(CreateEvaluation.this);
        friends.execute(connectedUser.getPseudo());
    }

    private Boolean isValid() {

        // Test day is not empty
        day = (((TextView) findViewById(R.id.DeadlineDay)).getText()).toString();
        if ("".equals(day)) {
            Toast.makeText(CreateEvaluation.this, "Vous n'avez pas choisi de délais", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test hour is not empty
        hour = (((TextView) findViewById(R.id.deadlineHour)).getText()).toString();
        if ("".equals(hour)) {
            Toast.makeText(CreateEvaluation.this, "Vous n'avez pas choisi d'heure de deadline", Toast.LENGTH_LONG).show();
            return false;
        }

        // test hour format
        if(hour.length() != 5 && hour.length() != 4) {
            Toast.makeText(CreateEvaluation.this, "Mauvais format de l'heure (Ex: 12:30)", Toast.LENGTH_LONG).show();
            return false;
        } else {
            String[] hourTab = hour.split(":");
            h = Integer.parseInt(hourTab[0]);
            m = Integer.parseInt(hourTab[1]);
            if(h > 23 || m > 59) {
                Toast.makeText(CreateEvaluation.this, "Cette horaire n'existe pas", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        // Test timer is not empty
        timer = (((TextView) findViewById(R.id.timer)).getText()).toString();
        if ("".equals(timer)) {
            Toast.makeText(CreateEvaluation.this, "Vous n'avez pas choisi de timer", Toast.LENGTH_LONG).show();
            return false;
        }

        // test if friends are choosed
        if(friendsChoosed.size()==0) {
            Toast.makeText(CreateEvaluation.this, "Vous n'avez pas choisi d'amis à évaluer", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void processFinish(Object obj) {
        try {
            if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(FRIENDS_TASK)) {
                ArrayList<User> resultsList = new ArrayList<>();
                if (obj != null && ((ReturnObject) ((List<Object>) obj).get(1)).getObject() != null) {
                    resultsList = (ArrayList<User>) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                }
                // Initialise listView
                final ListView listView = (ListView) findViewById(R.id.friendsListView);
                listView.setItemsCanFocus(true);
                myAdapter = new ListViewAdapterFriendsEval(resultsList);
                listView.setAdapter(myAdapter);
            }
        } catch (ClassCastException e) {
            switch (((ReturnObject) obj).getCode()) {
                case ERROR_000:
                    Toast.makeText(CreateEvaluation.this, "Evaluation envoyée", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(CreateEvaluation.this, Home.class);
                    i.putExtra(USER, connectedUser);
                    startActivity(i);
                    finish();
                    break;
                case ERROR_200:
                    Toast.makeText(CreateEvaluation.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_100:
                    Toast.makeText(CreateEvaluation.this, "Impossible de créer l'évéluation", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(CreateEvaluation.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(CreateEvaluation.this, Home.class);
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView caption;
    }

    public class ListViewAdapterFriendsEval extends BaseAdapter {
        private LayoutInflater mInflater;
        public ListViewAdapterFriendsEval(ArrayList<User> friendList) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myFriends = friendList;
            notifyDataSetChanged();
        }

        public int getCount() {
            return myFriends.size();
        }

        public Object getItem(int position) {
            return getView(position, null, null);
        }

        public long getItemId(int position) {
            return getView(position, null, null).getId();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final CreateEvaluation.ViewHolder holder;
            if (convertView == null) {
                holder = new CreateEvaluation.ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_friends_eval, null);
                holder.caption = (TextView) convertView.findViewById(R.id.friendName);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (CreateEvaluation.ViewHolder) convertView.getTag();
            }
            holder.caption.setText((myFriends.get(position)).getPseudo());
            holder.caption.setId(position);
            holder.checkBox.setId(position);

            // we need to update adapter once we check a box
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CheckBox Caption = (CheckBox) v;
                    if (Caption.isChecked()) {
                        friendsChoosed.add((myFriends.get(v.getId())).getPseudo());
                    } else {
                        friendsChoosed.remove((myFriends.get(v.getId())).getPseudo());
                    }
                }
            });
            return convertView;
        }
    }
}
