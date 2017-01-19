package cpe.top.quizz.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import cpe.top.quizz.ChooseTheme;
import cpe.top.quizz.CreateQuestion;
import cpe.top.quizz.CreateQuizz;
import cpe.top.quizz.Home;
import cpe.top.quizz.R;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 29/11/16.
 */

public class ListViewAdapterThemes extends BaseAdapter {
    private static final String THEME = "THEME";
    private static final String STATE = "STATE";
    private static final String NEW_THEME = "NEW_THEME";

    Context mContext;
    LayoutInflater inflater;

    // Different lists needed of themes
    // 1. themes viewed on the listView
    private ArrayList<Theme> activeListThemesView = new ArrayList<Theme>();
    // 2. themes on the database (all themes)
    private ArrayList<Theme> themeListDatabase = new ArrayList<Theme>();
    // 3. themes already choosed (multi-themes)
    private ArrayList<Theme> themeListChoosed = new ArrayList<Theme>();
    // variable to know if we are in CreateQuestion or CreateQuizz
    private String state;
    private Bundle bundle;

    public ListViewAdapterThemes(Context context, ArrayList<Theme> themeList, Intent intent) {
        bundle = intent.getExtras();
        mContext = context;
        // to prevent if it's the first time we arrive on this activity (so themeListChoosed will be null)
        if(bundle.getSerializable(THEME)!=null) {
            themeListChoosed = (ArrayList<Theme>) bundle.getSerializable(THEME);
        }
        state = bundle.getString(STATE);
        this.themeListDatabase = themeList;
        this.activeListThemesView.addAll(themeList);
        inflater = LayoutInflater.from(mContext);
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

        // On click on a theme, we send it at the next activity : CreateQuestion
        holder.name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                if("Question".equals(state)) {
                    intent = new Intent(mContext, CreateQuestion.class);
                } else if ("Quizz".equals(state)) {
                    intent = new Intent(mContext, CreateQuizz.class);
                } else {
                    // We can use that for an other activity in the future
                    intent = new Intent(mContext, ChooseTheme.class);
                }
                intent.putExtras(bundle);
                themeListChoosed.add(activeListThemesView.get(position));
                intent.putExtra(THEME, themeListChoosed);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter class to use research bar
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        activeListThemesView.clear();
        if (charText.length() == 0) {
            activeListThemesView.addAll(themeListDatabase);
        } else {
            for (Theme wp : themeListDatabase) {
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