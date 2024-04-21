package com.example.wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class PastSummaries extends AppCompatActivity {

    Button backToHome;

    // Insert more variables here if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_summaries);


        RelativeLayout relativeLayout = findViewById(R.id.activity_past_summaries);

        // Animated gradient background that can change from one color to another
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();


        backToHome = findViewById(R.id.back2Home);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TempActivity.class);
                startActivity(intent);
                finish();
            }
        });




        // Insert other code here if needed




    }


    // Insert other code here if needed



}