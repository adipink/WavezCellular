package com.example.wavezcellular;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;

import com.example.wavezcellular.activities.MenuActivity;

import org.junit.Rule;
import org.junit.Test;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.wavezcellular.activities.MenuActivity;
import com.example.wavezcellular.activities.SplashActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MenuActivityTest {

    @Rule
    public ActivityScenarioRule<MenuActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MenuActivity.class);
    @Test
    public void testSearchAnother() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<MenuActivity> activityScenario = activityScenarioRule.getScenario();
        // Click the "Search Another" button
        onView(withId(R.id.menu_BTN_searchBeach)).perform(click());

        // Verify that the HomeActivity is displayed
        onView(withId(R.id.home_SP_listOfBeaches)).check(matches(isDisplayed()));

        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testViewCurrent() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<MenuActivity> activityScenario = activityScenarioRule.getScenario();

        activityScenario.onActivity(menuActivity -> {
            // Call the clearUser method on the main application thread if needed
            menuActivity.runOnUiThread(menuActivity::testAction);
        });

        // Click the "Search Another" button
        onView(withId(R.id.menu_BTN_beachdetails)).perform(click());

        // Verify that the HomeActivity is displayed
        onView(withId(R.id.show_RB_review)).check(matches(isDisplayed()));

        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testLogin() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<MenuActivity> activityScenario = activityScenarioRule.getScenario();

        // Click the "Search Another" button
        onView(withId(R.id.menu_BTN_signIn)).perform(click());

        // Verify that the HomeActivity is displayed
        onView(withId(R.id.welcome_BTN_enterApp)).check(matches(isDisplayed()));

        // Verify that the HomeActivity is displayed
        onView(ViewMatchers.withId(R.id.welcome_EDT_email))
                .perform(ViewActions.typeText("xowafec498@duscore.com"));

        onView(ViewMatchers.withId(R.id.welcome_EDT_password))
                .perform(ViewActions.typeText("123456"));

        onView(ViewMatchers.withId(R.id.welcome_BTN_enterApp))
                .perform(ViewActions.click());

        // Introduce a delay to allow the application to process the navigation
        try {
            Thread.sleep(2000); // Delay of 1 second (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.home_LOGO_wavez)).check(matches(isDisplayed()));

        onView(withId(R.id.home_IMG_profile)).perform(ViewActions.click());

        onView(withId(R.id.user_BTN_signout)).check(matches(isDisplayed()));
        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testSignUp() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<MenuActivity> activityScenario = activityScenarioRule.getScenario();

        // Click the "Search Another" button
        onView(withId(R.id.menu_BTN_signUp)).perform(click());

        // Verify that the HomeActivity is displayed
        onView(withId(R.id.welcome_BTN_registerApp)).check(matches(isDisplayed()));

        // Verify that the HomeActivity is displayed
        onView(ViewMatchers.withId(R.id.welcome_EDT_name))
                .perform(ViewActions.typeText("Test"));

        long time = System.currentTimeMillis();
        // Verify that the HomeActivity is displayed
        onView(ViewMatchers.withId(R.id.welcome_EDT_email))
                .perform(ViewActions.typeText("Test" + time + "@" + "wavez.com"));
        onView(ViewMatchers.withId(R.id.welcome_EDT_password))
                .perform(ViewActions.typeText("Test123456"));

        onView(withId(R.id.welcome_BTN_registerApp)).perform(click());
        // Close the ActivityScenario
        activityScenario.close();
    }

}
