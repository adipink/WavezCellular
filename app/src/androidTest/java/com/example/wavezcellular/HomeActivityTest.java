package com.example.wavezcellular;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.TestCase.assertNotNull;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wavezcellular.activities.HomeActivity;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Test
    public void testHomeActivityStart() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.home_SP_listOfBeaches))
                .check(matches(isDisplayed()));

        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testSelectBeach() {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = ActivityScenario.launch(HomeActivity.class);
        try {
            // Wait for the RecyclerView to be ready (adjust the sleep time as needed)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.home_RecyclerView_rec))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.show_RB_review))
                .check(matches(isDisplayed()));
        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testBackButton() {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = ActivityScenario.launch(HomeActivity.class);
        onView(ViewMatchers.withId(R.id.home_BTN_back))
                .perform(click());
        onView(withId(R.id.menu_BTN_searchBeach))
                .check(matches(isDisplayed()));
        // Close the ActivityScenario
        activityScenario.close();
    }

}
