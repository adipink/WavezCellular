package com.example.wavezcellular;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wavezcellular.activities.SplashActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    @Test
    public void logoAppearsTest() throws InterruptedException {
        // Launch the SplashActivity
        ActivityScenario<SplashActivity> activityScenario = ActivityScenario.launch(SplashActivity.class);

        // Verify that the Splash Activity is displayed for at least 2 seconds
        Thread.sleep(2000);
        onView(withId(R.id.logo)).check(matches(isDisplayed()));

        // Close the ActivityScenario
        activityScenario.close();
    }
}
