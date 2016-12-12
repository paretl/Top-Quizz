package cpe.top.quizz.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import cpe.top.quizz.CreateQuestion;
import cpe.top.quizz.R;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 29/11/16.
 */

public class ListViewAdapterThemes extends BaseAdapter {
    // Declare Variables

    private static final String THEME = "THEME";
    private static final String USER = "USER";
    Context mContext;
    LayoutInflater inflater;
    private User user = new User();
    private ArrayList<Theme> activeListThemesView = new ArrayList<Theme>();
    private ArrayList<Theme> themeListDatabase = new ArrayList<Theme>();
    private ArrayList<Theme> themeListChoose = new ArrayList<Theme>();

    public ListViewAdapterThemes(Context context, ArrayList<Theme> themeList, User connectedUser, ArrayList<Theme> themeListChoosed) {
        user = connectedUser;
        mContext = context;
        if(themeListChoosed!=null) {
            this.themeListChoose = themeListChoosed;
        }
        this.themeListDatabase = themeList;
        inflater = LayoutInflater.from(mContext);
        this.activeListThemesView.addAll(themeList);
    }

    @Override
    public int getCount() {
        return activeListThemesView.size();
    }

    @Override
    public Theme getItem(int position) {
        return activeListThemesView.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_themes, null);
            holder.name = (TextView) view.findViewById(R.id.resultView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(activeListThemesView.get(position).getName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateQuestion.class);
                intent.putExtra(USER, user);
                themeListChoose.add(activeListThemesView.get(position));
                intent.putExtra(THEME, themeListChoose);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        activeListThemesView.clear();
        if (charText.length() == 0) {
            activeListThemesView.addAll(themeListDatabase);
        } else {
            for (Theme wp : themeListDatabase) {
                // Ici on regarde si le theme CONTIENT le char, on peut dire qu'il regarde au DEBUT (1er carac) en mettant .startsWith(charText))
                if (wp.getName().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    activeListThemesView.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView name;
    }


}
