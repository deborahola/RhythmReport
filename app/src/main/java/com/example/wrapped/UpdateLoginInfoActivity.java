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

public class UpdateLoginInfoActivity extends AppCompatActivity {

    EditText updateEmail, updatePassword, updateSpotifyUser;
    Button updateButton, backToSettings;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_login_info);


        ConstraintLayout constraintLayout = findViewById(R.id.activity_update_login_info);

        // Animated gradient background that can change from one color to another
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        updateEmail = findViewById(R.id.update_loginEmailTextInput);
        updatePassword = findViewById(R.id.update_loginPasswordTextInput);
        updateSpotifyUser = findViewById(R.id.update_loginSpotifyTextInput);

        updateButton = findViewById(R.id.update_account_button);
        backToSettings = findViewById(R.id.back_to_settings);

        mAuth = FirebaseAuth.getInstance();


        //Click listener for update button
        //Checks user input and updates account
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password, spotifyUserName;
                email = updateEmail.getText().toString();
                password = updatePassword.getText().toString();
                spotifyUserName = updateSpotifyUser.getText().toString();

                //If any inputs are empty or password != passwordAgain, show error message
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(UpdateLoginInfoActivity.this, "Please enter email.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(UpdateLoginInfoActivity.this, "Please enter password.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(spotifyUserName)) {
                    Toast.makeText(UpdateLoginInfoActivity.this, "Please enter Spotify user name.", Toast.LENGTH_LONG).show();
                } else { //Add a check to ensure that the spotify profile is a valid one
                    //Else update user

                    //SpotifyHelper.authenticateSpotify(SignupActivity.this); This file is not working at the moment

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.verifyBeforeUpdateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UpdateLoginInfoActivity.this,
                                                    "Account updated. Check your NEW email for " +
                                                            "a verification link if you updated your " +
                                                            "email.", Toast.LENGTH_LONG).show();
                                            SignedInUser.setUser(email, spotifyUserName);

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(spotifyUserName)
                                                    .build();

                                            mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(UpdateLoginInfoActivity.this, "Update failed: Password update error.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(UpdateLoginInfoActivity.this, "Update failed: Email update error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        //Click listener to send user back to settings page when clicked
        backToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateLoginInfoActivity.this,
                        SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}