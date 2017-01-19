package cpe.top.quizz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cpe.top.quizz.asyncTask.QuizzTask;
import cpe.top.quizz.asyncTask.responses.AsyncResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.User;

/**
 * Created by Camille on 18/01/2017.
 */

public class EvalQuizzListAdapter extends BaseAdapter {

    private List<Statistic> listS;

    private Context mContext;

    private LayoutInflater mInflater;

    private User connectedUser;

    public EvalQuizzListAdapter(Context context, List<cpe.top.quizz.beans.Statistic> aListQ, User connectedUser) {
        this.mContext = context;
        this.listS = aListQ;
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
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.evalquizz_list_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView userName = (TextView)layoutItem.findViewById(R.id.quizzName);
        TextView date = (TextView)layoutItem.findViewById(R.id.date);


        userName.setText(s.getPseudo());
        date.setText(s.getDate().toString());
        addListenerToLayout(s, layoutItem);

        return layoutItem;
    }

    private void addListenerToLayout(final Quizz q, LinearLayout layout) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Statistictask = new QuizzTask(EvalQuizzListAdapter.this);
                task.execute(q.getName().toString());
            }
        });
    }
}
