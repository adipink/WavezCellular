package com.example.wavezcellular.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wavezcellular.R;

public class VideoTransitionActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private VideoView videoView;
    private boolean hasPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_transition);

        videoView = findViewById(R.id.videoView);

        // Load the video from the raw resources
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wavevideo);
        videoView.setVideoURI(videoUri);

        // Set loop behavior for the video
        videoView.setOnCompletionListener(mp -> {
            if (hasPermission) {
                // Video playback finished and permission is granted, start the next activity
                startNextActivity();
            } else {
                // Request location permission
                requestLocationPermission();
            }
        });

        // Start playing the video
        videoView.start();

        // Check location permission
        checkPermission();
    }

    private void checkPermission() {
        int fineLocationStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fineLocationStatus != PackageManager.PERMISSION_GRANTED && coarseLocationStatus != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission granted, set the flag
            hasPermission = true;
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if location permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, set the flag and start the next activity
                hasPermission = true;
                startNextActivity();
            } else {
                // Permission not granted, continue looping the video
                videoView.start();
            }
        }
    }

    private void startNextActivity() {
        // Start the next activity
        Intent intent = new Intent(VideoTransitionActivity.this, MenuActivity.class);
        startActivity(intent);


        // Finish the current activity
        finish();
    }
}
