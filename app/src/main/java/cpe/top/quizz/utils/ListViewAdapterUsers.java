package cpe.top.quizz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import cpe.top.quizz.R;
import cpe.top.quizz.asyncTask.AddFriendTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 08/01/17.
 */

public class ListViewAdapterUsers extends BaseAdapter implements AsyncUserResponse {

    private Context mContext;

    private LayoutInflater inflater;

    private User connectedUser;

    private ArrayList<String> userList = new ArrayList<>();

    public ListViewAdapterUsers(Context context, ArrayList<String> list, User user) {
        mContext = context;
        this.userList = list;
        this.connectedUser = user;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public String getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        if (convertView == null) {
            layoutItem = (LinearLayout) inflater.inflate(R.layout.listview_users, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        TextView pseudo = (TextView)layoutItem.findViewById(R.id.pseudo);
        TextView add = (TextView)layoutItem.findViewById(R.id.add);

        String str = userList.get(position);
        pseudo.setText(str);

        addListenerToAddTextView(str, add);

        return layoutItem;
    }

    private void addListenerToAddTextView(final String pseudo, TextView add) {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendTask task = new AddFriendTask(ListViewAdapterUsers.this);
                task.execute(pseudo, connectedUser.getPseudo());
            }
        });
    }

    @Override
    public void processFinish(Object obj) {
        switch (((ReturnObject) obj).getCode()) {
            case ERROR_000:
                Toast.makeText(mContext, "Vous êtes désormais amis", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_300:
                Toast.makeText(mContext, "Vous êtes déjà amis", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_100:
                Toast.makeText(mContext, "L'ajout d'ami a échoué", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(mContext, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
