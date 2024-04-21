package com.example.wrapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etSpotifyUser;
    Button loginButton, toSignUpButton;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), TempActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ConstraintLayout constraintLayout = findViewById(R.id.activity_login);

        // Animated gradient background that can change from one color to another
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();


        etEmail = findViewById(R.id.loginEmailTextInput);
        etPassword = findViewById(R.id.loginPasswordTextInput);
        etSpotifyUser = findViewById(R.id.loginSpotifyTextInput);

        loginButton = findViewById(R.id.loginButton);
        toSignUpButton = findViewById(R.id.toSignUpButton);

        mAuth = FirebaseAuth.getInstance();

        //Click listener for login button
        //Checks user input and logs in if account exists
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, passwordAgain, spotifyUserName;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                spotifyUserName = etSpotifyUser.getText().toString();

                //If inputs are empty, show error message
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please enter email.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter password.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(spotifyUserName)) {
                    Toast.makeText(LoginActivity.this, "Please enter Spotify user name.", Toast.LENGTH_LONG).show();
                } else {
                    //Else attempt to log in user then send them to main activity
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                SignedInUser.setUser(email, spotifyUserName);
                                Intent intent = new Intent(getApplicationContext(), TempActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //Click listener to send user to signup page when clicked
        toSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}