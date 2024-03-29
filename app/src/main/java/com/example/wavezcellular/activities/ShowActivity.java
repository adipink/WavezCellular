package com.example.wavezcellular.activities;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.wavezcellular.Interfaces.testActionsListener;
import com.example.wavezcellular.R;
import com.example.wavezcellular.utils.ActivityManager;
import com.example.wavezcellular.utils.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Scanner;

/**
 * ShowActivity
 * Activity responsible for showing the user all data on the beach
 * The activity shows the user data on each category,
 *      and depending on the value a text describing that category at that beach
 * The activity also shows the user data on how the sky looks, the temperature and the flag in that beach.
 * The activity enables the user to pick either Waze or Moovit as transportation helpers on their way to that beach.
 * The user can also:
 *      1. go to see reports on that beach
 *      2. create a report himself if he is a signed in user
 *      3. go back to homeActivity to select another beach
 *      4. go to the userActivity
 */
public class ShowActivity extends AppCompatActivity implements testActionsListener {
    private final double HIGH_VALUE = 3.5;
    private final double LOW_VALUE = 1.5;
    private ActivityManager activityManager;
    private MaterialButton show_BTN_back, show_BTN_reports,show_BTN_create_report, show_BTN_waze, show_BTN_moovit;
    private ImageView show_IMG_profile;
    private MaterialTextView show_TXT_nameBeach, show_TXT_temperature, show_TXT_distance, show_TXT_nameCity;
    private TextView show_TXT_jellyfish, show_TXT_density, show_TXT_danger,
            show_TXT_wind, show_TXT_accessible, show_TXT_dog, show_TXT_warmth, show_TXT_hygiene;
    private ShapeableImageView show_IMG_flag, show_IMG_weather;
    private RatingBar show_RB_review;
    private Bundle bundle;
    private String BeachName;
    private double latitude;
    private double longitude;
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private DatabaseReference photoRef;
    private Context context;
    private double userLat;
    private double userLon;
    private String userID;
    private String photo;
    private boolean isGuest;
    private boolean hasPremission;
    private Resources resources;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_show);
        this.context = this.getApplicationContext();
        activityManager = new ActivityManager(this);
        resources = context.getResources();
        bundle = getIntent().getExtras();
        if (bundle != null)
            BeachName = bundle.getString("BEACH_NAME");
         else {
            this.bundle = new Bundle();
            BeachName = "";
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    userLat = location.getLatitude();
                    userLon = location.getLongitude();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userLat = User.DEFAULTLAT;
                userLon = User.DEFAULTLON;
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        isGuest = firebaseUser == null;
        myRef = FirebaseDatabase.getInstance().getReference("Beaches");

        findViews();
        createListeners();
        showInfo();
        showProfilePic();
    }

    private void showProfilePic() {
        if(isGuest){
            show_IMG_profile.setImageResource(R.drawable.ic_user);
        }else{
            userID = firebaseUser.getUid();
            photoRef = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Photo");

            photoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        photo = dataSnapshot.getValue(String.class);
                        // Use the retrieved photo URL as needed
                        int resourceId = getResources().getIdentifier(photo, "drawable", getPackageName());
                        show_IMG_profile.setImageResource(resourceId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error condition if needed
                }
            });
        }
    }

    //show data on the beach, formal and non formal stored in the beach's data table in the DB
    //also shows formal data using API on OpenWeatherMap to get -> windspeed, temperature and sky description.
    private void showInfo() {
        setBeachName(show_TXT_nameBeach, show_TXT_nameCity, BeachName);
        myRef.child(BeachName).child("Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, Object> beach = (HashMap<String, Object>) dataSnapshot.getValue();
                double review = getDouble(beach, "review");
                double warmth = getDouble(beach, "warmth");
                double wind = getDouble(beach, "wind");
                double danger = getDouble(beach, "danger");
                double accessible = getDouble(beach, "accessible");
                double density = getDouble(beach, "density");
                double jellyfish = getDouble(beach, "jellyfish");
                double hygiene = getDouble(beach, "hygiene");
                double dog = getDouble(beach, "dog");
                latitude = getDouble(beach, "latitude");
                longitude = getDouble(beach, "longitude");
                LatLng loc = new LatLng(latitude, longitude);
                LatLng user = new LatLng(userLat, userLon);
                double distance = getDistance(user, loc);
                String format = String.format("%.01f", distance);
                //show_TXT_distance.setText(format  + "km away from you");
                show_TXT_distance.setText(format  + getResources().getString(R.string.show_km));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String apiKey = resources.getString(R.string.secret_openweathermap_api);
                        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;

                        try {
                            URL url = new URL(urlString);
                            Scanner scanner = new Scanner(url.openStream());
                            String response = scanner.nextLine();
                            scanner.close();

                            JSONObject jsonObject = new JSONObject(response);
                            double temperature = (jsonObject.getJSONObject("main").getDouble("temp")) - 273.15;

                            JSONObject jsonWeather = (JSONObject) (jsonObject.getJSONArray("weather").get(0));
                            String skyDesc = jsonWeather.getString("description");

                            //Take windSpeed and convert to knots per hour
                            double windSpeed = (jsonObject.getJSONObject("wind").getDouble("speed"))*1.94384;
                            // Update UI with temperature using runOnUiThread()
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    show_TXT_temperature.setText((String.format("%.1f °C", temperature)));
                                    configureSkyIcon(skyDesc);
                                    configureDangerIcon(windSpeed);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                show_RB_review.setRating((float) review);

                if (wind > HIGH_VALUE)
                    show_TXT_wind.setText("Wind: Windy");
                else
                    show_TXT_wind.setText("Wind: OK wind");
                if (danger > HIGH_VALUE)
                    show_TXT_danger.setText("Danger: Dangerous");
                else
                    show_TXT_danger.setText("Danger: Not Dangerous");
                if (accessible > HIGH_VALUE)
                    show_TXT_accessible.setText("Accessibility: accessible");
                else
                    show_TXT_accessible.setText("Accessibility: Not accessible");
                if (density > HIGH_VALUE)
                    show_TXT_density.setText("Density: Crowded");
                else
                    show_TXT_density.setText("Density: Uncrowded");
                if (jellyfish > HIGH_VALUE)
                    show_TXT_jellyfish.setText("Jellyfish: Many Jellyfish");
                else
                    show_TXT_jellyfish.setText("Jellyfish: No Jellyfish");
                if (dog > HIGH_VALUE)
                    show_TXT_dog.setText("Pet Friendly");
                else
                    show_TXT_dog.setText("No Pets allowed");
                if (warmth > HIGH_VALUE)
                    show_TXT_warmth.setText("Warmth: It's hot");
                else if (warmth < LOW_VALUE)
                    show_TXT_warmth.setText("Warmth: It's cold");
                else
                    show_TXT_warmth.setText("Warmth: OK");
                if (hygiene > HIGH_VALUE)
                    show_TXT_hygiene.setText("Hygiene: Very Clean");
                else if (warmth < LOW_VALUE)
                    show_TXT_hygiene.setText("Hygiene: Very Dirty");
                else
                    show_TXT_hygiene.setText("Hygiene: OK");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void setBeachName(MaterialTextView viewName, MaterialTextView viewCity, String fullName) {
        String name;
        String city;
        String[] nameArray = fullName.split("beach");
        name = nameArray[0] + " Beach";
        city = nameArray[1];
        viewName.setText("" + name);
        if(viewCity != null)
            viewCity.setText("" + city);
    }

    //show different icons for different sky descriptions
    private void configureSkyIcon(String skyDesc){
        skyDesc = skyDesc.toUpperCase();
        if (skyDesc.contains("CLEAR SKY") || skyDesc.contains("FEW CLOUDS")) {
            show_IMG_weather.setImageResource(R.drawable.ic_sun);
        } else if (skyDesc.contains("SCATTERED CLOUDS") || skyDesc.contains("BROKEN CLOUDS") || skyDesc.contains("OVERCAST CLOUDS")) {
            show_IMG_weather.setImageResource(R.drawable.cloudy_day);
        } else if (skyDesc.contains("LIGHT RAIN") || skyDesc.contains("MODERATE RAIN") || skyDesc.contains("HEAVY RAIN") || skyDesc.contains("THUNDERSTORM")) {
            show_IMG_weather.setImageResource(R.drawable.rain);
        } else if (skyDesc.contains("MIST") || skyDesc.contains("FOG")) {
            show_IMG_weather.setImageResource(R.drawable.fog);
        }else
            show_IMG_weather.setImageResource(R.drawable.ic_sun);
    }

    //show different icons for different windspeeds
    private void configureDangerIcon(double windSpeed){
        if (windSpeed <= 8) {
            show_IMG_flag.setImageResource(R.drawable.ic_whiteflag);
        } else if (windSpeed > 8 && windSpeed <= 15) {
            show_IMG_flag.setImageResource(R.drawable.ic_redflag);
        } else if (windSpeed > 15) {
            show_IMG_flag.setImageResource(R.drawable.ic_blackflag);
        }
    }

    private void createListeners() {
        show_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityManager.startActivity(HomeActivity.class,bundle);
            }
        });
        show_IMG_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGuest){
                    goToWelcome();
                }else{
                    activityManager.startActivity(UserActivityUpgrade.class,bundle);
                }
            }
        });
        show_BTN_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickOnReports(); }
        });
        show_BTN_create_report.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                clickOnCreateReports();
            }
        });
        show_BTN_waze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnWaze();
            }
        });
        show_BTN_moovit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoovitDirections(latitude, longitude, BeachName);
            }
        });
    }

    //if a non signed in user clicked on "Create Report" prompt that he must sign in first
    //and enable him to do so
    private void clickOnCreateReports() {
         if(isGuest){ // user who are not registered cannot report
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
            builder.setTitle("Do you want to add a report to this beach ?");
            builder.setMessage("You need to register or login first");
            builder.setCancelable(false);
            builder.setPositiveButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });
            builder.setNegativeButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                // When the user click yes button then app will take it to the register/login page
                goToWelcome();

            });
            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        }else{
             activityManager.startActivity(ReportActivity.class,bundle);
        }
    }

    private void clickOnReports(){
        activityManager.startActivity(UserReportsActivity.class,bundle);
    }

    private void clickOnWaze() {
        try {
            String url = "https://waze.com/ul?q=" + BeachName + "&ll=" + latitude + "," + longitude + "&navigate=yes" + BeachName.replace(" ", "%20");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

    public void openMoovitDirections(double latitude, double longitude, String placeName) {
        try {
            String uri = String.format("moovit://directions?dest_lat=%f&dest_lon=%f&dest_name=%s",
                    latitude, longitude, URLEncoder.encode(BeachName, "UTF-8"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void goToWelcome(){
        bundle.putString("LOGIN_STATE", "login");
        activityManager.startActivity(WelcomeActivity.class,bundle);
    }

    private void findViews() {
        show_BTN_reports = findViewById(R.id.show_BTN_reports);
        show_BTN_create_report = findViewById(R.id.show_BTN_create_report);
        show_BTN_back = findViewById(R.id.show_BTN_back);
        show_IMG_profile = findViewById(R.id.show_IMG_profile);
        show_TXT_nameBeach = findViewById(R.id.show_TXT_nameBeach);
        show_TXT_density = findViewById(R.id.show_TXT_density);
        show_TXT_jellyfish = findViewById(R.id.show_TXT_jellyfish);
        show_TXT_temperature = findViewById(R.id.show_TXT_temperature);
        show_TXT_danger = findViewById(R.id.show_TXT_danger);
        show_TXT_wind = findViewById(R.id.show_TXT_wind);
        show_RB_review = findViewById(R.id.show_RB_review);
        show_TXT_accessible = findViewById(R.id.show_TXT_accessible);
        show_TXT_dog = findViewById(R.id.show_TXT_dog);
        show_BTN_waze = findViewById(R.id.show_BTN_waze);
        show_BTN_moovit = findViewById(R.id.show_BTN_moovit);
        show_TXT_distance = findViewById(R.id.show_TXT_distance);
        show_TXT_warmth = findViewById(R.id.show_TXT_warmth);
        show_TXT_hygiene = findViewById(R.id.show_TXT_hygiene);
        show_IMG_flag = findViewById(R.id.show_IMG_flag);
        show_IMG_weather = findViewById(R.id.show_IMG_weatherIcon);
        show_TXT_nameCity = findViewById(R.id.show_TXT_nameCity);
    }

    private double getDistance(LatLng location1, LatLng location2) {
        return (SphericalUtil.computeDistanceBetween(location1, location2) / 1000);
    }

    public static double getDouble(HashMap<String, Object> hashMap, String valueName) {
        Object o = hashMap.get(valueName);
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

    public static double getTemperature(String apikey, double lat, double lon) {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apikey + "&units=metric";

        try {
            URL url = new URL(urlString);
            Scanner scanner = new Scanner(url.openStream());
            String response = scanner.nextLine();
            scanner.close();

            JSONObject jsonObject = new JSONObject(response);
            double temperature = jsonObject.getJSONObject("main").getDouble("temp");

            return temperature;
        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }

    private void checkPermission() {
        int fineLocationStatus = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationStatus = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((fineLocationStatus != PackageManager.PERMISSION_GRANTED) &&
                (coarseLocationStatus != PackageManager.PERMISSION_GRANTED)) {
            hasPremission = true;
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    101);
        } else {
            hasPremission = true;
        }
    }

    @Override
    public void testAction() {
        BeachName = "Bugrashov beach Tel Aviv";
        bundle.putString("BEACH_NAME", BeachName);
    }
}