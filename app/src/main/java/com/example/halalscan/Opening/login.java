package com.example.halalscan.Opening;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.App.home;
import com.example.halalscan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    EditText Email, Password;
    Button SignIn;
    TextView SignUpRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView textView = findViewById(R.id.SignUpRedirectText);
        String text = "Don’t have an account? SIGN UP";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Start SignIn activity
                startActivity(new Intent(login.this, signUp.class));
            }
        };
        spannableString.setSpan(clickableSpan, text.indexOf("SIGN UP"), text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        SignUpRedirectText = findViewById(R.id.SignUpRedirectText);
        SignIn = findViewById(R.id.SignIn);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail() | !validatePassword()) {

                }else{

                    checkUser();

                }
                }});



        }

    public boolean validateEmail() {
        String val = Email.getText().toString();
        if (val.isEmpty()) {
            Email.setError("incorrect email");
            return false;
        } else {
            Email.setError(null);
            return true;
        }
    }
    public boolean validatePassword(){
            String val = Password.getText().toString();
            if (val.isEmpty()){
                Password.setError("incorrect password");
                return false;
            } else {
                Password.setError(null);
                return true;
            }

    }

    public void checkUser() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDB = ref.orderByChild("email").equalTo(email);
        checkUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Email.setError(null);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String passwordFromDB = ds.child("password").getValue(String.class);

                        if (passwordFromDB != null && passwordFromDB.equals(password)) {
                            Password.setError(null);
                            Intent intent = new Intent(login.this, home.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    Password.setError("Invalid Credentials");
                    Password.requestFocus();
                } else {
                    Email.setError("User does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DATABASE_ERROR", "Database error: " + error.getMessage());
            }
        });
    }


}
