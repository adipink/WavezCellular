package com.example.wavezcellular.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wavezcellular.R;
import com.example.wavezcellular.utils.ActivityManager;
import com.example.wavezcellular.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * WelcomeActivity
 * Activity to enable the user to login if he has signed up previously or sign up.
 * After signing in switch to HomeActivity
 * After signing up switch to login to login with new user
 * User can go back to MenuActivity
 */
public class WelcomeActivity extends AppCompatActivity {

    private ActivityManager activityManager;
    private FirebaseAuth mAuth;
    private MaterialTextView welcome_TXT_title;
    private EditText welcome_EDT_name, welcome_EDT_email, welcome_EDT_password;
    private MaterialButton /*welcome_BTN_signIn, welcome_BTN_signUp,*/ welcome_BTN_forgotPassword, welcome_BTN_enterApp, welcome_BTN_back,
            welcome_BTN_registerApp;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        activityManager = new ActivityManager(this);
        mAuth = FirebaseAuth.getInstance();
        bundle = getIntent().getExtras();
        String state ;
        if (bundle != null) {
            state = bundle.getString("LOGIN_STATE");

        }else{
            bundle = new Bundle();
            state = "login";
        }
        findViews();
        createEnterMode(state);
        createListeners();
    }

    private void resetPassword() {
        String email = welcome_EDT_email.getText().toString();

        if (email.isEmpty()) {
            welcome_EDT_email.setError("Email is required!");
            welcome_EDT_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            welcome_EDT_email.setError("Please enter a valid email!");
            welcome_EDT_email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(WelcomeActivity.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(WelcomeActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signInUser() {
        String email = welcome_EDT_email.getText().toString();
        String password = welcome_EDT_password.getText().toString();

        if (email.isEmpty()) {
            welcome_EDT_email.setError("Email is required!");
            welcome_EDT_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            welcome_EDT_email.setError("Please provide valid email!");
            welcome_EDT_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            welcome_EDT_password.setError("Password is required");
            welcome_EDT_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            welcome_EDT_password.setError("Minimum password length should be 6 characters");
            welcome_EDT_password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign-in was successful

                    // Fetch the user's data from the Realtime Database
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(WelcomeActivity.this, "Welcome " + dataSnapshot.child("name").getValue(String.class), Toast.LENGTH_LONG).show();
                                replaceActivityEnter();
                            } else {
                                // User data doesn't exist in the database
                                Toast.makeText(WelcomeActivity.this, "Failed to fetch user data", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that occur during the retrieval
                            Toast.makeText(WelcomeActivity.this, "Error fetching user data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // Sign-in failed
                    Toast.makeText(WelcomeActivity.this, "Failed to login! please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signUpUser() {
        String email = welcome_EDT_email.getText().toString().trim();
        String name = welcome_EDT_name.getText().toString().trim();
        String password = welcome_EDT_password.getText().toString().trim();

        if (name.isEmpty()) {
            welcome_EDT_name.setError("Full name is required");
            welcome_EDT_name.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            welcome_EDT_email.setError("Email is required");
            welcome_EDT_email.requestFocus();
            return;
        }
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            welcome_EDT_email.setError("Please provide valid email!");
            welcome_EDT_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            welcome_EDT_password.setError("Password is required");
            welcome_EDT_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            welcome_EDT_password.setError("Minmum password lenght should be 6 characters");
            welcome_EDT_password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(WelcomeActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                createEnterMode("login");
                                            } else {
                                                Toast.makeText(WelcomeActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(WelcomeActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createListeners() {
        welcome_BTN_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        welcome_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivityBack();
            }
        });

        welcome_BTN_enterApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });

        welcome_BTN_registerApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });
    }


    private void openingMode() {
        welcome_EDT_email.setVisibility(View.INVISIBLE);
        welcome_EDT_password.setVisibility(View.INVISIBLE);
        welcome_BTN_enterApp.setVisibility(View.INVISIBLE);
        welcome_BTN_registerApp.setVisibility(View.INVISIBLE);
        welcome_BTN_back.setVisibility(View.INVISIBLE);
        welcome_BTN_forgotPassword.setVisibility(View.INVISIBLE);
        welcome_EDT_name.setVisibility(View.INVISIBLE);
    }

    private void createEnterMode(String mode) {
        welcome_EDT_email.setVisibility(View.VISIBLE);
        welcome_EDT_password.setVisibility(View.VISIBLE);
        welcome_BTN_enterApp.setVisibility(View.VISIBLE);
        welcome_BTN_back.setVisibility(View.VISIBLE);
        if (mode.equals("login")) { //sign in/login
            welcome_BTN_forgotPassword.setVisibility(View.VISIBLE);
            welcome_BTN_registerApp.setVisibility(View.INVISIBLE);
            welcome_BTN_enterApp.setVisibility(View.VISIBLE);
            welcome_EDT_name.setVisibility(View.INVISIBLE);
        } else { //sign up
            welcome_EDT_name.setVisibility(View.VISIBLE);
            welcome_BTN_registerApp.setVisibility(View.VISIBLE);
            welcome_BTN_enterApp.setVisibility(View.INVISIBLE);
        }
    }



    private void replaceActivityEnter() {
        activityManager.startActivity(HomeActivity.class,bundle);
    }

    private void replaceActivityBack() {
        activityManager.startActivity(MenuActivity.class,bundle);
    }
    private void findViews() {
        welcome_TXT_title = findViewById(R.id.welcome_TXT_title);
        welcome_EDT_name = findViewById(R.id.welcome_EDT_name);
        welcome_EDT_email = findViewById(R.id.welcome_EDT_email);
        welcome_EDT_password = findViewById(R.id.welcome_EDT_password);
        welcome_BTN_forgotPassword = findViewById(R.id.welcome_BTN_forgotPassword);
        welcome_BTN_enterApp = findViewById(R.id.welcome_BTN_enterApp);
        welcome_BTN_back = findViewById(R.id.welcome_BTN_back);
        welcome_BTN_registerApp = findViewById(R.id.welcome_BTN_registerApp);
    }

}