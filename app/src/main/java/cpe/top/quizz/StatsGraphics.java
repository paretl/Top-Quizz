package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import cpe.top.quizz.asyncTask.StatisticTask;
import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.User;

import static android.R.color.transparent;

public class StatsGraphics extends AppCompatActivity implements AsyncStatisticResponse, NavigationView.OnNavigationItemSelectedListener {

    private static final String USER = "USER";
    private static final String STATISTICS = "STATISTICS";
    private static final String LIST_QUIZZ = "LIST_QUIZZ";

    private User connectedUser;
    private List<Statistic> lStats;
    private Map<Integer, Quizz> lQuizz;
    private int refresh = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_graphics);
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
        if (intent != null && intent.getSerializableExtra(USER) != null && intent.getSerializableExtra(STATISTICS) != null && intent.getSerializableExtra(LIST_QUIZZ) != null) {
            this.connectedUser = (User) intent.getSerializableExtra(USER);
            this.lStats = (List<Statistic>) intent.getSerializableExtra(STATISTICS);

            // Add values to spinner
            this.addValuesSpinner();

            // Add GraphicalView
            this.addGraphicView();
        }
        else if (intent != null && intent.getSerializableExtra(USER) != null && intent.getSerializableExtra(LIST_QUIZZ) != null && intent.getSerializableExtra(STATISTICS) == null) {
            this.connectedUser = (User) intent.getSerializableExtra(USER);

            // Add values to spinner
            this.addValuesSpinner();

            // TextView : no stats found for the quiz
            this.addTextViewNoStats();
        }
    }

    /**
     * Private function used to add values to spinner (list of quiz of the user)
     */
    private void addValuesSpinner() {
        List<Quizz> lQ = (List<Quizz>) getIntent().getSerializableExtra(LIST_QUIZZ);
        lQuizz = new HashMap<>();

        TreeSet<String> lNameStats = new TreeSet<String>();

        int i=1;
        // Spinner wants List<String> and not of objects
        for (Quizz q : lQ) {
            lQuizz.put(i,q);
            lNameStats.add(i + " | " + q.getName());
            i++;
        }

        // Add values to spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(lNameStats));
        spinner.setAdapter(adapter);

        // Spinner on select an item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // The fist time - we don't need to reload
                if (refresh == 0) {
                    refresh++;
                }

                if (refresh != 0) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    String[] stSplit = selectedItem.split(" | ");
                    StatisticTask u = new StatisticTask(StatsGraphics.this);
                    u.execute(connectedUser.getPseudo(), Integer.toString(lQuizz.get(Integer.parseInt(stSplit[0])).getId()));
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}});
    }

    /**
     * Private function used to add the GraphicalView to the view
     */
    private void addGraphicView() {
        // Add GraphicalView
        LinearLayout chart = (LinearLayout) findViewById(R.id.chart);
        chart.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chart.setLayoutParams(params);
        GraphicalView chartView = openChart();
        chart.addView(chartView);
        chartView.setBackground(getResources().getDrawable(R.color.colorWhite));
        chartView.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
        chartView.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;

        this.adjustHeightComponents();
    }

    /**
     * Private function used to set properly the height of components because of non correctly work of AChartEngine with MATCH_PARENT / WRAP_CONTENT
     */
    private void adjustHeightComponents() {
        LinearLayout cSpinner = (LinearLayout) findViewById(R.id.containerSpinner);
        cSpinner.setMinimumHeight(160);
        cSpinner.getLayoutParams().height = 160;
        cSpinner.setGravity(Gravity.CENTER);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setMinimumHeight(160);
        spinner.getLayoutParams().height = 160;
        spinner.setGravity(Gravity.CENTER);

        LinearLayout legend = (LinearLayout) findViewById(R.id.legend);
        legend.setMinimumHeight(240);
        legend.getLayoutParams().height = 240;
    }

    /**
     * Private function used to add a TextView to the view (No Quiz)
     */
    private void addTextViewNoStats() {
        LinearLayout chart = (LinearLayout) findViewById(R.id.chart);
        chart.removeAllViews();
        TextView noStats = new TextView(this);
        noStats.setText("Aucuns statistiques pour ce quiz !");
        noStats.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        noStats.setTextSize(20);
        noStats.setGravity(Gravity.CENTER);
        chart.addView(noStats);
        noStats.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
        noStats.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
        chart.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        chart.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;

        // Not to display
        LinearLayout legend = (LinearLayout) findViewById(R.id.legend);
        legend.setMinimumHeight(0);
        legend.getLayoutParams().height = 0;

        LinearLayout cSpinner = (LinearLayout) findViewById(R.id.containerSpinner);
        cSpinner.setMinimumHeight(60);
        cSpinner.getLayoutParams().height = 60;
        cSpinner.setGravity(Gravity.CENTER);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setMinimumHeight(60);
        spinner.getLayoutParams().height = 60;
        spinner.setGravity(Gravity.CENTER);
    }

    private GraphicalView openChart(){
        int[] x = new int[lStats.size()];
        int[] scores = new int[lStats.size()];
        int[] nbQuestions = new int[lStats.size()];

        for (int i = 0; i < lStats.size(); i++) {
            x[i] = i;
            scores[i] = lStats.get(i).getNbRightAnswers();
            nbQuestions[i] = lStats.get(i).getNbQuestions();
        }

        // Creating an XYSeries for stats
        XYSeries nbQuestionsSeries = new XYSeries("NumberQuestions");
        XYSeries scoreSeries = new XYSeries("ScoreQuestions");
        XYSeries scoresSerieWithPointsColor = new XYSeries("ScoreQuestionsColorPoints");

        // Adding data to stats serie
        for (int i=0; i<x.length; i++) {
            scoreSeries.add(x[i], scores[i]);
            scoresSerieWithPointsColor.add(x[i], scores[i]);
            nbQuestionsSeries.add(x[i], nbQuestions[i]);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(nbQuestionsSeries);
        dataset.addSeries(scoreSeries);
        dataset.addSeries(scoresSerieWithPointsColor);

        XYSeriesRenderer scoresRenderer = new XYSeriesRenderer();
        scoresRenderer.setFillPoints(true);
        scoresRenderer.setFillBelowLine(true);
        scoresRenderer.setDisplayBoundingPoints(true);
        scoresRenderer.setPointStyle(PointStyle.CIRCLE);
        scoresRenderer.setPointStrokeWidth(50);
        scoresRenderer.setShowLegendItem(true);

        XYSeriesRenderer scoresRendererWithPointsColor = new XYSeriesRenderer();
        scoresRendererWithPointsColor.setDisplayBoundingPoints(true);
        scoresRendererWithPointsColor.setPointStyle(PointStyle.CIRCLE);
        scoresRendererWithPointsColor.setPointStrokeWidth(15);
        scoresRendererWithPointsColor.setShowLegendItem(true);
        scoresRendererWithPointsColor.setColor(Color.BLUE);

        XYSeriesRenderer nBQuestionsRenderer = new XYSeriesRenderer();
        nBQuestionsRenderer.setLineWidth(5);
        nBQuestionsRenderer.setColor(Color.DKGRAY);
        nBQuestionsRenderer.setFillPoints(true);
        nBQuestionsRenderer.setDisplayBoundingPoints(true);
        nBQuestionsRenderer.setPointStyle(PointStyle.CIRCLE);
        nBQuestionsRenderer.setPointStrokeWidth(20);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setMarginsColor(transparent);
        multiRenderer.setZoomRate(0.2f);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setYAxisMax(returnMaximumStatYValue(lStats));
        multiRenderer.setYAxisMin(0);
        multiRenderer.setShowLegend(false);
        multiRenderer.setShowGrid(true);
        multiRenderer.setGridColor(R.color.colorBlack, 0);
        multiRenderer.setLabelsColor(R.color.colorBlack);
        multiRenderer.setYAxisAlign(Paint.Align.LEFT, 0);
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setLabelsTextSize(50);
        // TO DO : CHANGE
        multiRenderer.setYLabelsPadding(-15);
        multiRenderer.setYLabelsVerticalPadding(-20);

        // Add series
        multiRenderer.addSeriesRenderer(nBQuestionsRenderer);
        multiRenderer.addSeriesRenderer(scoresRenderer);
        multiRenderer.addSeriesRenderer(scoresRendererWithPointsColor);

        return ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);
    }

    /**
     * Private function used to return the maximum of questions of the quiz for the graph
     * @param listStats list of Statistic
     * @return max the maximum of questions of the quiz
     */
    private int returnMaximumStatYValue(List<Statistic> listStats) {
        int max = -1;
        for (Statistic s : listStats) {
            if (max < s.getNbQuestions()) {
                max = s.getNbQuestions();
            }
        }
        return max;
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
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StatsGraphics.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.chat:
                intent = new Intent(StatsGraphics.this, Chat.class);
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
        switch (((ReturnObject) ((List<Object>) obj).get(1)).getCode()) {
            case ERROR_000:
                this.lStats = (List<Statistic>) ((List<ReturnObject>) obj).get(1).getObject();
                if (lStats != null) {
                    this.addGraphicView();
                } else {
                    this.addTextViewNoStats();
                }
                break;
            case ERROR_200:
                Toast.makeText(StatsGraphics.this, "Impossible d'acceder au serveur", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_100:
                this.addTextViewNoStats();
                break;
            default:
                Toast.makeText(StatsGraphics.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

}
