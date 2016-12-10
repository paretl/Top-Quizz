package cpe.top.quizz.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cpe.top.quizz.ChooseTheme;
import cpe.top.quizz.CreateQuestion;
import cpe.top.quizz.Home;
import cpe.top.quizz.R;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

/**
 * Created by lparet on 29/11/16.
 */

public class ListViewAdapterThemes extends BaseAdapter {
    // Declare Variables

    final String THEME = "";

    private User connectedUser;
    private static final String USER = "";

    Context mContext;
    LayoutInflater inflater;

    private ArrayList<String> arraylist_tmp = new ArrayList<>();
    private ArrayList<String> arraylist = new ArrayList<>();
    ArrayList<Theme> myThemes = new ArrayList<>();
    private Map<Integer, String> themeMap;

    public ListViewAdapterThemes(Context context, Map<Integer, String> themeMap) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.themeMap = themeMap;
        for(Map.Entry<Integer,String> t : themeMap.entrySet()) {
            this.arraylist_tmp.add(t.getValue());
        }
        Set<String> mySet = new HashSet<>(arraylist_tmp);
        arraylist = new ArrayList<>(mySet);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public String getItem(int position) {
        return arraylist.get(position);
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
        holder.name.setText(arraylist.get(position));

        holder.name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateQuestion.class);

                Theme theme = new Theme(position, (holder.name.getText()).toString());
                myThemes.add(theme);
                //connectedUser = (User) intent.getSerializableExtra(USER);
                //System.out.println(connectedUser);
                connectedUser = new User("louis", "louis.paret@gmail.com", "test", null, null);
                intent.putExtra(USER, connectedUser.getPseudo());
                intent.putExtra(THEME, theme);

                mContext.startActivity(intent);
            }
        });

        return view;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        arraylist_tmp.clear();
        if (charText.length() == 0) {
            for(Map.Entry<Integer,String> t : themeMap.entrySet()) {
                this.arraylist_tmp.add(t.getValue());
            }
            Set<String> mySet = new HashSet<>(arraylist_tmp);
            arraylist = new ArrayList<>(mySet);
        } else {
            for(Map.Entry<Integer,String> wp : themeMap.entrySet()) {
                if (wp.getValue().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    arraylist_tmp.add(wp.getValue());
                }
            }
            Set<String> mySet = new HashSet<>(arraylist_tmp);
            arraylist = new ArrayList<>(mySet);
        }
        notifyDataSetChanged();
    }


}
