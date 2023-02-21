package com.example.wavezcellular.activities;

import static com.example.wavezcellular.utils.User.getGuest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wavezcellular.R;
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
    private RatingBar[] userReports_RB_reviews;
    private TextView[] userReports_TXT_blurb;
    private TextView[] userReports_TXT_username;
    private TextView userReports_TXT_headline;
    private LinearLayout[] userReports_LinearLayout_reports;
    private Bundle bundle;
    private String BeachName;
    private FirebaseUser firebaseUserUser;
    private DatabaseReference myRef;
    private String user;

    public static double getDouble(Map.Entry<String, HashMap<String, Object>> set) {
        Object o = set.getValue().get("review");
        double val;
        if (o instanceof Long) {
            Long l = (Long) o;
            if (l != null) {
                val = (double) l;
                return val;
            }
        } else if (o instanceof Double) {
            return (double) o;
        } else
            return 0;
        return 0;
    }

    public static double getDouble(Object o) {
        double val;
        if (o instanceof Long) {
            Long l = (Long) o;
            if (l != null) {
                val = (double) l;
                return val;
            }
        } else if (o instanceof Double) {
            return (double) o;
        } else
            return 0;
        return 0;
    }

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
        firebaseUserUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUserUser == null) {
            user = getGuest(bundle);
        } else
            user = firebaseUserUser.getDisplayName();
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
//        report_IMG_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(UserReportsActivity.this, UserActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();
//            }
//        });

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
                ArrayList<Map.Entry<String, HashMap<String, Object>>> list = new ArrayList<>();
                HashMap<String, HashMap<String, Object>> data = (HashMap) dataSnapshot.getValue(Object.class);
                if (data != null) {
                    for (Map.Entry<String, HashMap<String, Object>> set :
                            data.entrySet()) {
                        list.add(set);
                    }
                }
                createReports(list);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createReports(ArrayList<Map.Entry<String, HashMap<String, Object>>> list) {
        int j = 0;
        for (int i = 0; i < list.size() && j < MAX_NUM; i++) {
            String username = list.get(i).getKey();
            double review = getDouble(list.get(i));
            String blurb = (String) list.get(i).getValue().get("comment");
            if (blurb != null && blurb.length() > 0) {
                userReports_RB_reviews[j].setRating((float) review);
                userReports_TXT_username[j].setText(username);
                userReports_TXT_blurb[j].setText(blurb);
                userReports_LinearLayout_reports[j].setVisibility(View.VISIBLE);
                j++;
            }
        }
        if (j == 0)
            userReports_TXT_headline.setText("No reports with comments found");
    }

    private void findViews() {
        userReports_BTN_back = findViewById(R.id.userReports_BTN_back);
        //report_IMG_profile = findViewById(R.id.report_IMG_profile);
        userReports_TXT_nameBeach = findViewById(R.id.userReports_TXT_nameBeach);
        userReports_BTN_report = findViewById(R.id.userReports_BTN_report);
        userReports_TXT_headline = findViewById(R.id.userReports_TXT_headline);
        userReports_RB_reviews = new RatingBar[]{
                findViewById(R.id.userReports_RB_review1),
                findViewById(R.id.userReports_RB_review2),
                findViewById(R.id.userReports_RB_review3),
                findViewById(R.id.userReports_RB_review4),
                findViewById(R.id.userReports_RB_review5)

        };
        userReports_TXT_blurb = new TextView[]{
                findViewById(R.id.userReports_TXT_blurb1),
                findViewById(R.id.userReports_TXT_blurb2),
                findViewById(R.id.userReports_TXT_blurb3),
                findViewById(R.id.userReports_TXT_blurb4),
                findViewById(R.id.userReports_TXT_blurb5)

        };

        userReports_TXT_username = new TextView[]{
                findViewById(R.id.userReports_TXT_username1),
                findViewById(R.id.userReports_TXT_username2),
                findViewById(R.id.userReports_TXT_username3),
                findViewById(R.id.userReports_TXT_username4),
                findViewById(R.id.userReports_TXT_username5)

        };

        userReports_LinearLayout_reports = new LinearLayout[]{
                findViewById(R.id.userReports_LinearLayout_report1),
                findViewById(R.id.userReports_LinearLayout_report2),
                findViewById(R.id.userReports_LinearLayout_report3),
                findViewById(R.id.userReports_LinearLayout_report4),
                findViewById(R.id.userReports_LinearLayout_report5)

        };
    }
}