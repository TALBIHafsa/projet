package com.example.halalscan.Opening;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.halalscan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    EditText name, email, password, confirmPassword;
    TextView LoginRedirectText;
    Button SignUp;
    FirebaseDatabase DB;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView textView = findViewById(R.id.LoginRedirectText);
        String text = "Already have an account? SIGN IN";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUp.this, login.class));
            }
        };
        spannableString.setSpan(clickableSpan, text.indexOf("SIGN IN"), text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        SignUp = findViewById(R.id.SignUp);

        DB = FirebaseDatabase.getInstance();
        usersRef = DB.getReference("users");

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String u_name = name.getText().toString();
                final String u_email = email.getText().toString();
                final String u_password = password.getText().toString();
                final String u_confirmPassword = confirmPassword.getText().toString();

                if (!isValidEmail(u_email)) {
                    Toast.makeText(signUp.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!u_password.equals(u_confirmPassword)) {
                    Toast.makeText(signUp.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                usersRef.orderByChild("email").equalTo(u_email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(signUp.this, "There's already an account with this email , login or try a different one", Toast.LENGTH_SHORT).show();
                        } else {
                            users user = new users(u_email, u_name, u_password);
                            usersRef.child(u_name).setValue(user);

                            Toast.makeText(signUp.this, "You have signed up correctly", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signUp.this, login.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(signUp.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        LoginRedirectText = findViewById(R.id.LoginRedirectText);
        LoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUp.this, login.class));
            }
        });
    }

    public void goToLogin(View v) {
        startActivity(new Intent(signUp.this, login.class));
    }

    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
