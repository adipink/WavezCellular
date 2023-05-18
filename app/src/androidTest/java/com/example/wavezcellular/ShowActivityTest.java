package com.example.wavezcellular;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.wavezcellular.activities.ShowActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ShowActivityTest {

    @Test
    public void testShowActivity() throws InterruptedException {
        // Launch the HomeActivity
        ActivityScenario<ShowActivity> activityScenario = ActivityScenario.launch(ShowActivity.class);

        // Close the ActivityScenario
        activityScenario.close();
    }
}
