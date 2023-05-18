package com.example.wavezcellular.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wavezcellular.R;
import com.example.wavezcellular.fragments.MapFragment;
import com.example.wavezcellular.utils.Beach;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivityLegacy extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener{

    private ImageView home_IMG_profile, home_IMG_search;
    private MaterialButton home_BTN_show, home_BTN_report;
    private Spinner home_SP_listOfBeaches;
    private ArrayAdapter<CharSequence> adapter;

    //firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;

    //map
    private boolean hasPremission;
    private MapFragment mapFragment;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GoogleMap googleMap;

    private String beachName;
    private Bundle bundle = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (bundle == null){
            bundle = new Bundle();
        }
        findViews();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Beaches");

        loadMapFragment();
        createSpinner();
        checkPermission();
        createListener();

        if (hasPremission) {
            if (checkGooglePlayServices()) {
                SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.home_FRL_map, supportMapFragment).commit();
                supportMapFragment.getMapAsync(this);
                checkLocation();
            } else {
                Toast.makeText(this, "Google Play services Unavaliable", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void beachesInfo(Beach beach) {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(beach.getName())) {
                    //do nothing
                }else{
                    myRef.child(beach.getName()).setValue(beach);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createListener(){
        home_IMG_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity("Profile");
            }
        });

        home_BTN_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity("Report");
            }
        });

        home_BTN_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity("Show");
            }
        });
    }

    private void replaceActivity(String mode) {
        Intent intent;
        if(mode.equals("Profile")){
            intent = new Intent(this, UserActivity.class);
            startActivity(intent);
            finish();
        }else if(mode.equals("Report")){
            intent = new Intent(this, ReportActivity.class);
            bundle.putString("BEACH_NAME",beachName);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }else{
            intent = new Intent(this, ShowActivity.class);
            bundle.putString("BEACH_NAME",beachName);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

    }

    private void createSpinner() {
        adapter = ArrayAdapter.createFromResource(this,R.array.beaches, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        home_SP_listOfBeaches.setAdapter(adapter);
        home_SP_listOfBeaches.setOnItemSelectedListener(this);


    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(HomeActivityLegacy.this, "User Canceled Dialog", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }

        return false;
    }

    private void loadMapFragment() {
        //Initialize fragment
        mapFragment = new MapFragment();

        //open fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_FRL_map, mapFragment)
                .commit();

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

    private void checkLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // locationListener = location -> findLocation();
    }

    private void findViews() {
        home_IMG_profile = findViewById(R.id.home_IMG_profile);
        home_IMG_search = findViewById(R.id.home_IMG_search);
        home_BTN_show = findViewById(R.id.home_BTN_show);
        home_BTN_report = findViewById(R.id.home_BTN_report);
        home_SP_listOfBeaches = findViewById(R.id.home_SP_listOfBeaches);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        //when map is loaded
        this.googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // when clicked on map
                //Intialized marker options
                MarkerOptions markerOptions = new MarkerOptions();
                //Set position of marker
                markerOptions.position(latLng);
                //Set title of markr
                markerOptions.title(latLng.latitude+":"+latLng.longitude);
                //remove all marker
                googleMap.clear();
                //animationg to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                //add marker on map
                googleMap.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String location = adapterView.getItemAtPosition(position).toString();
        beachName = location;
        Geocoder geocoder = new Geocoder(HomeActivityLegacy.this,Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocationName(location,1);
            if(listAddress.size()>0){
                double latit = listAddress.get(0).getLatitude();
                double logi = listAddress.get(0).getLongitude();
                Beach beach = new Beach(location,latit,logi);
                beachesInfo(beach);
                LatLng latLng = new LatLng(latit,logi);
                addMarkerToMap(latLng,location);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setItemsWhenEmpty() {
        adapter = ArrayAdapter.createFromResource(this,R.array.beaches, android.R.layout.simple_spinner_item);
        for (int i=0;i<adapter.getCount();i++) {
            String location = adapter.getItem(i).toString();
            beachName = location;
            Geocoder geocoder = new Geocoder(HomeActivityLegacy.this, Locale.getDefault());
            try {
                List<Address> listAddress = geocoder.getFromLocationName(location, 1);
                if (listAddress.size() > 0) {
                    double latit = listAddress.get(0).getLatitude();
                    double logi = listAddress.get(0).getLongitude();
                    myRef.child(beachName).child("latitude").setValue(latit);
                    myRef.child(beachName).child("longitude").setValue(logi);
                    myRef.child(beachName).child("review").setValue(3);
                    myRef.child(beachName).child("name").setValue(beachName);
                    myRef.child(beachName).child("warmth").setValue(3);
                    myRef.child(beachName).child("wave").setValue(3);
                    myRef.child(beachName).child("wind").setValue(3);
                    myRef.child(beachName).child("jellyfish").setValue(3);
                    myRef.child(beachName).child("density").setValue(3);
                    myRef.child(beachName).child("dog").setValue(3);
                    myRef.child(beachName).child("flag").setValue(3);
                    myRef.child(beachName).child("accessible").setValue(3);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMarkerToMap(LatLng latlng,String location){
        MarkerOptions marker = new MarkerOptions();
        marker.title(location+"");
        marker.position(latlng);
        googleMap.addMarker(marker);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng,15);
        googleMap.animateCamera(cameraUpdate);
    }
}
