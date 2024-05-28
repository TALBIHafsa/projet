package com.example.halalscan.Opening;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.App.home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.halalscan.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in, skip instruction pages
                    Intent homeIntent = new Intent(MainActivity.this, home.class);
                    startActivity(homeIntent);
                    finish();
                } else {
                    // No user is signed in, show instruction pages
                    Intent intent = new Intent(MainActivity.this, page1.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000); // Delay for 3 seconds
    }
}