package com.example.wavezcellular.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wavezcellular.R;
import com.example.wavezcellular.adapters_holders.UserReportAdapter;
import com.example.wavezcellular.utils.UserReport;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserReportsActivity extends AppCompatActivity {
    private final int MAX_NUM = 5;
    private TextView userReports_TXT_nameBeach;
    private MaterialButton userReports_BTN_back, userReports_BTN_report;
    private RecyclerView userReports_RecycleView_reports;
    private TextView userReports_TXT_headline;
    private Bundle bundle;
    private String BeachName;
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private boolean isGuest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            BeachName = bundle.getString("BEACH_NAME");
        } else {
            this.bundle = new Bundle();
            BeachName = "";
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        isGuest = firebaseUser == null;
        myRef = FirebaseDatabase.getInstance().getReference("Beaches").child(BeachName).child("Reports");

        findViews();
        createListeners();
        userReports_TXT_nameBeach.setText("" + BeachName);
        getReports();
    }

    private void createListeners() {
        userReports_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserReportsActivity.this, ShowActivity.class);
                bundle.putString("BEACH_NAME", BeachName);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });


        userReports_BTN_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnReports();
                /*Intent intent = new Intent(UserReportsActivity.this, ReportActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();*/
            }
        });
    }

    private void clickOnReports() {
        if(isGuest){ // user who are not registered cannot report
            AlertDialog.Builder builder = new AlertDialog.Builder(UserReportsActivity.this);
            builder.setTitle("Do you want to add a report to this beach ?");
            builder.setMessage("You need to register or login first");
            builder.setCancelable(false);
            builder.setPositiveButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });
            builder.setNegativeButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                // When the user click yes button then app will take it to the register/login page
                replaceActivity("Welcome");

            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        }else{
            replaceActivity("Report");
        }
    }

    private void getReports() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<UserReport> list = new ArrayList<>();
                HashMap<String, HashMap<String, Object>> data = (HashMap) dataSnapshot.getValue(Object.class);
                if (data != null) {
                    for (Map.Entry<String, HashMap<String, Object>> set :
                            data.entrySet()) {
                        list.add(new UserReport(set.getKey(), set.getValue()));
                    }
                }
                createReportsRec(list);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createReportsRec(ArrayList<UserReport> list) {
        userReports_RecycleView_reports.setLayoutManager(new LinearLayoutManager(this));
        userReports_RecycleView_reports.setAdapter(new UserReportAdapter(getApplicationContext(), list));
    }

    private void replaceActivity(String mode) {
        Intent intent;
        if (mode.equals("Profile")) {
            intent = new Intent(this, UserActivityUpgrade.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        if (mode.equals("Report")) {
            intent = new Intent(this, ReportActivity.class);
            bundle.putString("BEACH_NAME", BeachName);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        if (mode.equals("Home")) {
            intent = new Intent(this, HomeActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        if (mode.equals("Welcome")) {
            intent = new Intent(this, WelcomeActivity.class);
            bundle.putString("LOGIN_STATE", "login");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    private void findViews() {
        userReports_BTN_back = findViewById(R.id.userReports_BTN_back);
        userReports_TXT_nameBeach = findViewById(R.id.userReports_TXT_nameBeach);
        userReports_BTN_report = findViewById(R.id.userReports_BTN_report);
        userReports_TXT_headline = findViewById(R.id.userReports_TXT_headline);
        userReports_RecycleView_reports = findViewById(R.id.userReports_RecyclerView_reports);
    }


}