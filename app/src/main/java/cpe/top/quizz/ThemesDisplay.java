package cpe.top.quizz;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

import cpe.top.quizz.asyncTask.ThemeTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

/**
 * Created by Camille Cordier on 07/11/2016.
 *
 */

public class ThemesDisplay extends AppCompatActivity implements AsyncUserResponse{

    private static final String USER = "USER";

    private static final int BUTTTONHEIGHT= 120;

    private User connectedUser;

    private boolean recuperation = true;

    private Collection<Theme> themeCollection=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes_display);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) getIntent().getSerializableExtra(USER);
        }

        ThemeTask themeTask = new ThemeTask(ThemesDisplay.this);
        themeTask.execute(connectedUser.getPseudo());

        displayTheme();




    }

    @Override
    protected void onRestart() {
        super.onRestart();

        displayTheme();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                //Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ThemesDisplay.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void processFinish(Object obj) {
        //Object cannot be null
        switch (((ReturnObject) obj).getCode()){
            case ERROR_000:
                themeCollection = (Collection<Theme>) ((ReturnObject) obj).getObject();
                onRestart();
                break;
            case ERROR_100:
                break;
            case ERROR_200:
                Toast.makeText(ThemesDisplay.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_700:
            default:
                Toast.makeText(ThemesDisplay.this, "Erreur Inconnue", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void displayTheme(){
        LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(themeCollection  != null){
            int nbButton=themeCollection.size();
            int changeHorizontalLayout =0;
            LL.setLayoutParams(LLParams);
            LinearLayout layoutButtons = null;
            for(Theme tmp: themeCollection) {
                if (changeHorizontalLayout == 0) {
                    //Create Horizontal Layout for 2 theme buttons
                    layoutButtons = new LinearLayout(this);
                    layoutButtons.setOrientation(LinearLayout.HORIZONTAL);
                    layoutButtons.setBaselineAligned(false);

                    LinearLayout.LayoutParams layoutButtonsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutButtonsParams.weight = 1;
                    layoutButtons.setLayoutParams(layoutButtonsParams);

                    //create button
                    layoutButtons.addView(createButton(tmp.getName()));


                    nbButton -= 1;
                    changeHorizontalLayout++;
                } else {
                    layoutButtons.addView(createButton(tmp.getName()));
                    LL.addView(layoutButtons);
                    nbButton -= 1;
                    changeHorizontalLayout = 0;
                }
            }

            if (themeCollection.size() % 2 == 1) {
                //Create TexteView Hidden to fix parity problem
                TextView falseContentText = new TextView(this);
                LinearLayout.LayoutParams layoutText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutText.weight = 1;
                layoutText.height = BUTTTONHEIGHT;
                falseContentText.setLayoutParams(layoutText);
                layoutButtons.addView(falseContentText);
                LL.addView(layoutButtons);
            }

        }else{
            LinearLayout layoutButtons = null;
            layoutButtons = new LinearLayout(this);
            TextView noTheme=new TextView(getApplicationContext());
            noTheme.setText("Vous n'avez pas de thème...");
            if(!recuperation){
                noTheme.setText("Récupération de vos thèmes...");
                recuperation = false;
            }
            LinearLayout.LayoutParams layoutText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutText.height = BUTTTONHEIGHT;
            noTheme.setLayoutParams(layoutText);
            layoutButtons.addView(noTheme);


            LL.addView(layoutButtons);
        }

        ScrollView scroll = ((ScrollView) findViewById(R.id.scroll));
        scroll.removeAllViews();
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

