package com.example.wavezcellular.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityManager {
    private Activity currentActivity;

   // private Bundle bundle = null;

    public ActivityManager(Activity activity){
        currentActivity = activity;
    }

    public void startActivity(Class<?> activityClass, Bundle bundle) {
        Intent intent = new Intent(currentActivity, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        currentActivity.startActivity(intent);
        finishActivity();
    }

    public void finishActivity() {
        currentActivity.finish();
    }

    public void setActivity(Activity activity) {
        currentActivity = activity;
    }
}
