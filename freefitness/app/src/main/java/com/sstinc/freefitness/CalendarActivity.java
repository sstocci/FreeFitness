package com.sstinc.freefitness;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;


public class CalendarActivity extends AppCompatActivity {
    private CalendarView myCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

    }
}
