package com.example.inna.sleepy2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_layout);
        BarChart barChart1 = (BarChart) findViewById(R.id.barchart1);
        BarChart barChart2 = (BarChart) findViewById(R.id.barchart2);

        // create BarEntry for Bar Group 1
        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
        bargroup1.add(new BarEntry(1f, 0));
        bargroup1.add(new BarEntry(2f, 1));
        bargroup1.add(new BarEntry(5f, 2));
        bargroup1.add(new BarEntry(20f, 3));
        bargroup1.add(new BarEntry(15f, 4));
        bargroup1.add(new BarEntry(19f, 5));
        bargroup1.add(new BarEntry(4f, 6));
        bargroup1.add(new BarEntry(7f, 7));

        // create BarEntry for Bar Group 2
        ArrayList<BarEntry> bargroup2 = new ArrayList<>();
        bargroup2.add(new BarEntry(8f, 0));
        bargroup2.add(new BarEntry(2f, 1));
        bargroup2.add(new BarEntry(5f, 2));
        bargroup2.add(new BarEntry(20f, 3));
        bargroup2.add(new BarEntry(15f, 4));
        bargroup2.add(new BarEntry(19f, 5));
// creating dataset for Bar Group1
        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "Bar Group 1");
        BarDataSet barDataSet2 = new BarDataSet(bargroup2, "Bar Group 2");

//barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet2.setColor(Color.DKGRAY);


        ArrayList<String> labels1 = new ArrayList<>();
        labels1.add("22:00");
        labels1.add("23:00");
        labels1.add("24:00");
        labels1.add("01:00");
        labels1.add("02:00");
        labels1.add("03:00");
        labels1.add("04:00");
        labels1.add("05:00");
        labels1.add("06:00");

        ArrayList<String> labels2 = new ArrayList<>();
        labels2.add("2016");
        labels2.add("2015");
        labels2.add("2014");
        labels2.add("2013");
        labels2.add("2012");
        labels2.add("2011");

        ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets1.add(barDataSet1);
        ArrayList<IBarDataSet> dataSets2 = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets2.add(barDataSet2);

// initialize the Bardata with argument labels and dataSet
        BarData data1 = new BarData(labels1,dataSets1);
        barChart1.setData(data1);
        BarData data2 = new BarData(labels2,dataSets2);
        barChart2.setData(data2);
    }
}
