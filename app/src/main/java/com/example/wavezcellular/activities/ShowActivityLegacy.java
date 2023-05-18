package com.example.wavezcellular.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.wavezcellular.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowActivityLegacy extends AppCompatActivity {

    private MaterialButton show_BTN_back;
    private ImageView show_IMG_profile;
    private MaterialButton show_TXT_density,show_TXT_wave,show_TXT_jellyfish,
            show_TXT_flag,show_TXT_wind;
    private MaterialTextView show_TXT_nameBeach,show_TXT_temperature;

    private Bundle bundle;
    private String BeachName;


    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Beaches");

        bundle = getIntent().getExtras();
        if (bundle != null) {
            BeachName = bundle.getString("BEACH_NAME");
        } else {
            this.bundle = new Bundle();
            BeachName = "";
        }
        findViews();
        createListeners();
        showInfo();


    }

    private void showInfo() {
        show_TXT_nameBeach.setText(""+ BeachName);
        myRef.child(BeachName).child("density").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String density = dataSnapshot.getValue(String.class);
                if(density.equals(""))
                    show_TXT_density.setText("density: there are no reports");
                else
                    show_TXT_density.setText("density: "+density);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.child(BeachName).child("wave").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String wave = dataSnapshot.getValue(String.class);
                if(wave.equals(""))
                    show_TXT_wave.setText("wave: there are no reports");
                else
                    show_TXT_wave.setText("wave: "+wave);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child(BeachName).child("jellyfish").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String jellyfish = dataSnapshot.getValue(String.class);
                if(jellyfish.equals(""))
                    show_TXT_jellyfish.setText("jellyfish: there are no reports");
                else
                    show_TXT_jellyfish.setText("jellyfish: "+jellyfish);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child(BeachName).child("temperature").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String temperature = dataSnapshot.getValue(String.class);
                if(temperature.equals(""))
                    show_TXT_temperature.setText("temperature: there are no reports");
                else
                    show_TXT_temperature.setText("temperature: "+temperature);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child(BeachName).child("flag").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String flag = dataSnapshot.getValue(String.class);
                if(flag.equals(""))
                    show_TXT_flag.setText("flag: there are no reports");
                else
                    show_TXT_flag.setText("flag: "+flag);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child(BeachName).child("wind").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String wind = dataSnapshot.getValue(String.class);
                if(wind.equals(""))
                    show_TXT_wind.setText("wind: there are no reports");
                else
                    show_TXT_wind.setText("wind: "+wind);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void createListeners() {
        show_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowActivityLegacy.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews() {
        show_BTN_back = findViewById(R.id.show_BTN_back);
        show_IMG_profile = findViewById(R.id.show_IMG_profile);
        show_TXT_nameBeach = findViewById(R.id.show_TXT_nameBeach);
        show_TXT_density = findViewById(R.id.show_TXT_density);
        show_TXT_jellyfish = findViewById(R.id.show_TXT_jellyfish);
        show_TXT_temperature = findViewById(R.id.show_TXT_temperature);
        show_TXT_flag = findViewById(R.id.show_TXT_danger);
        show_TXT_wind = findViewById(R.id.show_TXT_wind);


    }
}