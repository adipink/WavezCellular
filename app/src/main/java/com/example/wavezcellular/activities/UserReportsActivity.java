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
import com.example.wavezcellular.utils.ActivityManager;
import com.example.wavezcellular.utils.UserReport;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * UserReportsActivity
 * Activity to show all reports submitted by the users.
 * Each report shows the user's review of the beach and a blurb about it.
 * User can choose to add a report if he's signed in, otherwise he can go sign in/sign up
 * User can also go back to the showActivity
 */
public class UserReportsActivity extends AppCompatActivity {
    private final int MAX_NUM = 5;
    private ActivityManager activityManager;
    private TextView userReports_TXT_nameBeach;
    private MaterialButton userReports_BTN_back, userReports_BTN_report;
    private RecyclerView userReports_RecycleView_reports;
    private TextView userReports_TXT_headline;
    private Bundle bundle;
    private String BeachName;
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private DatabaseReference userRef;
    private DatabaseReference photoRef;
    private boolean isGuest;
    private HashMap<String,String> usersPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports);
        activityManager = new ActivityManager(this);
        usersPhotos = new HashMap<>();
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
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        findViews();
        createListeners();
        ShowActivity.setBeachName((MaterialTextView) userReports_TXT_nameBeach, null , BeachName);
        getUserPhotos();
        getReports();
    }

    private void createListeners() {
        userReports_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("BEACH_NAME", BeachName);
                activityManager.startActivity(ShowActivity.class,bundle);
            }
        });


        userReports_BTN_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnReports();
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
                bundle.putString("LOGIN_STATE", "login");
                activityManager.startActivity( WelcomeActivity.class,bundle);

            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        }else{
            bundle.putString("BEACH_NAME", BeachName);
            activityManager.startActivity(ReportActivity.class,bundle);
        }
    }

    private void getUserPhotos(){
        userRef.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, Object>> data = (HashMap) dataSnapshot.getValue(Object.class);
                if (data != null) {
                    for (Map.Entry<String, HashMap<String, Object>> set :
                            data.entrySet()) {
                        usersPhotos.put(set.getKey(), (String) set.getValue().get("Photo"));
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void getReports() {
        ArrayList<UserReport> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, HashMap<String, Object>> data = (HashMap) dataSnapshot.getValue(Object.class);
                if (data != null) {
                    for (Map.Entry<String, HashMap<String, Object>> set :
                            data.entrySet()) {
                        list.add(new UserReport(set.getValue(), (String)usersPhotos.get(set.getKey()), getApplicationContext()));
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


    private void findViews() {
        userReports_BTN_back = findViewById(R.id.userReports_BTN_back);
        userReports_TXT_nameBeach = findViewById(R.id.userReports_TXT_nameBeach);
        userReports_BTN_report = findViewById(R.id.userReports_BTN_report);
        userReports_TXT_headline = findViewById(R.id.userReports_TXT_headline);
        userReports_RecycleView_reports = findViewById(R.id.userReports_RecyclerView_reports);
    }
}