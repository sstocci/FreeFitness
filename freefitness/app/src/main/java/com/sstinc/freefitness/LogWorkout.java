package com.sstinc.freefitness;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class LogWorkout extends AppCompatActivity {

    //set up variables
    private Spinner setSpinner;
    private EditText exerciseEditText;
    private String workoutExcerise;
    private WorkoutArrayAdapter adapter;
    private Button nextExcerise;
    private Button endWorkout;
    private BottomNavigationView navigationMenu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excerise_main);

        // This is where the user enters the exercise name Ex) Bench press, Squat...
        exerciseEditText = findViewById(R.id.exceriseEditText);

        // A dropdown list to allow the user to select the number of sets 1 to 10
        setSpinner = findViewById(R.id.setSpinner);

        // Button at bottom of the screen
        nextExcerise = findViewById(R.id.nextExceriseButton);
        //setSpinner.setPrompt("Sets");
        endWorkout = findViewById(R.id.finishWorkoutButton);
        navigationMenu = findViewById(R.id.navigationMenu);
        navigationMenu.getMenu().getItem(1).setChecked(true);

        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homePage:
                        startActivity(new Intent(LogWorkout.this,HomeActivity.class));
                        break;
                    case R.id.trendPage:
                        startActivity(new Intent(LogWorkout.this,TrendsActivity.class));
                        break;
                    case R.id.BMIPage:
                        startActivity(new Intent(LogWorkout.this,BMI.class));
                        break;
//                    case R.id.calendarPage:
//                        startActivity(new Intent(LogWorkout.this,CalendarActivity.class));
//                        break;
                }
                return false;
            }
        });

        // This is where we will store the workout information
        final ArrayList<Exercise> workout = new ArrayList<>();

        // Once the user selects the number of sets they wish to do it will populate a list with
        // the corresponding amount of sets
        setSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Our WorkoutArray Adapter is what allows us to create list elements that are the same
                adapter = new WorkoutArrayAdapter(LogWorkout.this,R.layout.set_item,workout );

                // Remove any old data or if we want to change the sets it clears all the data on screen
                adapter.clear();

                //Gets the exercise the user entered in exerciseEditText
                workoutExcerise = exerciseEditText.getText().toString();

                ListView workoutsets = findViewById(R.id.setListView);
                //For each set it creates a list item that will attach to our adapter after
                for (int i=0;i<Integer.parseInt(setSpinner.getSelectedItem().toString());i++) {
                    workout.add(new Exercise(workoutExcerise, i+1));
                }

                //takes all our workout items and attaches them to our adapter
                workoutsets.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // needed for onItemSelected Listener but does nothing for this app :/
            }
        });

        // Once the user fills in the data for each set the user they will move onto the next exercise
        // All this does is store the data logged in the list and puts it into the database
        // then clears the adapter to log the next exercise
        nextExcerise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> weights = adapter.getWeightsLifted(Integer.parseInt(setSpinner.getSelectedItem().toString()));
                ArrayList<Integer> reps = adapter.getRepsLifted(Integer.parseInt(setSpinner.getSelectedItem().toString()));
                //Toast.makeText(LogWorkout.this, setSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                for (int i=0;i< reps.size();i++){
                    DatabaseHelper dbHandler = new DatabaseHelper(getApplicationContext(), null, null, 1);
                    dbHandler.addData(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            exerciseEditText.getText().toString(),i+1, weights.get(i),reps.get(i));
                }
                //Toast.makeText(LogWorkout.this, setSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                adapter.clear();
            }
        });
        endWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Nice Job!",Toast.LENGTH_SHORT).show();
                DatabaseHelper dbHandler = new DatabaseHelper(getApplicationContext(), null, null, 1);
                dbHandler.deleteRow();

            }
        });

    }

}
