package com.example.wavezcellular;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wavezcellular.activities.ReportActivity;
import com.example.wavezcellular.activities.ShowActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReportActivityTest {

    @Rule
    public ActivityScenarioRule<ReportActivity> activityScenarioRule =
            new ActivityScenarioRule<>(ReportActivity.class);

    /**
     * Test that the activity starts without any issues
     */
    @Test
    public void testReportActivityStart() {

        ActivityScenario<ReportActivity> activityScenario = activityScenarioRule.getScenario();
        // Add assertions to verify the initial state of the activity if needed
        // For example, you can check if certain views are displayed or not
        Espresso.onView(ViewMatchers.withId(R.id.report_EditTXT_comment))
                .check(matches(isDisplayed()));
        activityScenario.close();
    }

    /**
     * Test the submission of a valid report from a user
     */
    @Test
    public void testUserReportSubmit() {


        ActivityScenario<ReportActivity> activityScenario = activityScenarioRule.getScenario();
        FirebaseAuth.getInstance().signOut();
        FirebaseAuth.getInstance().signInWithEmailAndPassword("xowafec498@duscore.com","123456");
        activityScenario.onActivity(reportActivity -> {
            // Call the clearUser method on the main application thread if needed
            reportActivity.runOnUiThread(reportActivity::testAction);
        });

        // Enter a valid report comment
        String comment = "TestReportSubmit";
        Espresso.onView(ViewMatchers.withId(R.id.report_EditTXT_comment))
                .perform(ViewActions.typeText(comment));

        // Submit the report
        Espresso.onView(ViewMatchers.withId(R.id.report_BTN_submit))
                .perform(ViewActions.click());

        // Verify that the report was submitted successfully
        Espresso.onView(ViewMatchers.withId(R.id.show_BTN_reports))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(comment))
                .check(matches(isDisplayed()));
        FirebaseAuth.getInstance().signOut();
        // Close the activity scenario
        activityScenario.close();
    }


    /**
     * Test navigating back from reportActivity
     */
    @Test
    public void testNavigatingBack() {
        FirebaseAuth.getInstance().signOut();
        ActivityScenario<ReportActivity> activityScenario = activityScenarioRule.getScenario();

        activityScenario.onActivity(reportActivity -> {
            // Call the clearUser method on the main application thread if needed
            reportActivity.runOnUiThread(reportActivity::testAction);
        });

        // Perform the action that triggers the navigation back
        Espresso.onView(ViewMatchers.withId(R.id.report_BTN_back))
                .perform(ViewActions.click());

        // Introduce a delay to allow the application to process the navigation
        try {
            Thread.sleep(1000); // Delay of 1 second (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that a view in ShowActivity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.show_RB_review))
                .check(matches(isDisplayed()));

    }

    /**
     * Test navigating to profile from reportActivity
     */
    @Ignore("This test is skipped intentionally")
    @Test
    public void testNavigatingProfile() {
        ActivityScenario<ReportActivity> activityScenario = activityScenarioRule.getScenario();

        FirebaseAuth.getInstance().signOut();
        FirebaseAuth.getInstance().signInWithEmailAndPassword("xowafec498@duscore.com","123456");

        activityScenario.onActivity(reportActivity -> {
            // Call the clearUser method on the main application thread if needed
            reportActivity.runOnUiThread(reportActivity::testAction);
        });

        // Perform the action that triggers the navigation back
        Espresso.onView(ViewMatchers.withId(R.id.report_IMG_profile))
                .perform(ViewActions.click());

        // Introduce a delay to allow the application to process the navigation
        try {
            Thread.sleep(1000); // Delay of 1 second (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that a view in ShowActivity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.user_TXT_name))
                .check(matches(isDisplayed()));

    }



}



