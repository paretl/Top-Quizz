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
import java.util.List;
import java.util.Locale;

import cpe.top.quizz.ChooseTheme;
import cpe.top.quizz.CreateQuestion;
import cpe.top.quizz.R;
import cpe.top.quizz.beans.Theme;

/**
 * Created by lparet on 29/11/16.
 */

public class ListViewAdapterThemes extends BaseAdapter {
    // Declare Variables

    final String THEME = "Theme";

    Context mContext;
    LayoutInflater inflater;
    private List<Theme> themeList = null;
    private ArrayList<Theme> arraylist;

    public ListViewAdapterThemes(Context context, List<Theme> themeList) {
        mContext = context;
        this.themeList = themeList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Theme>();
        this.arraylist.addAll(themeList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return themeList.size();
    }

    @Override
    public Theme getItem(int position) {
        return themeList.get(position);
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
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.resultView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(themeList.get(position).getName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateQuestion.class);
                intent.putExtra(THEME, holder.name.getText()).toString();
                mContext.startActivity(intent);
            }
        });

        return view;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        themeList.clear();
        if (charText.length() == 0) {
            themeList.addAll(arraylist);
        } else {
            for (Theme wp : arraylist) {
                // Ici on regarde si le theme CONTIENT le char, on peut dire qu'il regarde au DEBUT (1er carac) en mettant .startsWith(charText))
                if (wp.getName().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    themeList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
