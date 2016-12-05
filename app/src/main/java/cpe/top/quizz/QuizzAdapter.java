package cpe.top.quizz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.QuizzTask;
import cpe.top.quizz.asyncTask.responses.AsyncQuizzResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;

/**
 * @author Romain Chazottier
 * @version 0.1
 * @since 25/11/2016
 */
public class QuizzAdapter extends BaseAdapter implements AsyncQuizzResponse {
    private static final String QUIZZ = "QUIZZ";

    private List<Quizz> listQ;

    private Context mContext;

    private LayoutInflater mInflater;

    public QuizzAdapter(Context context, List<cpe.top.quizz.beans.Quizz> aListQ) {
        this.mContext = context;
        this.listQ = aListQ;
        this.mInflater = LayoutInflater.from(mContext);
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
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.quizz_list_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView name = (TextView)layoutItem.findViewById(R.id.name);
        TextView theme = (TextView)layoutItem.findViewById(R.id.theme);

        Quizz q = (Quizz) listQ.get(position);
        name.setText(q.getName());
        // Theme of quizz on view = 1st theme of the 1st question
        List<Question> lQ = new ArrayList<Question>(q.getQuestions());
        List<Theme> lT = new ArrayList<Theme>(lQ.get(0).getThemes());
        theme.setText(lT.get(0).getName());

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
                QuizzTask task = new QuizzTask(QuizzAdapter.this);
                task.execute(q.getName().toString());
            }
        });
    }

    @Override
    public void processFinish(Object obj) {
        switch (((ReturnObject) obj).getCode()){
            case ERROR_000:
                Intent myIntent = new Intent(mContext, StartQuizz.class);
                myIntent.putExtra(QUIZZ, (Quizz) ((ReturnObject) obj).getObject());
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
