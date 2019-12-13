package com.sstinc.freefitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class BMI extends AppCompatActivity {

    private EditText heightEditText;
    private EditText weightEditText;

    private TextView bmiValue;
    private TextView rangeValue;

    private Spinner heightSpinner;
    private Spinner weightSpinner;

    private BottomNavigationView navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_layout);

        navigationMenu = findViewById(R.id.navigationMenu);
        navigationMenu.getMenu().getItem(3).setChecked(true);

        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.trendPage:
                        Toast.makeText(getApplicationContext(),"Trends Page",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BMI.this,TrendsActivity.class));
                        break;
                    case R.id.logWorkoutPage:
                        Toast.makeText(getApplicationContext(),"Log Workout Page",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BMI.this,LogWorkout.class));
                        break;
                    case R.id.homePage:
                        startActivity(new Intent(BMI.this,HomeActivity.class));
                        break;
//                    case R.id.calendarPage:
//                        startActivity(new Intent(BMI.this,CalendarActivity.class));
//                        break;
                }
                return false;
            }
        });

        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);

        bmiValue = findViewById(R.id.bmiValue);
        rangeValue = findViewById(R.id.rangeValue);

        heightSpinner = findViewById(R.id.heightSpinner);
        weightSpinner = findViewById(R.id.weightSpinner);


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //YOUR CODE
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //YOUR CODE
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!weightEditText.getText().toString().isEmpty() && !heightEditText.getText().toString().isEmpty()){
                    String heightText = heightSpinner.getSelectedItem().toString();
                    String weightText = weightSpinner.getSelectedItem().toString();
                    calculate(heightText, weightText);
                }


            }
        };
        heightEditText.addTextChangedListener(watcher);
        weightEditText.addTextChangedListener(watcher);


        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!weightEditText.getText().toString().isEmpty() && !heightEditText.getText().toString().isEmpty()) {
                    String heightText = heightSpinner.getSelectedItem().toString();
                    String weightText = weightSpinner.getSelectedItem().toString();
                    calculate(heightText, weightText);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Enter Some data!",Toast.LENGTH_SHORT).show();
            }
        };
        weightSpinner.setOnItemSelectedListener(spinnerListener);
        heightSpinner.setOnItemSelectedListener(spinnerListener);

    }
    //heightSpinner.
    //Determines the input type to be either cm and kilograms or inches and pounds and will always convert values to metric
    //before doing bmi calculation because metric formula is cleaner
    private void calculate(String height, String weight) {
        double bmi = 0, heightValue, weightValue;
        String heightStr = heightEditText.getText().toString();
        String weightStr = weightEditText.getText().toString();

        if (heightSpinner.getSelectedItem().toString().equals("Meters")){
            heightValue=39.37* Double.parseDouble(heightStr);
        }else{
            heightValue=Double.parseDouble(heightStr);
        }

        if(weightSpinner.getSelectedItem().toString().equalsIgnoreCase("Kilograms")){
            weightValue= 2.205 * Double.parseDouble(weightStr);
        }else{
            weightValue=Double.parseDouble(weightStr);
        }

        bmi= (weightValue*705)/(heightValue*heightValue);
        DecimalFormat df2 = new DecimalFormat("#.##");
        bmiValue.setText(String.valueOf( df2.format(bmi)));
        displayBMI(bmi);
    }

    private void displayBMI(double bmi) {
        String rangeLabel = "";

        if(Double.compare(bmi, 15f) <= 0) {
            rangeLabel = "Very Severely Underweight";
        } else if(Double.compare(bmi, 15f) > 0 && Double.compare(bmi, 18.5f) <= 0) {
            rangeLabel = "Severely Underweight";
        } else if(Double.compare(bmi, 16f) > 0 && Double.compare(bmi, 18.5f) <= 0) {
            rangeLabel = "Underweight";
        } else if(Double.compare(bmi, 18.5f) > 0 && Double.compare(bmi, 25f) <= 0) {
            rangeLabel = "Normal";
        } else if(Double.compare(bmi, 25f) > 0 && Double.compare(bmi, 30f) <= 0) {
            rangeLabel = "Overweight";
        } else if(Double.compare(bmi, 30f) > 0 && Double.compare(bmi, 35f) <= 0) {
            rangeLabel = "Moderately Obese";
        } else if(Double.compare(bmi, 35f) > 0 && Double.compare(bmi, 40f) <= 0) {
            rangeLabel = "Severely Obese";
        } else {
            rangeLabel = "Morbidly Obese";
        }
        rangeValue.setText(rangeLabel);
    }
}