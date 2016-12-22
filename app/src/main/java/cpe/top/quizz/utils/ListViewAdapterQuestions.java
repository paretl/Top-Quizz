package cpe.top.quizz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import cpe.top.quizz.R;
import cpe.top.quizz.ShowQuestions;
import cpe.top.quizz.beans.Question;
import cpe.top.quizz.beans.User;

/**
 * Created by romai on 21/12/2016.
 */

public class ListViewAdapterQuestions extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<Question> questionsList= new ArrayList<Question>();

    public ListViewAdapterQuestions(Context context, ArrayList<Question> questionsDatabase) {
        mContext = context;
        this.questionsList = questionsDatabase;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return this.questionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.questionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListViewAdapterQuestions.ViewHolder holder;

        if (convertView == null) {
            holder = new ListViewAdapterQuestions.ViewHolder();
            convertView = inflater.inflate(R.layout.listview_themes, null);
            holder.name = (TextView) convertView.findViewById(R.id.resultView);
            convertView.setTag(holder);
        } else {
            holder = (ListViewAdapterQuestions.ViewHolder) convertView.getTag();
        }
        holder.name.setText(questionsList.get(position).getLabel());

        return convertView;
    }

    public class ViewHolder {
        TextView name;
    }

}
