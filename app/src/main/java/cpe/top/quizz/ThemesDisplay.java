package cpe.top.quizz;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import java.util.List;

import cpe.top.quizz.asyncTask.ThemeTask;
import cpe.top.quizz.asyncTask.responses.AsyncUserResponse;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Theme;
import cpe.top.quizz.beans.User;

/**
 * Created by Camille Cordier on 07/11/2016.
 *
 */

public class ThemesDisplay extends AppCompatActivity implements AsyncUserResponse, NavigationView.OnNavigationItemSelectedListener{

    List<Button> listButton = new ArrayList<>();

    private static final String USER = "USER";
    private static final String THEME = "THEME";

    private static final int BUTTTONHEIGHT= 120;

    private User connectedUser;

    private boolean recuperation = true;

    private Collection<Theme> themeCollection=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes_display);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            connectedUser = (User) getIntent().getSerializableExtra(USER);
        }

        final ThemeTask themeTask = new ThemeTask(ThemesDisplay.this);
        themeTask.execute(connectedUser.getPseudo());

        displayTheme();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        displayTheme();

        for (final Button bt : listButton){
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ThemesDisplay.this, ShowQuestions.class);
                    for(Theme t: themeCollection){
                        //Sure of one element will be selected
                        if(t.getName().equals(bt.getText())){
                            intent.putExtra(THEME, t);
                        }
                    }

                    intent.putExtra(USER, connectedUser);
                    startActivity(intent);
                }
            });
        }
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
            case R.id.chat:
                intent = new Intent(ThemesDisplay.this, Chat.class);
                intent.putExtra(USER, connectedUser);
                startActivity(intent);
                finish();
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
                    layoutButtonsParams.bottomMargin = 10;
                    layoutButtonsParams.topMargin = 10;
                    layoutButtonsParams.leftMargin = 10;
                    layoutButtonsParams.rightMargin = 10;
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
        addButtonListener();
    }

    private Button createButton(String name) {
        Button myButton = new Button(this);
        LinearLayout.LayoutParams layoutButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutButton.weight = 1;
        layoutButton.height = BUTTTONHEIGHT;
        layoutButton.setMargins(5,5,5,5);
        myButton.setText(name);
        myButton.setTextSize(15);
        Resources res = this.getResources();
        myButton.setTextColor(res.getColor(R.color.colorPrimaryDark));
        myButton.setBackground(res.getDrawable(R.drawable.border));
        myButton.setLayoutParams(layoutButton);
        listButton.add(myButton);
        return myButton;
    }

    private void addButtonListener(){

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

