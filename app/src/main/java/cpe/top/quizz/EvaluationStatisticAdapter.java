package cpe.top.quizz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import cpe.top.quizz.asyncTask.StatisticTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.User;

/**
 * Created by Camille on 18/01/2017.
 * Adapter to format score display by user playing the evalQuizz
 */

public class EvaluationStatisticAdapter extends BaseAdapter {

    private List<Statistic> listS;

    private Context mContext;

    private LayoutInflater mInflater;

    private User connectedUser;

    public EvaluationStatisticAdapter(Context context, List<Statistic> aListS, User connectedUser) {
        this.mContext = context;
        this.listS = aListS;
        this.mInflater = LayoutInflater.from(mContext);
        this.connectedUser = connectedUser;
    }

    @Override
    public int getCount() {
        return listS.size();
    }

    @Override
    public Object getItem(int position) {
        return listS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;
        Statistic s = (Statistic) listS.get(position);

        if (convertView == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.quizz_score_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView userName = (TextView) layoutItem.findViewById(R.id.userName);
        TextView score = (TextView) layoutItem.findViewById(R.id.score);
        TextView date = (TextView) layoutItem.findViewById(R.id.date);

        userName.setText(s.getPseudo());
        if(s.getNbRightAnswers() != null) {
            score.setText(s.getNbRightAnswers().toString() + "/" + s.getNbQuestions());
        } else {
            score.setText("Pas réalisée");
        }

        if(s.getDate() != null) {
            Date myDate = s.getDate();
            int month = myDate.getMonth()+1;
            date.setText("Réalisée le : " + myDate.getDate() + "/" + month);
        } else {
            date.setText("Evaluation non réalisée");
        }

        return layoutItem;
    }
}