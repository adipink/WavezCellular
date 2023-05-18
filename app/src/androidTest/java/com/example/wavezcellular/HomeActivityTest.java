package com.example.wavezcellular;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wavezcellular.activities.HomeActivity;
import com.example.wavezcellular.activities.SplashActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Test
    public void testHomeActivity() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = ActivityScenario.launch(HomeActivity.class);

        // Close the ActivityScenario
        activityScenario.close();
    }
}
