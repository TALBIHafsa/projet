package com.example.halalscan.Opening;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Delayed transition to page1 after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, page1.class);
                startActivity(intent);
                finish(); // Optional: Finish MainActivity if you don't want it to be accessible after transition
            }
        }, 3000); // 5000 milliseconds = 5 seconds
    }
}

