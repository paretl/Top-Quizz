package cpe.top.quizz;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import cpe.top.quizz.beans.User;

import static android.R.color.transparent;

public class StatsGraphics extends AppCompatActivity {

    private static final String USER = "USER";

    private User connectedUser;

    private String[] mMonth = new String[] {
            "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
            "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_graphics);
        /**Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);*/

        // Add values to spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] arraySpinner = new String[] {
                "Quizz 1", "Quizz 2", "Quizz 3", "Quizz 4", "Quizz 5"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinner.setAdapter(adapter);

        // Add GraphicalView
        LinearLayout chartView = (LinearLayout) findViewById(R.id.chart);
        GraphicalView resultView = openChart();
        resultView.setBackgroundColor(Color.LTGRAY);
        LinearLayout.LayoutParams lP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        resultView.setLayoutParams(lP);
        chartView.addView(resultView);
    }

    private GraphicalView openChart(){
        int[] x = { 1,2,3,4,5,6,7,8 };
        int[] income = { 4,5,4,6,7,8,7,9 };

        // Creating an  XYSeries for Income
        XYSeries incomeSeries = new XYSeries("Income");

        // Adding data to Income and Expense Series
        for(int i=0;i<x.length;i++){
            incomeSeries.add(x[i], income[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setPointStyle(PointStyle.CIRCLE);
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(false);
        incomeRenderer.setFillBelowLine(true);
        incomeRenderer.setShowLegendItem(false);
        incomeRenderer.setColor(R.color.colorPrimary);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomRate(0.2f);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setShowGrid(false);
        multiRenderer.setShowLegend(false);
        multiRenderer.setShowAxes(false);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYAxisMax(10);
        multiRenderer.setAxesColor(Color.BLACK);
        multiRenderer.setLabelsTextSize(new Float(50));
        multiRenderer.setMarginsColor(Color.LTGRAY);
        multiRenderer.setBackgroundColor(Color.LTGRAY);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setYLabelsVerticalPadding(-40);

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(incomeRenderer);

        // Creating an intent to plot line chart using dataset and multipleRenderer
        return ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);
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
            default:
                break;
        }
        return true;
    }
}
