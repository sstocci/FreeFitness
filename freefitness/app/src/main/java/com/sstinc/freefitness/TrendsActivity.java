package com.sstinc.freefitness;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




public class TrendsActivity extends AppCompatActivity {

    private BottomNavigationView navigationMenu;
    private Spinner exerciseSpinner;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    boolean hasDraw=false;
    TextView firstDayTextView;
    TextView lastDayTextView;
    TextView totalDaysTextview;
    TextView maxWeightTextView;
    TextView leastWeightTextView;
    TextView weeklyAverageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);

        exerciseSpinner = (Spinner)findViewById(R.id.exerciseSpinner);
        firstDayTextView = (TextView) findViewById(R.id.FirstDayTextView);
        lastDayTextView = (TextView)findViewById(R.id.LastDayTextView);
        totalDaysTextview = (TextView) findViewById(R.id.TimesDoneTextView);
        maxWeightTextView = (TextView)findViewById(R.id.MaxWeightTextView);
        leastWeightTextView = (TextView)findViewById(R.id.LowestWeightTextView);
        weeklyAverageTextView = (TextView)findViewById(R.id.AverageTextView);

        // Gets access to users database stored on phone
        DatabaseHelper dbHandler = new DatabaseHelper(getApplicationContext(), null, null, 1);
        //Returns an ArrayList where each element is a list containing user Information such as reps, exercise,...etc

        List exerciseData = null;
        try {
            ArrayList<List> result = dbHandler.loadData();
            List userIdData = result.get(0);
            List dateData = result.get(1);
            exerciseData = result.get(2);
            List setNumberData = result.get(3);
            final List weightData = result.get(4);
            List repData = result.get(5);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
        }

//        Log.d("Exercise Data Size",Integer.toString(exerciseData.size()));
//        Log.d("Exercise Data empty?",Boolean.toString(exerciseData.isEmpty()));
        if(exerciseData!=null){
            List uniqueValues = getUniqueValues(exerciseData);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,uniqueValues);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            exerciseSpinner.setAdapter(adapter);
        }
        else{
            // If the user has no data enter, display an alert box
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("You have no data entered. Would you like to go enter some?");
            builder1.setCancelable(true);builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(TrendsActivity.this,LogWorkout.class));
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
        // Creates Navigation Menu at the bottom of the screen
        // Allows users to change Activities by press a button on the menu
        navigationMenu = findViewById(R.id.navigationMenu);
        navigationMenu.getMenu().getItem(2).setChecked(true);
        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()){
                    case R.id.homePage:
                        startActivity(new Intent(TrendsActivity.this,HomeActivity.class));
                        break;
                    case R.id.logWorkoutPage:
                        startActivity(new Intent(TrendsActivity.this,LogWorkout.class));
                        break;
                    case R.id.BMIPage:
                        startActivity(new Intent(TrendsActivity.this,BMI.class));
                        break;
//                    case R.id.calendarPage:
//                        startActivity(new Intent(TrendsActivity.this,CalendarActivity.class));
//                        break;
                }
                return false;
            }
        });
        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = exerciseSpinner.getSelectedItem().toString();

                if (selected.length() > 1) {

                    // passes exercise name to database to get new query
                    DatabaseHelper dbHandler = new DatabaseHelper(getApplicationContext(), null, null, 1);
                    ArrayList<List> trendData = dbHandler.getQueryData(selected);
                    List dates = trendData.get(0);
                    List maxWeights = trendData.get(1);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

                    GraphView graph = (GraphView) findViewById(R.id.graph);

                    graph.removeAllSeries();
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();


                    firstDayTextView.setText(getFirstDay(dates));
                    lastDayTextView.setText(getLastDay(dates));
                    totalDaysTextview.setText(getTotals(dates));
                    maxWeightTextView.setText(Double.toString(getMax(maxWeights)));
                    leastWeightTextView.setText(Double.toString(getLeast(maxWeights)));
                    weeklyAverageTextView.setText(getFrequency(dates));


                    for (int i = 0; i < maxWeights.size(); i++) {
                        try {
                            Date date = formatter.parse(dates.get(i).toString());
                            Log.d("dates", date.toString());
                            Log.d("exercise", selected);
                            series.appendData(new DataPoint(i, Double.parseDouble(maxWeights.get(i).toString())), false, 100);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    double maxVal = getMax(maxWeights);
                    Log.d("weights ", "maxval: " + String.valueOf(maxVal));
                    series.setDrawDataPoints(true);
                    graph.setTitle("Max Weight For: " + selected);
                    graph.getViewport().setScrollableY(true);
                    graph.getViewport().setMinY(100.0);
                    graph.getViewport().setMaxY(maxVal + 10);
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.addSeries(series);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(maxWeights.size());
                    maxWeights.clear();
                    dates.clear();
                    hasDraw = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { //Mandatory override for spinner class.
            }});
    }

    public static List<String> getUniqueValues(List list){
        // used for drop down list
        // have to remove duplicates or else it will appear weird
        List<String> unique_values = new ArrayList<String>();
        for (int i=0;i<list.size();i++){
            if (!unique_values.contains(list.get(i).toString())) {
                unique_values.add(list.get(i).toString());
            }
        }
        return unique_values;
    }

    public static double getMax(List weights){
        if (weights.size()==0){
            return 0.0;
        }
        else if (weights.size()==1){
            return Double.parseDouble(weights.get(0).toString());
        }
        else{
            double maxVal = Double.parseDouble(weights.get(0).toString());
            for (int i =1; i<weights.size();i++){
                if (Double.parseDouble(weights.get(i).toString())>maxVal){
                    maxVal=Double.parseDouble(weights.get(i).toString());
                }
            }
            return maxVal;
        }
    }
    public static double getLeast(List weights){
        if (weights.size()==0){
            return 0.0;
        }
        else if (weights.size()==1){
            return Double.parseDouble(weights.get(0).toString());
        }
        else{
            double leastVal = Double.parseDouble(weights.get(0).toString());
            for (int i =1; i<weights.size();i++){
                if (Double.parseDouble(weights.get(i).toString())<leastVal){
                    leastVal=Double.parseDouble(weights.get(i).toString());
                }
            }
            return leastVal;
        }
    }
    public static String getFirstDay(List dates){
        // since its in ascending order it will just be the first element
        return dates.get(0).toString();
    }

    public static String getLastDay(List dates){
        // since its in ascending order it will just be the last element
        return dates.get(dates.size()-1).toString();
    }

    public static String getFrequency(List dates){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {

            Date lastday =formatter.parse(getLastDay(dates));
            Date firstDay =formatter.parse(getFirstDay(dates));
            long diff = lastday.getTime() - firstDay.getTime();
            double weeks = diff / (1000 * 60 * 60 * 24 * 7);

            weeks = Math.round(weeks);
            Log.d("weeks",Double.toString(weeks));

            DecimalFormat df2 = new DecimalFormat("#.##");
            if (weeks == 1 || weeks == 0){
                return df2.format(dates.size());
            }else{
                return df2.format(dates.size()/weeks);

            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "0.0";
        }

    }

    public static String getTotals(List dates){
        return Double.toString(dates.size());
    }
}
