package com.sstinc.freefitness;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Setting up my variables
    private AutoCompleteTextView emailTextview;
    private String email;
    private String password;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextView;

    // used for Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Setting up the Views
        signupTextView = findViewById(R.id.signupTextView);
        loginButton = findViewById(R.id.email_sign_in_button);
        emailTextview = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.email_sign_in_button);
        mAuth = FirebaseAuth.getInstance();

        // this is for when the user returns to the app
        // the user won't have to re-login every time
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        };
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get users Email and password
                email=emailTextview.getText().toString();
                password=passwordEditText.getText().toString();

                // if the password and email aren't blank, if they aren't attempt login
                // if they password or email aren't correct, display a message
                if ((email.isEmpty()!=true)||(password.isEmpty()!=true)){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Signin has failed. Check Email and Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // Launches a new screen for the user to create an account
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        // When the app launches I check to see if the user has already logged in before
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }
}


