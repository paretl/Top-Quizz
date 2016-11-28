package cpe.top.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lparet on 22/11/16.
 */

public class CreateQuestion extends AppCompatActivity {

    private int nbReponses = 2;
    String rep1, rep2, rep3, rep4, explication, question;
    private MyAdapter myAdapter;
    public ArrayList myItems = new ArrayList();
    public Boolean oneChecked = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        final TextView questionLabel = (TextView) findViewById(R.id.questionLabel);

        // Question not on a single line
        questionLabel.setLines(3);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setItemsCanFocus(true);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        // Button less is invicible by default
        final Button responseLessButton = (Button) findViewById(R.id.responseLessButton);
        responseLessButton.setVisibility(View.INVISIBLE);

        // Button to add a response
        final Button responseMoreButton = (Button) findViewById(R.id.responseMoreButton);
        responseMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbReponses == 3) {
                    responseMoreButton.setVisibility(View.INVISIBLE);
                }
                nbReponses++;
                System.out.println("View added with ID : " + nbReponses);
                responseLessButton.setVisibility(View.VISIBLE);
                ListItem listItem = new ListItem();
                listItem.caption = "Réponse " + nbReponses;
                listItem.checked = false;
                myItems.add(listItem);
                myAdapter.notifyDataSetChanged();
                listView.setAdapter(myAdapter);
            }
        });

        // Button to valid question
        final Button validQuestion = (Button) findViewById(R.id.validQuestion);
        validQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(myAdapter)) {
                    Intent intent = new Intent(CreateQuestion.this, MainActivity.class);
                    startActivity(intent);
                }
                System.out.println("Form not valid");
            }
        });

        //Button to delete the last response
        responseLessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbReponses == 3) {
                    responseLessButton.setVisibility(View.INVISIBLE);
                }
                System.out.println("View deleted with ID : " + nbReponses);
                nbReponses--;
                if(myAdapter.isCheckedContent(nbReponses)) {
                    oneChecked = false;
                }
                myAdapter.notifyDataSetChanged();
                listView.setAdapter(myAdapter);
                myItems.remove(nbReponses);
                responseMoreButton.setVisibility(View.VISIBLE);
            }
        });
    }

    class ViewHolder {
        CheckBox checkBox;
        EditText caption;
    }

    class ListItem {
        Boolean checked;
        String caption;
    }


    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 1; i < 3; i++) {
                ListItem listItem = new ListItem();
                listItem.caption = "Réponse " + i;
                listItem.checked = false;
                myItems.add(listItem);
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return myItems.size();
        }

        public String getTextContent(int position) {
            final LinearLayout en = (LinearLayout) myAdapter.getView(position,null, null);
            final EditText ed = (EditText) en.getChildAt(1);
            String rep = (ed.getText()).toString();
            return rep;
        }

        public Boolean isCheckedContent(int position) {
            final LinearLayout en = (LinearLayout) myAdapter.getView(position,null, null);
            final CheckBox ed = (CheckBox) en.getChildAt(0);
            Boolean checked = ed.isChecked();
            return checked;
        }

        public Object getItem(int position) {
            return getView(position, null, null);
        }

        public long getItemId(int position) {
            return getView(position, null, null).getId();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.activity_list, null);
                holder.caption = (EditText) convertView.findViewById(R.id.editText);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Fill EditText with the value you have in data source
            holder.caption.setText(((ListItem) myItems.get(position)).caption);
            holder.caption.setId(position);

            holder.checkBox.setChecked(((ListItem) myItems.get(position)).checked);
            holder.checkBox.setId(position);

            //we need to update adapter once we finish with editing
            holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ((ListItem)myItems.get(position)).caption = Caption.getText().toString();
                    }
                }
            });
            //we need to update adapter once we finish with editing
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = v.getId();
                    final CheckBox Caption = (CheckBox) v;
                    System.out.println(oneChecked);
                    if(oneChecked && Caption.isChecked()) {
                        ((ListItem)myItems.get(position)).checked = false;
                        notifyDataSetChanged();
                        Toast.makeText(CreateQuestion.this, "Vous ne pouvez cocher qu'une seule réponse", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(Caption.isChecked()) {
                        ((ListItem) myItems.get(position)).checked = Caption.isChecked();
                        oneChecked = true;
                    } else {
                        ((ListItem) myItems.get(position)).checked = false;
                        oneChecked = false;
                    }
                }
            });
            return convertView;
        }
    }

    private Boolean isValid(MyAdapter myAdapter) {
        final TextView questionLabel = (TextView) findViewById(R.id.questionLabel);
        final TextView explicationLabel = (TextView) findViewById(R.id.explicationLabel);

        // Test question
        question = (questionLabel.getText()).toString();
        if ("".equals(question)) {
            Toast.makeText(CreateQuestion.this, "Veuillez rentrer une question", Toast.LENGTH_LONG).show();
            return false;
        }

        // Test explication
        explication = (explicationLabel.getText()).toString();
        if ("".equals(explication)) {
            Toast.makeText(CreateQuestion.this, "Veuillez rentrer une explication", Toast.LENGTH_LONG).show();
            return false;
        }

        // Rep 1
        rep1 = myAdapter.getTextContent(0);
        System.out.println("Réponse 1 : " + rep1);
        if ("".equals(rep1)) {
            Toast.makeText(CreateQuestion.this, "Réponse 1 non renseignée", Toast.LENGTH_LONG).show();
            return false;
        }

        // Rep 2
        rep2 = myAdapter.getTextContent(1);
        System.out.println("Réponse 2 : " + rep2);

        if ("".equals(rep2)) {
            Toast.makeText(CreateQuestion.this, "Réponse 2 non renseignée", Toast.LENGTH_LONG).show();
            return false;
        }

        // Rep 3 and checked
        Boolean checkBox3 = false;
        if (nbReponses >= 3) {
            checkBox3 = myAdapter.isCheckedContent(2);
            System.out.println("Checkbox 3 : " + checkBox3);
            rep3 = myAdapter.getTextContent(2);
            System.out.println("Réponse 3 : " + rep3);
            if ("".equals(rep3)) {
                Toast.makeText(CreateQuestion.this, "Réponse 3 non renseignée", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        // Rep 4 and checked
        Boolean checkBox4 = false;
        if (nbReponses == 4) {
            checkBox4 = myAdapter.isCheckedContent(3);
            System.out.println("Checkbox 4 : " + checkBox4);
            rep4 = myAdapter.getTextContent(3);
            System.out.println("Réponse 4 : " + rep4);
            if ("".equals(rep4)) {
                Toast.makeText(CreateQuestion.this, "Réponse 4 non renseignée", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        // checked
        Boolean checkBox1 = myAdapter.isCheckedContent(0);
        System.out.println("Checkbox 1 : " + checkBox1);

        Boolean checkBox2 = myAdapter.isCheckedContent(1);
        System.out.println("Checkbox 2 : " + checkBox2);

        if (!(checkBox1 || checkBox2 || checkBox3 || checkBox4)) {
            Toast.makeText(CreateQuestion.this, "Vous n'avez pas renseigné de bonne réponse", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}