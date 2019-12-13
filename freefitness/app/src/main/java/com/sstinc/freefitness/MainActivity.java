package com.sstinc.freefitness;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    // set up variables
    private AutoCompleteTextView emailTextview;
    private String email;
    private String password;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView switchTextView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Setting up the Views
        switchTextView = findViewById(R.id.signupTextView);
        loginButton = findViewById(R.id.email_sign_in_button);
        emailTextview = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.email_sign_in_button);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        };


        // Getting the firebase reference url
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gets users input for email and password
                email=emailTextview.getText().toString();
                password=passwordEditText.getText().toString();

                // if email and password aren't blank then create user
                if ((email.isEmpty()==false)||(password.isEmpty()==false)){
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Signup has failed. Try again!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Signup has worked!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            }
                        }

                    });
                }else {
                    // if the email or password are blank then display a message
                    Toast.makeText(MainActivity.this, "Email and Password are Empty?", Toast.LENGTH_SHORT).show();
                    Log.d("Email:", email);
                    Log.d("password", password);
                }
            }
        });

        //if they already have an account they can just login by going to this state
        switchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

    }
    // allows the user to return to app without logging again
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }
}

