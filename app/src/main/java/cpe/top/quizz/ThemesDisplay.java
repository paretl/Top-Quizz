package cpe.top.quizz;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Camille Cordier on 07/11/2016.
 *
 * RAF : la boucle qui crée les bouttons automatiquement
 * que faire lorsqu'il y a trop de theme, barre defilante ....
 *
 */

public class ThemesDisplay extends AppCompatActivity {

    private static final int BUTTTONHEIGHT= 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes_display);

        LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int nbButton=18;
        LL.setLayoutParams(LLParams);

        while(nbButton>0) {
            //Create Horizontal Layout for 2 theme buttons
            LinearLayout layoutButtons = new LinearLayout(this);
            layoutButtons.setOrientation(LinearLayout.HORIZONTAL);
            layoutButtons.setBaselineAligned(false);

            LinearLayout.LayoutParams layoutButtonsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutButtonsParams.weight = 1;
            layoutButtons.setLayoutParams(layoutButtonsParams);

            //create the first button
            layoutButtons.addView(createButton("To Do"));
            if(nbButton-1>0){
                //create second button if necessary
                layoutButtons.addView(createButton("To Do"));
            }else{
                //Create TexteView Hidden to fix parity problem
                TextView falseContentText=new TextView(getApplicationContext());
                LinearLayout.LayoutParams layoutText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutText.weight = 1;
                layoutText.height = BUTTTONHEIGHT;
                falseContentText.setLayoutParams(layoutText);
                layoutButtons.addView(falseContentText);
            }

            nbButton-=2;

            LL.addView(layoutButtons);
        }

        ScrollView scroll = ((ScrollView) findViewById(R.id.scroll));

        scroll.addView(LL);
    }


    private Button createButton(String name) {
        Button myButton = new Button(this);
        LinearLayout.LayoutParams layoutButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutButton.weight = 1;
        layoutButton.height = BUTTTONHEIGHT;
        layoutButton.setMargins(5,5,5,5);
        myButton.setText(name);
        Resources res = this.getResources();
        myButton.setBackground(res.getDrawable(R.drawable.border));
        myButton.setLayoutParams(layoutButton);
        return myButton;
    }

}

