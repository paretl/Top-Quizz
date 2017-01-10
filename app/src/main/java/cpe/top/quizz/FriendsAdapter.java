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

import cpe.top.quizz.asyncTask.DeleteFriendTask;
import cpe.top.quizz.asyncTask.FriendsTask;
import cpe.top.quizz.asyncTask.ProfilTask;
import cpe.top.quizz.asyncTask.QuizzDeleteTask;
import cpe.top.quizz.asyncTask.QuizzTask;
import cpe.top.quizz.asyncTask.responses.AsyncFriendsResponse;
import cpe.top.quizz.asyncTask.responses.AsyncProfilResponse;
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
public class FriendsAdapter extends BaseAdapter implements AsyncFriendsResponse, AsyncProfilResponse {

    private static final String FRIENDS_DEL = "FRIENDS_DEL";

    private static final String USER_FRIEND = "USER_FRIEND";

    private final static String USER = "USER";

    private static final String LIST_FRIENDS = "LIST_FRIENDS";

    private List<User> aListF;

    private Context mContext;

    private LayoutInflater mInflater;

    private User connectedUser;

    private int index;

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
        index = 0;

        LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.friends_list_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView name = (TextView)layoutItem.findViewById(R.id.nameUser);
        TextView mail = (TextView)layoutItem.findViewById(R.id.mailUser);
        TextView del = (TextView)layoutItem.findViewById(R.id.delUserFriend);

        User f = (User) aListF.get(position);
        name.setText(f.getPseudo());
        mail.setText(f.getMail());

        addListenerToLayout(f, layoutItem);

        addListenerToDelTextView(f, del, position);

        return layoutItem;
    }

    /**
     * Private method used to add a listener on click to a specified layout
     * @param layout a layout
     */
    private void addListenerToLayout(final User f, LinearLayout layout) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilTask task = new ProfilTask(FriendsAdapter.this);
                task.execute(f.getPseudo());
            }
        });
    }

    private void addListenerToDelTextView(final User u, TextView del, final int position) {
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                DeleteFriendTask task = new DeleteFriendTask(FriendsAdapter.this);
                task.execute(connectedUser.getPseudo(), u.getPseudo());
            }
        });
    }

    @Override
    public void processFinish(Object obj) {
        if (((List<Object>) obj).get(0) != null && ((ReturnObject) ((List<Object>) obj).get(0)).getObject().equals(FRIENDS_DEL)) {
            switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
                case ERROR_000:
                    Intent myIntent = new Intent(mContext, FriendsDisplay.class);
                    this.aListF.remove(index);
                    myIntent.putExtra(LIST_FRIENDS, (ArrayList<User>) this.aListF);
                    myIntent.putExtra(USER, (User) this.connectedUser);
                    ((Activity) mContext).finish();
                    mContext.startActivity(myIntent);
                    break;
                case ERROR_200:
                    Toast.makeText(mContext, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
                case ERROR_000:
                    Intent myIntent = new Intent(mContext, Profil.class);
                    myIntent.putExtra(USER, (User) connectedUser);
                    User userFriend = (User) ((ReturnObject) ((List<Object>) obj).get(1)).getObject();
                    if (((ReturnObject) ((List<Object>) obj).get(2)).getObject() != null) {
                        userFriend.setQuizz((List<Quizz>) (((ReturnObject) ((List<Object>) obj).get(2)).getObject()));
                    }
                    myIntent.putExtra(USER_FRIEND, userFriend);
                    mContext.startActivity(myIntent);
                    break;
                case ERROR_200:
                    Toast.makeText(mContext, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_100:
                    Toast.makeText(mContext, "Ce profil n'existe pas...", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
