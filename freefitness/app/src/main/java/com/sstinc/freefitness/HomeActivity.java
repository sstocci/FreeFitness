package com.sstinc.freefitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    //private FirebaseAuth.AuthStateListener mAuthList;
    FirebaseAuth mAuth;
    private BottomNavigationView navigationMenu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Creates Navigation Menu at the bottom of the screen
        // Allows users to change Activities by press a button on the menu
        navigationMenu = findViewById (R.id.navigationMenu);
        navigationMenu.getMenu().getItem(0).setChecked(true);

        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.trendPage:
                        startActivity(new Intent(HomeActivity.this,TrendsActivity.class));
                        break;
                    case R.id.logWorkoutPage:
                        startActivity(new Intent(HomeActivity.this,LogWorkout.class));
                        break;
                    case R.id.BMIPage:
                        startActivity(new Intent(HomeActivity.this,BMI.class));
                        break;
//                    case R.id.calendarPage:
//                        startActivity(new Intent(HomeActivity.this,CalendarActivity.class));
//                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Creates menu on the top of the screen (contains logout function)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // This allows the user to logout of their account
        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
        return false;
    }
}
