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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.spotify.sdk.android.auth.app.SpotifyNativeAuthUtil;

public class SignupActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etPasswordAgain, etSpotifyUser;
    Button signupButton, toLoginButton;
    FirebaseAuth mAuth;

    //If a user is logged in on launch, Open main activity
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
        setContentView(R.layout.activity_signup);


        ConstraintLayout constraintLayout = findViewById(R.id.activity_signup);

        // Animated gradient background that can change from one color to another
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();


        etEmail = findViewById(R.id.signupEmailTextInput);
        etPassword = findViewById(R.id.signupPasswordTextInput);
        etPasswordAgain = findViewById(R.id.signupAgainPasswordTextInput);
        etSpotifyUser = findViewById(R.id.signupSpotifyTextInput);

        signupButton = findViewById(R.id.signUpButton);
        toLoginButton = findViewById(R.id.toLoginButton);

        mAuth = FirebaseAuth.getInstance();

        //Click listener for sign up button
        //Checks user input and creates account if one does not already exist
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password, passwordAgain, spotifyUserName;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                passwordAgain = etPasswordAgain.getText().toString();
                spotifyUserName = etSpotifyUser.getText().toString();

                //If any inputs are empty or password != passwordAgain, show error message
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Please enter email.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Please enter password.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(passwordAgain)) {
                    Toast.makeText(SignupActivity.this, "Please enter password twice.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(spotifyUserName)) {
                    Toast.makeText(SignupActivity.this, "Please enter Spotify user name.", Toast.LENGTH_LONG).show();
                } else if (!(password.equals(passwordAgain))){
                    Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                } else { //Add a check to ensure that the spotify profile is a valid one
                    //Else create user

                    //SpotifyHelper.authenticateSpotify(SignupActivity.this); This file is not working at the moment

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                                SignedInUser.setUser(email, spotifyUserName);

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(spotifyUserName)
                                        .build();

                                mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(getApplicationContext(), TempActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //Click listener to sent user to login page if button clicked
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SpotifyHelper.onActivityResult(requestCode, resultCode, data);
    }
}