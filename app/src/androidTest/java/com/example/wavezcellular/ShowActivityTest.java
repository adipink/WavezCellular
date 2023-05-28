package com.example.wavezcellular;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wavezcellular.activities.MenuActivity;
import com.example.wavezcellular.activities.ReportActivity;
import com.example.wavezcellular.activities.ShowActivity;
import com.example.wavezcellular.activities.UserActivity;

import static androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static androidx.test.espresso.intent.Intents.*;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


@RunWith(AndroidJUnit4.class)
public class ShowActivityTest {
    public final String DEFAULT_BEACH = "Bugrashov beach Tel Aviv";
    private Intent intent;
    private ActivityScenario<ShowActivity> activityScenario;
    @Before
    public void setup() {
        // Initialize Intents
        Intents.init();
        // Create a new intent
        intent = new Intent(ApplicationProvider.getApplicationContext(), ShowActivity.class);

        // Create a bundle and put the extra data
        Bundle bundle = new Bundle();
        bundle.putString("BEACH_NAME", DEFAULT_BEACH);

        intent.putExtras(bundle);
    }

    @Test
    public void testShowActivityStart() {

        activityScenario = ActivityScenario.launch(intent);
        onView(ViewMatchers.withId(R.id.show_RB_review))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testBackButton() {

        activityScenario = ActivityScenario.launch(intent);

        onView(ViewMatchers.withId(R.id.show_BTN_back))
                .perform(click());

        onView(ViewMatchers.withId(R.id.home_SP_listOfBeaches))
                .check(matches(isDisplayed()));

    }
    @Ignore("This test is skipped intentionally")
    @Test
    public void testProfileButton() {

        activityScenario = ActivityScenario.launch(intent);

        onView(ViewMatchers.withId(R.id.show_IMG_profile))
                .perform(click());

        onView(ViewMatchers.withId(R.id.user_TXT_name))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testWazeButton() {
        activityScenario = ActivityScenario.launch(intent);

        // Perform button click
        onView(ViewMatchers.withId(R.id.show_BTN_waze))
                .perform(click());

        // Find the intent that contains the URL
        String url = null;
        for (Intent intent : Intents.getIntents()) {
            if (intent.getData() != null && intent.getData().toString().contains("waze.com")) {
                url = intent.getData().toString();
                break;
            }
        }

        // Assert that the URL is not null and matches the expected format
        assertNotNull(url);


    }






    @Test
    public void testMoovitButton() {
        activityScenario = ActivityScenario.launch(intent);

        // Perform button click
        onView(ViewMatchers.withId(R.id.show_BTN_moovit))
                .perform(click());

        // Find the intent that contains the URL
        String url = null;
        for (Intent intent : Intents.getIntents()) {
            if (intent.getData() != null && intent.getData().toString().contains("moovit")) {
                url = intent.getData().toString();
                break;
            }
        }

        // Assert that the URL is not null and matches the expected format
        assertNotNull(url);

    }
    @After
    public void closeTest() {
        activityScenario.close();
        Intents.release();
    }



}
