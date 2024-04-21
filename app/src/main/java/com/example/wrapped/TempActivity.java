package com.example.wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TempActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, settings;
    TextView textView, tv2;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);


        RelativeLayout relativeLayout = findViewById(R.id.activity_temp);

        // Animated gradient background that can change from one color to another
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();


        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_email);
        tv2 = findViewById(R.id.spotify_username);
        user = auth.getCurrentUser();

        settings = findViewById(R.id.settings);

        SignedInUser.validateCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (SignedInUser.getCurrentUser() == null) {
                Toast.makeText(TempActivity.this, "Signed in user is null", Toast.LENGTH_LONG).show();
            }
            textView.setText(SignedInUser.getCurrentUserEmail());
            tv2.setText(SignedInUser.getCurrentUserSpotify());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SignedInUser.SignUserOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}