package com.example.halalscan.Opening;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalscan.App.home;
import com.example.halalscan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();


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
                if(!validateEmail() || !validatePassword()) {

                }else{

                    loginUser();

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

    public void loginUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait while we are checking your data");
        progressDialog.show();

        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    checkUserInDatabase(currentUser,progressDialog);
                } else {
                    Toast.makeText(login.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(login.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserInDatabase(FirebaseUser currentUser,ProgressDialog progressDialog) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressDialog.dismiss();
                    Intent i = new Intent(login.this, home.class);
                    startActivity(i);
                } else {

                    progressDialog.dismiss();
                    Toast.makeText(login.this, "User not found in database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressDialog.dismiss();
                Log.e("DATABASE_ERROR", "Database error: " + error.getMessage());
            }
        });
    }



}
