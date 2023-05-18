package com.example.wavezcellular.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private String user;


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
                Intent intent = new Intent(UserReportsActivity.this, ReportActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
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

    private void findViews() {
        userReports_BTN_back = findViewById(R.id.userReports_BTN_back);
        userReports_TXT_nameBeach = findViewById(R.id.userReports_TXT_nameBeach);
        userReports_BTN_report = findViewById(R.id.userReports_BTN_report);
        userReports_TXT_headline = findViewById(R.id.userReports_TXT_headline);
        userReports_RecycleView_reports = findViewById(R.id.userReports_RecyclerView_reports);
    }


}