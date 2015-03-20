package com.electrocardio.michaelsun.electrocardio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class HeartRateActivity extends Activity {

    Button getHeartRateButton;
    GraphView heartRateGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_heart_rate);
        initializeUI();
    }

    /** Method for initializing the UI.*/
    public void initializeUI(){

        getHeartRateButton = (Button)findViewById(R.id.getHeartRate);
        heartRateGraph = (GraphView)findViewById(R.id.heartRateGraph);
    }

    /** Method for getting the Heart Rate.*/
    public void getHeartRate(View v){

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        heartRateGraph.addSeries(series);
    }

    /** Method for clearing the graph.*/
    public void clearGraph(View v){

        heartRateGraph.removeAllSeries();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
