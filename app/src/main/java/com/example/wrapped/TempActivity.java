package com.example.wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TempActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView, tv2;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        tv2 = findViewById(R.id.tv2);
        user = auth.getCurrentUser();

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
    }
}