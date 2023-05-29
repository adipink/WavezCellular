package com.example.wavezcellular;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wavezcellular.activities.HomeActivity;
import com.example.wavezcellular.adapters_holders.BeachHomeAdapter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;


@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
    @Rule
    public ActivityScenarioRule<HomeActivity> activityScenarioRule = new ActivityScenarioRule<>(HomeActivity.class);
    @Test
    public void testHomeActivityStart() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = activityScenarioRule.getScenario();
        onView(withId(R.id.home_SP_listOfBeaches))
                .check(matches(isDisplayed()));

        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testSelectBeach() {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = activityScenarioRule.getScenario();
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
    public void testFilterOrder() {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = activityScenarioRule.getScenario();
        try {
            // Wait for the RecyclerView to be ready (adjust the sleep time as needed)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        activityScenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.home_RecyclerView_rec);
            BeachHomeAdapter adapter = (BeachHomeAdapter) recyclerView.getAdapter();
            List<Map.Entry<String, String>> beachList = adapter.getEntries();
            String firstItemValue = beachList.get(0).getValue();
            String secondItemValue = beachList.get(1).getValue();
            assertTrue("First item value should be smaller than the second item value",
                    (firstItemValue.compareTo(secondItemValue))<0);
        });

        onView(withId(R.id.home_BTN_switch))
                .perform(click());

        try {
            // Wait for the RecyclerView to be ready (adjust the sleep time as needed)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        activityScenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.home_RecyclerView_rec);
            BeachHomeAdapter adapter = (BeachHomeAdapter) recyclerView.getAdapter();
            List<Map.Entry<String, String>> beachList = adapter.getEntries();
            String firstItemValue = beachList.get(0).getValue();
            String secondItemValue = beachList.get(1).getValue();
            assertTrue("First item value should be bigger than the second item value",
                    (firstItemValue.compareTo(secondItemValue))>0);
        });
        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testFilterByCategory() {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = activityScenarioRule.getScenario();
        try {
            // Wait for the RecyclerView to be ready (adjust the sleep time as needed)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.home_SP_listOfBeaches)).perform(click());

        // Assuming you have an array of values for the spinner items
        String selectedItemValue = "Name of the Beach";

        // Find and select the desired item based on its value
        onView(withText(selectedItemValue))
                .perform(click());

        try {
            // Wait for the RecyclerView to be ready (adjust the sleep time as needed)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        activityScenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.home_RecyclerView_rec);
            BeachHomeAdapter adapter = (BeachHomeAdapter) recyclerView.getAdapter();
            List<Map.Entry<String, String>> beachList = adapter.getEntries();
            String firstItemName = beachList.get(0).getKey();
            String secondItemName = beachList.get(1).getKey();
            assertTrue("First item value should be smaller than the second item value",
                    (firstItemName.compareTo(secondItemName))<0);
        });
        // Close the ActivityScenario
        activityScenario.close();
    }

    @Test
    public void testNameComparison() {
        // Launch the HomeActivity
        ActivityScenario<HomeActivity> activityScenario = activityScenarioRule.getScenario();
        try {
            // Wait for the RecyclerView to be ready (adjust the sleep time as needed)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String nameToFind = "Bugrashov";
        onView(ViewMatchers.withId(R.id.home_EditTXT_byName))
                .perform(ViewActions.typeText(nameToFind));
        activityScenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.home_RecyclerView_rec);
            BeachHomeAdapter adapter = (BeachHomeAdapter) recyclerView.getAdapter();
            List<Map.Entry<String, String>> beachList = adapter.getEntries();
            String firstItemKey = beachList.get(0).getKey();
            int contains = 0;
            if(firstItemKey.contains(nameToFind))
                contains = 1;
            assertEquals("item length should be 0", 1, contains);
        });
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
