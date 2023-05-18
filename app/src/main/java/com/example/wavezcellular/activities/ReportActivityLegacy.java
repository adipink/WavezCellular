package com.example.wavezcellular.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wavezcellular.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportActivityLegacy extends AppCompatActivity {
    private TextView report_TXT_nameBeach;
    private MaterialButton report_BTN_back;
    private ImageView report_IMG_profile;
    private CardView record_CRD_density,report_CRD_wave,record_CRD_jellyfish,record_CRD_temperature,record_CRD_flag,report_CRD_wind;

    private Bundle bundle;
    private String BeachName;


    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;

    private AlertDialog alertDialog;

    //density
    private String density = "";
    ImageView density_IMG_toocrowded,density_IMG_fewpeople,density_IMG_empty;
    TextView density_TXT_toocrowded,density_TXT_fewpeople,density_TXT_empty;
    LinearLayout density_LinLay_toocrowded,density_LinLay_fewpeople,density_LinLay_empty;
    MaterialButton density_BTN_cancel,density_BTN_ok;

    //flag
    private String flag;
    ImageView flag_IMG_red,flag_IMG_white,flag_IMG_black;
    TextView flag_TXT_red,flag_TXT_white,flag_TXT_black;
    LinearLayout flag_LinLay_red,flag_LinLay_white,flag_LinLay_black;
    MaterialButton flag_BTN_cancel,flag_BTN_ok;

    //temperature
    private String temperature;
    private MaterialButton temperature_BTN_toohot,temperature_BTN_hot,temperature_BTN_pleasant,
            temperature_BTN_cold,temperature_BTN_misty,temperature_BTN_windy,
            temperature_BTN_cancel,temperature_BTN_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

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
        report_TXT_nameBeach.setText(""+ BeachName);
    }

    private void createListeners() {
        report_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivityLegacy.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        report_IMG_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivityLegacy.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        record_CRD_density.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDensity();
            }
        });
        report_CRD_wave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertWave(view);
            }
        });
        record_CRD_jellyfish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertJellyfish(view);
            }
        });
        report_CRD_wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertWind(view);
            }
        });
        record_CRD_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertFlag();
            }
        });
        record_CRD_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertTemperature();
            }
        });
    }

    private void createAlertTemperature() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View temperatureView = inflater.inflate(R.layout.alert_temperature,null);
        dialogBuilder.setView(temperatureView);

        TextView temperature_TXT_title = (TextView) temperatureView.findViewById(R.id.temperature_TXT_title);

        temperature_BTN_toohot = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_toohot);
        temperature_BTN_hot = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_hot);
        temperature_BTN_pleasant = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_pleasant);
        temperature_BTN_cold = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_cold);
        temperature_BTN_misty = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_misty);
        temperature_BTN_windy = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_windy);
        temperature_BTN_cancel = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_cancel);
        temperature_BTN_ok = (MaterialButton) temperatureView.findViewById(R.id.temperature_BTN_ok);

        alertDialog = dialogBuilder.create();

        int width = (int)(getResources().getDisplayMetrics().widthPixels*1);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);

        alertDialog.show();
        alertDialog.getWindow().setLayout(width, height);
        createTemperatureListeners();
    }

    private void createTemperatureListeners() {
        temperature_BTN_toohot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temperature = "The beach is too hot";
            }
        });
        temperature_BTN_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temperature = "The beach is hot";
            }
        });
        temperature_BTN_pleasant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temperature = "The beach is pleasant";
            }
        });
        temperature_BTN_cold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temperature = "The beach is cold";
            }
        });
        temperature_BTN_misty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temperature = "The beach is misty";
            }
        });
        temperature_BTN_windy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temperature = "The beach is windy";
            }
        });

        temperature_BTN_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(BeachName).child("temperature").setValue(temperature);
                alertDialog.dismiss();
            }
        });
        temperature_BTN_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void createAlertFlag() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View flagView = inflater.inflate(R.layout.alert_flag,null);
        dialogBuilder.setView(flagView);

        flag_IMG_red = (ImageView) flagView.findViewById(R.id.flag_IMG_red);
        flag_IMG_white = (ImageView) flagView.findViewById(R.id.flag_IMG_white);
        flag_IMG_black = (ImageView) flagView.findViewById(R.id.flag_IMG_black);

        flag_TXT_red = (TextView) flagView.findViewById(R.id.flag_TXT_red);
        flag_TXT_white = (TextView) flagView.findViewById(R.id.flag_TXT_white);
        flag_TXT_black = (TextView) flagView.findViewById(R.id.flag_TXT_black);

        flag_LinLay_red = (LinearLayout) flagView.findViewById(R.id.flag_LinLay_red);
        flag_LinLay_white = (LinearLayout) flagView.findViewById(R.id.flag_LinLay_white);
        flag_LinLay_black = (LinearLayout) flagView.findViewById(R.id.flag_LinLay_black);

        flag_BTN_ok = (MaterialButton) flagView.findViewById(R.id.flag_BTN_ok);
        flag_BTN_cancel = (MaterialButton) flagView.findViewById(R.id.flag_BTN_cancel);

        alertDialog = dialogBuilder.create();

        int width = (int)(getResources().getDisplayMetrics().widthPixels*1);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);

        alertDialog.show();
        alertDialog.getWindow().setLayout(width, height);
        createFlagListeners();
    }

    private void createFlagListeners() {
        flag_IMG_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_LinLay_red.setBackgroundColor(Color.parseColor("#597af2"));
                flag_LinLay_white.setBackgroundColor(Color.parseColor("#F7ECDE"));
                flag_LinLay_black.setBackgroundColor(Color.parseColor("#F7ECDE"));
                flag = "The flag on the beach is red";
            }
        });
        flag_IMG_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_LinLay_red.setBackgroundColor(Color.parseColor("#F7ECDE"));
                flag_LinLay_white.setBackgroundColor(Color.parseColor("#597af2"));
                flag_LinLay_black.setBackgroundColor(Color.parseColor("#F7ECDE"));
                flag = "The flag on the beach is white";
            }
        });
        flag_IMG_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_LinLay_red.setBackgroundColor(Color.parseColor("#F7ECDE"));
                flag_LinLay_white.setBackgroundColor(Color.parseColor("#F7ECDE"));
                flag_LinLay_black.setBackgroundColor(Color.parseColor("#597af2"));
                flag = "The flag on the beach is black";
            }
        });

        myRef.child(BeachName).child("flag").setValue(flag);

        flag_BTN_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(BeachName).child("flag").setValue(flag);
                alertDialog.dismiss();
            }
        });
        flag_BTN_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void createAlertWind(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle("Wind's Report: ");
        final EditText input = new EditText(view.getContext());
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                myRef.child(BeachName).child("wind").setValue(input.getText().toString());
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void createAlertJellyfish(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle("Jellyfish's Report: ");
        final EditText input = new EditText(view.getContext());
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                myRef.child(BeachName).child("jellyfish").setValue(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void createAlertWave(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle("Wave's Report: ");
        final EditText input = new EditText(view.getContext());
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                myRef.child(BeachName).child("wave").setValue(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }


    //density
    private void createAlertDensity() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View densityView = inflater.inflate(R.layout.alert_density,null);
        dialogBuilder.setView(densityView);
        density_IMG_toocrowded = (ImageView) densityView.findViewById(R.id.density_IMG_toocrowded);
        density_IMG_fewpeople = (ImageView) densityView.findViewById(R.id.density_IMG_fewpeople);
        density_IMG_empty = (ImageView) densityView.findViewById(R.id.density_IMG_empty);

        density_TXT_toocrowded = (TextView) densityView.findViewById(R.id.density_TXT_toocrowded);
        density_TXT_fewpeople = (TextView) densityView.findViewById(R.id.density_TXT_fewpeople);
        density_TXT_empty = (TextView) densityView.findViewById(R.id.density_TXT_empty);

        density_LinLay_toocrowded = (LinearLayout) densityView.findViewById(R.id.density_LinLay_toocrowded);
        density_LinLay_fewpeople = (LinearLayout) densityView.findViewById(R.id.density_LinLay_fewpeople);
        density_LinLay_empty = (LinearLayout) densityView.findViewById(R.id.density_LinLay_empty);

        density_BTN_ok = (MaterialButton) densityView.findViewById(R.id.density_BTN_ok);
        density_BTN_cancel = (MaterialButton) densityView.findViewById(R.id.density_BTN_cancel);

        alertDialog = dialogBuilder.create();

        int width = (int)(getResources().getDisplayMetrics().widthPixels*1);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);

        alertDialog.show();
        alertDialog.getWindow().setLayout(width, height);
        createDensityListeners();
    }

    //density
    private void createDensityListeners() {
       density_IMG_toocrowded.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               density_LinLay_toocrowded.setBackgroundColor(Color.parseColor("#597af2"));
               density_LinLay_fewpeople.setBackgroundColor(Color.parseColor("#F7ECDE"));
               density_LinLay_empty.setBackgroundColor(Color.parseColor("#F7ECDE"));
               density = "The beach is too Crowded";
           }
       });
        density_IMG_fewpeople.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               density_LinLay_toocrowded.setBackgroundColor(Color.parseColor("#F7ECDE"));
               density_LinLay_fewpeople.setBackgroundColor(Color.parseColor("#597af2"));
               density_LinLay_empty.setBackgroundColor(Color.parseColor("#F7ECDE"));
               density = "There are few people on the beach";
           }
       });
        density_IMG_empty.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               density_LinLay_toocrowded.setBackgroundColor(Color.parseColor("#F7ECDE"));
               density_LinLay_fewpeople.setBackgroundColor(Color.parseColor("#F7ECDE"));
               density_LinLay_empty.setBackgroundColor(Color.parseColor("#597af2"));
               density = "The beach is empty";
           }
       });

        density_BTN_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(BeachName).child("density").setValue(density);
                alertDialog.dismiss();
            }
        });
        density_BTN_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    private void findViews() {
        report_BTN_back = findViewById(R.id.report_BTN_back);
        report_IMG_profile = findViewById(R.id.report_IMG_profile);
        record_CRD_density = findViewById(R.id.record_CRD_density);
        report_CRD_wave = findViewById(R.id.report_CRD_wave);
        record_CRD_jellyfish = findViewById(R.id.record_CRD_jellyfish);
        record_CRD_temperature = findViewById(R.id.record_CRD_temperature);
        record_CRD_flag = findViewById(R.id.record_CRD_flag);
        report_CRD_wind = findViewById(R.id.report_CRD_wind);
        report_TXT_nameBeach = findViewById(R.id.report_TXT_nameBeach);

    }
}