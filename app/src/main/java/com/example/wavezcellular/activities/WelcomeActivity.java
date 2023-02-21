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
import com.example.wavezcellular.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private MaterialTextView welcome_TXT_title;
    private EditText welcome_EDT_name, welcome_EDT_email, welcome_EDT_password;
    private MaterialButton welcome_BTN_signIn, welcome_BTN_signUp, welcome_BTN_forgotPassword, welcome_BTN_enterApp, welcome_BTN_back,
            welcome_BTN_registerApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();

        findViews();
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
            welcome_EDT_password.setError("Minmum password lenght should be 6 characters");
            welcome_EDT_password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //redirect to rest of application
                    Toast.makeText(WelcomeActivity.this, "success", Toast.LENGTH_LONG).show();
                    replaceActivityEnter();
                } else {
                    Toast.makeText(WelcomeActivity.this, "Failed to login! please cheak your credentials", Toast.LENGTH_LONG).show();
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
                                                createEnterMode("signin");
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
        welcome_BTN_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEnterMode("signin");
            }
        });

        welcome_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEnterMode("signup");
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
        welcome_BTN_signIn.setVisibility(View.VISIBLE);
        welcome_BTN_signUp.setVisibility(View.VISIBLE);
        welcome_EDT_email.setVisibility(View.INVISIBLE);
        welcome_EDT_password.setVisibility(View.INVISIBLE);
        welcome_BTN_enterApp.setVisibility(View.INVISIBLE);
        welcome_BTN_registerApp.setVisibility(View.INVISIBLE);
        welcome_BTN_back.setVisibility(View.INVISIBLE);
        welcome_BTN_forgotPassword.setVisibility(View.INVISIBLE);
        welcome_EDT_name.setVisibility(View.INVISIBLE);
    }

    private void createEnterMode(String mode) {
        welcome_BTN_signIn.setVisibility(View.INVISIBLE);
        welcome_BTN_signUp.setVisibility(View.INVISIBLE);
        welcome_EDT_email.setVisibility(View.VISIBLE);
        welcome_EDT_password.setVisibility(View.VISIBLE);
        welcome_BTN_enterApp.setVisibility(View.VISIBLE);
        welcome_BTN_back.setVisibility(View.VISIBLE);
        if (mode.equals("signin")) { //sign in
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

    private void findViews() {
        welcome_TXT_title = findViewById(R.id.welcome_TXT_title);
        welcome_EDT_name = findViewById(R.id.welcome_EDT_name);
        welcome_EDT_email = findViewById(R.id.welcome_EDT_email);
        welcome_EDT_password = findViewById(R.id.welcome_EDT_password);
        welcome_BTN_signIn = findViewById(R.id.welcome_BTN_signIn);
        welcome_BTN_signUp = findViewById(R.id.welcome_BTN_signUp);
        welcome_BTN_forgotPassword = findViewById(R.id.welcome_BTN_forgotPassword);
        welcome_BTN_enterApp = findViewById(R.id.welcome_BTN_enterApp);
        welcome_BTN_back = findViewById(R.id.welcome_BTN_back);
        welcome_BTN_registerApp = findViewById(R.id.welcome_BTN_registerApp);
    }

    private void replaceActivityEnter() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void replaceActivityBack() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}