package cpe.top.quizz.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.Home;
import cpe.top.quizz.R;
import cpe.top.quizz.StartQuizz;
import cpe.top.quizz.asyncTask.ShareQuizzTask;
import cpe.top.quizz.asyncTask.responses.AsyncQuestionResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 03/01/17.
 */

public class ListViewAdapterQuizz extends BaseAdapter implements AsyncQuestionResponse {
    private static final String QUIZZ = "QUIZZ";
    private final static String USER = "USER";

    private List<Quizz> listQ;
    private User connectedUser;

    private Context mContext;

    private LayoutInflater mInflater;

    public ListViewAdapterQuizz(Context context, List<cpe.top.quizz.beans.Quizz> aListQ, User connectedUser) {
        this.mContext = context;
        this.listQ = aListQ;
        this.mInflater = LayoutInflater.from(mContext);
        this.connectedUser = connectedUser;
    }

    public int getCount() {
        return listQ.size();
    }

    public Object getItem(int position) {
        return listQ.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.listview_quizz, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView name = (TextView) layoutItem.findViewById(R.id.quizz);
        TextView pseudo = (TextView) layoutItem.findViewById(R.id.pseudo);

        Quizz q = (Quizz) listQ.get(position);
        name.setText(q.getName());
        pseudo.setText((((ArrayList<Question>) q.getQuestions()).get(0)).getPseudo());

        addListenerToLayout(q, layoutItem);

        return layoutItem;
    }

    /**
     * Private method used to add a listener on click to a specified layout
     * @param layout a layout
     */
    private void addListenerToLayout(final Quizz q, LinearLayout layout) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton("Jouer le Quizz", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(mContext, StartQuizz.class);
                                intent.putExtra(USER, connectedUser);
                                intent.putExtra(QUIZZ, q);
                                mContext.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Ajouter ce quizz à ma liste", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final ShareQuizzTask addQuizzInMyList = new ShareQuizzTask(ListViewAdapterQuizz.this);
                                addQuizzInMyList.execute(connectedUser.getPseudo(), Integer.toString(q.getId()));
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void processFinish(Object obj) {
        Intent intent = new Intent(mContext, Home.class);
        intent.putExtra(USER, connectedUser);
        mContext.startActivity(intent);
    }
}
