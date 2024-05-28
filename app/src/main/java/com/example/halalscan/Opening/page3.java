package com.example.halalscan.Opening;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.R;

public class page3 extends AppCompatActivity {



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);

        TextView textView = findViewById(R.id.textView5);
        String text = "Already have an account? SIGN IN";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Start SignIn activity
                startActivity(new Intent(page3.this, login.class));
            }
        };
        spannableString.setSpan(clickableSpan, text.indexOf("SIGN IN"), text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void goToPage2(View v){
        Intent i = new Intent(this, page2.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }


    public void goToSignUp(View v){
        Intent i = new Intent(this, signUp.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}