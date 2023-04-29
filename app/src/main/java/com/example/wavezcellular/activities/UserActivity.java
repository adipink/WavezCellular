package com.example.wavezcellular.activities;

import static com.example.wavezcellular.utils.User.getGuest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wavezcellular.R;
import com.example.wavezcellular.utils.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {

    private MaterialButton user_BTN_back, user_BTN_change, user_BTN_signout;
    private MaterialTextView user_TXT_name, user_TXT_email;

    //fireBase
    private FirebaseUser firebaseUserUser;
    private DatabaseReference myRef;
    private String userID;
    private User user;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        findViews();
        createListener();
        getCurrentUsersData();

    }

    private void createListener() {
        user_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        user_BTN_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        user_BTN_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Change Your Name:");
                final EditText input = new EditText(view.getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        user.setName(input.getText().toString());
                        saveData();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();

            }
        });
    }

    public void saveData() {
        if (!user.getName().contains("guest")) {
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(userID)
                    .setValue(user);
            setUserInfo();
        }
    }


    public void getCurrentUsersData() {
        firebaseUserUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUserUser.getDisplayName().equals("")) {
            String guest = getGuest(bundle);
            user = new User(guest, "No Email Available");
            setUserInfo();
        } else {
            myRef = FirebaseDatabase.getInstance().getReference("Users");
            userID = firebaseUserUser.getUid();
            myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    user = dataSnapshot.getValue(User.class);
                    setUserInfo();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void setUserInfo() {
        user_TXT_name.setText(user.getName());
        user_TXT_email.setText(user.getEmail());
    }

    private void findViews() {
        user_BTN_back = findViewById(R.id.user_BTN_back);
        user_BTN_change = findViewById(R.id.user_BTN_change);
        user_BTN_signout = findViewById(R.id.user_BTN_signout);
        user_TXT_name = findViewById(R.id.user_TXT_name);
        user_TXT_email = findViewById(R.id.user_TXT_email);
    }
}