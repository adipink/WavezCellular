package com.example.wavezcellular;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wavezcellular.activities.ReportActivity;
import com.example.wavezcellular.activities.SplashActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)


public class SplashActivityTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SplashActivity.class);
    @Test
    public void testLogoAppears() throws InterruptedException {
        // Launch the SplashActivity
        ActivityScenario<SplashActivity> activityScenario = activityScenarioRule.getScenario();
        // Verify that the Splash Activity is displayed for at least 2 seconds
        Thread.sleep(2000);
        onView(withId(R.id.logo)).check(matches(isDisplayed()));

        // Close the ActivityScenario
        activityScenario.close();
    }

/*
    @Test
    public void testPermission() throws InterruptedException {

        // Launch the SplashActivity
        ActivityScenario<SplashActivity> activityScenario = activityScenarioRule.getScenario();

        // Verify that the Splash Activity is displayed for at least 2 seconds
        Thread.sleep(2000);
        onView(withId(R.id.logo)).check(matches(isDisplayed()));

        // Close the ActivityScenario
        activityScenario.close();
    }
*/

}
