package com.example.wavezcellular.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.wavezcellular.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private final int ANIM_DURATION = 2000;
    private final int VIEW_SHOW_DELAY = 5000;
    private final float VIEW_ARC_START_ANGLE = 270f;
    private final float VIEW_ARC_SWEEP_ANGLE = -180f;
    private final float VIEW_ARC_X_START = 0f;
    private final float VIEW_ARC_Y_START = 0f;
    private final float VIEW_ARC_X_END_DIVISOR = 2f;
    private final float VIEW_ARC_Y_END_DIVISOR = 3f;
    private final float VIEW_ANIMATION_VOLUME = 1.0f;
    private final boolean VIEW_ARC_LARGE = true;

    private boolean hasPermission;
    private Context context;
    private ImageView logo;
    private LottieAnimationView lottie, lottie2;

    /**
     * This activity displays a splash screen with the logo.
     * The logo slides down from the top of the screen and the animations play in the background.
     * After a set time, the activity finishes and the MenuActivity is launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.context = this.getApplicationContext();
        logo = findViewById(R.id.logo);
        logo.setVisibility(View.INVISIBLE);
        lottie = findViewById(R.id.lottie);
        lottie2 = findViewById(R.id.lottie2);
        showViewSlideDown(logo);
    }

    /**
     * Animates a view sliding down from the top of the screen along an arc path.
     * After the animation finishes, checks for permission and starts the MenuActivity if permission is granted.
     */
    public void showViewSlideDown(final View v) {
        v.setVisibility(View.VISIBLE);
        Path path = new Path();
        MediaPlayer player = MediaPlayer.create(this.context, R.raw.wave);
        player.setLooping(false); // Set looping
        player.setVolume(VIEW_ANIMATION_VOLUME, VIEW_ANIMATION_VOLUME);
        player.start();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        // Calculate the arc path based on the screen size
        path.arcTo(
                VIEW_ARC_X_START,
                VIEW_ARC_Y_START,
                width / VIEW_ARC_X_END_DIVISOR,
                height / VIEW_ARC_Y_END_DIVISOR,
                VIEW_ARC_START_ANGLE,
                VIEW_ARC_SWEEP_ANGLE,
                VIEW_ARC_LARGE
        );
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.X, View.Y, path);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            animator.cancel();
            player.stop();
            checkPermission();
        }, VIEW_SHOW_DELAY);
    }

    /**
     * Starts the MenuActivity and finishes this activity.
     */
    private void startMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkPermission() {
        int fineLocationStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((fineLocationStatus != PackageManager.PERMISSION_GRANTED) || (coarseLocationStatus != PackageManager.PERMISSION_GRANTED)) {
            hasPermission = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            hasPermission = true;
            startMenuActivity(); // Permission is already granted, replace the activity
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
                startMenuActivity(); // Start the MenuActivity after permission is granted
            } else {
                hasPermission = false;
                finish();
            }
        }
    }
}
