package com.example.wrapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    Button backToHome, deleteAccount, update;
//    FirebaseAuth auth;
//    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        RelativeLayout relativeLayout = findViewById(R.id.activity_settings);

        // Animated gradient background that can change from one color to another
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();


        backToHome = findViewById(R.id.back_to_home);
        deleteAccount = findViewById(R.id.delete_account);
        update = findViewById(R.id.update_login_info);

//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TempActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateLoginInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Delete account?");
                builder.setMessage("Are you sure you want to delete your RhythmReport account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Proceed with deleting the account if "Yes" is clicked
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseAuth.getInstance().signOut(); // Sign out after deletion
                                    SignedInUser.SignUserOut();

                                    Toast.makeText(SettingsActivity.this,
                                            "Account deletion successful.",
                                            Toast.LENGTH_SHORT).show();

                                    // Redirect to login page
                                    Intent intent = new Intent(SettingsActivity.this,
                                            LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // Finish the current activity
                                } else {
                                    Toast.makeText(SettingsActivity.this,
                                            "Account deletion failed. Try logging out and " +
                                                    "signing in again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User decided not to delete the account
                        // Do nothing if "No" is clicked; stay on the same screen
                    }
                });
                builder.create().show(); // Show the alert dialog
            }
        });



    }


}