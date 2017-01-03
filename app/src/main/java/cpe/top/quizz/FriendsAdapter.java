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

import java.util.ArrayList;
import java.util.List;

import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.QuizzDeleteTask;
import cpe.top.quizz.asyncTask.QuizzTask;
import cpe.top.quizz.asyncTask.responses.AsyncFriendsResponse;
import cpe.top.quizz.asyncTask.responses.AsyncQuizzResponse;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

import static cpe.top.quizz.R.attr.theme;

/**
 * @author Maxence Royer
 * @version 1.0
 * @since 03/01/2016
 */
public class FriendsAdapter extends BaseAdapter implements AsyncFriendsResponse {

    private final static String USER = "USER";

    private List<User> aListF;

    private Context mContext;

    private LayoutInflater mInflater;

    private User connectedUser;

    public FriendsAdapter(Context context, List<cpe.top.quizz.beans.User> aListF, User connectedUser) {
        this.mContext = context;
        this.aListF = aListF;
        this.mInflater = LayoutInflater.from(mContext);
        this.connectedUser = connectedUser;
    }

    public int getCount() {
        return aListF.size();
    }

    public Object getItem(int position) {
        return aListF.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.friends_list_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView name = (TextView)layoutItem.findViewById(R.id.nameUser);
        TextView mail = (TextView)layoutItem.findViewById(R.id.mailUser);

        User f = (User) aListF.get(position);
        name.setText(f.getPseudo());
        mail.setText(f.getMail());

        return layoutItem;
    }

    @Override
    public void processFinish(Object obj) {
        // TO IMPLEMENT - I'm waiting for Camille
    }
}
