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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    EditText name, email, password, confirmPassword;
    TextView LoginRedirectText;
    Button SignUp;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public void goToLogin(View v) {
        startActivity(new Intent(signUp.this, login.class));
    }

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        usersRef = DB.getReference("users");

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
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

    private void signUpUser() {
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

        // Check if the email already exists
        usersRef.orderByChild("email").equalTo(u_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email already exists
                    Toast.makeText(signUp.this, "Email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Email does not exist, proceed with user creation
                    mAuth.createUserWithEmailAndPassword(u_email, u_password)
                            .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();
                                        if (user != null) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(u_name)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Create user object and store in Firebase Realtime Database
                                                                users user = new users(u_email, u_name, u_password);
                                                                usersRef.child(uid).setValue(user);
                                                                Toast.makeText(signUp.this, "You have signed up correctly", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(signUp.this, login.class));
                                                            } else {
                                                                Toast.makeText(signUp.this, "Failed to add username", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(signUp.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(signUp.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
