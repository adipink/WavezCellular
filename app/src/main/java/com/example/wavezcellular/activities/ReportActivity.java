package com.example.wavezcellular.activities;

import static com.example.wavezcellular.utils.User.getGuest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wavezcellular.R;
import com.example.wavezcellular.utils.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportActivity extends AppCompatActivity {
    private TextView report_TXT_nameBeach;
    private MaterialButton report_BTN_back, report_BTN_submit;
    private RatingBar report_RB_review;
    private SeekBar report_SB_density, report_SB_danger, report_SB_wind, report_SB_dog, report_SB_warmth, report_SB_accessible, report_SB_jellyfish, report_SB_hygiene;
    private EditText report_EditTXT_comment;
    private ImageView report_IMG_profile;
    private Bundle bundle;
    private String BeachName;
    private FirebaseUser firebaseUserUser;
    private DatabaseReference myRef;
    private String guest;
    private String userID;
    private User user;
    //private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_upgrade);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            BeachName = bundle.getString("BEACH_NAME");
        } else {
            this.bundle = new Bundle();
            BeachName = "";
        }

        findViews();
        getCurrentUsersData();
        createListeners();
        report_TXT_nameBeach.setText("" + BeachName);
    }

    private void createListeners() {
        report_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        report_IMG_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, UserActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        report_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubmiterData();
                Intent intent = new Intent(ReportActivity.this, ShowActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getCurrentUsersData() {
        firebaseUserUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUserUser == null) {
            String guest = getGuest(bundle);
            user = new User(guest, "No Email Available");
        } else {
            myRef = FirebaseDatabase.getInstance().getReference("Users");
            userID = firebaseUserUser.getUid();
            myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    user = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void addSubmiterData() {
        String username = user.getName();
        myRef = FirebaseDatabase.getInstance().getReference("Beaches").child(BeachName).child("Reports").child(username);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myRef.child("review").setValue(report_RB_review.getRating());
                myRef.child("density").setValue(report_SB_density.getProgress() / 20);
                myRef.child("jellyfish").setValue(report_SB_jellyfish.getProgress() / 20);
                myRef.child("accessible").setValue(report_SB_accessible.getProgress() / 20);
                myRef.child("danger").setValue(report_SB_danger.getProgress() / 20);
                myRef.child("dog").setValue(report_SB_dog.getProgress() / 20);
                myRef.child("hygiene").setValue(report_SB_hygiene.getProgress() / 20);
                myRef.child("warmth").setValue(report_SB_warmth.getProgress() / 20);
                myRef.child("wind").setValue(report_SB_wind.getProgress() / 20);
                myRef.child("comment").setValue(report_EditTXT_comment.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void findViews() {
        report_BTN_back = findViewById(R.id.report_BTN_back);
        report_IMG_profile = findViewById(R.id.report_IMG_profile);
        report_TXT_nameBeach = findViewById(R.id.report_TXT_nameBeach);
        report_RB_review = findViewById(R.id.report_RB_review);
        report_SB_density = findViewById(R.id.report_SB_density);
        report_SB_jellyfish = findViewById(R.id.report_SB_jellyfish);
        report_SB_accessible = findViewById(R.id.report_SB_accessible);
        report_SB_danger = findViewById(R.id.report_SB_danger);
        report_SB_dog = findViewById(R.id.report_SB_dog);
        report_SB_hygiene = findViewById(R.id.report_SB_Hygiene);
        report_SB_warmth = findViewById(R.id.report_SB_warmth);
        report_SB_wind = findViewById(R.id.report_SB_wind);
        report_BTN_submit = findViewById(R.id.report_BTN_submit);
        report_EditTXT_comment = findViewById(R.id.report_EditTXT_comment);
    }
}