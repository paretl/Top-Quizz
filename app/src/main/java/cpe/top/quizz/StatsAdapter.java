package cpe.top.quizz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cpe.top.quizz.asyncTask.StatisticTask;
import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

import static cpe.top.quizz.beans.ReturnCode.ERROR_000;
import static cpe.top.quizz.beans.ReturnCode.ERROR_100;
import static cpe.top.quizz.beans.ReturnCode.ERROR_200;


/**
 * Created by Camille on 29/12/2016.
 */


public class StatsAdapter extends BaseAdapter implements AsyncStatisticResponse {
    private static final String QUIZZ = "QUIZZ";

    private static final String LIST_QUIZZ = "LIST_QUIZZ";

    private static final String STATISTICS = "STATISTICS";

    private static final String QUIZZ_DELETE_TASK = "QuizzDeleteTask";

    private final static String USER = "USER";

    private List<Statistic> lStats;

    private Context mContext;

    private LayoutInflater mInflater;

    private User connectedUser;

    public StatsAdapter(Context context, List<Statistic> aListStat, User connectedUser) {
        this.mContext = context;
        this.lStats = aListStat;
        this.mInflater = LayoutInflater.from(mContext);
        this.connectedUser = connectedUser;
    }

    public int getCount() {
        return lStats.size();
    }

    public Object getItem(int position) {
        return lStats.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.quizz_list_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView result = (TextView)layoutItem.findViewById(R.id.name);
        TextView dateTime = (TextView)layoutItem.findViewById(R.id.theme);

        Statistic s = (Statistic) lStats.get(position);
        result.setText(s.getNbRightAnswers());
        SimpleDateFormat formatDateJour = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        String dateFormatee = formatDateJour.format(s.getDate());
        dateTime.setText(dateFormatee);

        //addListenerToLayout(s, layoutItem);   no need because it is just display not button

        return layoutItem;
    }

    /**
     * Private method used to add a listener on click to a specified layout
     *
     */
    /*private void addListenerToLayout(final Statistic q, LinearLayout layout) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticTask task = new StatisticTask(StatsAdapter.this);
                task.execute(q.getName().toString());
            }
        });
    }*/

/*    private void addListenerToDelTextView(final Quizz q, TextView del) {
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizzDeleteTask task = new QuizzDeleteTask(QuizzAdapter.this);
                task.execute(String.valueOf(q.getId()), connectedUser.getPseudo());
            }
        });
    }*/

    @Override
    public void processFinish(Object obj) {
        try {
            if (((List<Object>) obj).get(1) != null) { // Case of QuizzDeleteTask
                switch (((ReturnObject) ((List<Object>) obj).get(0)).getCode()) {
                    case ERROR_000:
                        Intent myIntent = new Intent(mContext, Home.class);
                        List<Statistic> quizzes = (List<Statistic>) ((List<ReturnObject>) obj).get(2).getObject();
                        myIntent.putExtra(LIST_QUIZZ, (ArrayList<Statistic>) quizzes);
                        myIntent.putExtra(USER, (User) connectedUser);
                        ((Activity) mContext).finish();
                        mContext.startActivity(myIntent);
                        Toast.makeText(mContext, "Suppression du quiz", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR_200:
                        Toast.makeText(mContext, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } catch (ClassCastException e) {
            switch (((ReturnObject) obj).getCode()) {
                case ERROR_000:
                    Intent myIntent = new Intent(mContext, StartQuizz.class);
                    myIntent.putExtra(STATISTICS, (Statistic) ((ReturnObject) obj).getObject());
                    myIntent.putExtra(USER, (User) connectedUser);
                    mContext.startActivity(myIntent);
                    break;
                case ERROR_200:
                    Toast.makeText(mContext, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_100:
                default:
                    Toast.makeText(mContext, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
