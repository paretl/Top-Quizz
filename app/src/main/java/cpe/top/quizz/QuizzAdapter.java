package cpe.top.quizz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Romain on 25/11/2016.
 */

public class QuizzAdapter extends BaseAdapter {

    // Une liste de quizz
    private List<Quizz> listQ;

    //Le contexte dans lequel est présent notre adapter
    private Context mContext;

    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater mInflater;

    public QuizzAdapter(Context context, List<Quizz> aListQ) {
        mContext = context;
        listQ = aListQ;
        mInflater = LayoutInflater.from(mContext);
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
        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "personne_layout.xml"
            layoutItem = (LinearLayout) mInflater.inflate(R.layout.quizz_list_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView name = (TextView)layoutItem.findViewById(R.id.name);
        TextView theme = (TextView)layoutItem.findViewById(R.id.theme);

        //(3) : Renseignement des valeurs
        name.setText(listQ.get(position).name);
        theme.setText(listQ.get(position).theme);

        //On retourne l'item créé.
        return layoutItem;
    }
}
