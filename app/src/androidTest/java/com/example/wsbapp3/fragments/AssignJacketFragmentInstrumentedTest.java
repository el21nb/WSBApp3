package com.example.wsbapp3.fragments;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.Times;

import static androidx.core.util.Predicate.not;
import static androidx.core.util.SparseBooleanArrayKt.containsKey;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.android.material.datepicker.CompositeDateValidator.allOf;
import static com.google.firebase.firestore.Filter.equalTo;

import android.content.ComponentName;
import android.content.Intent;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.google.zxing.integration.android.IntentIntegrator;

@RunWith(AndroidJUnit4.class)
public class AssignJacketFragmentInstrumentedTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(MainActivity.class);
        FragmentScenario.launchInContainer(AssignJacketFragment.class);
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /*Check that clicking Find Jacket launches text input popup dialog*/
    @Test
    public void testHandleFindJacketButtonClick_launchesPopup() {
        onView(withId(R.id.findJacketButton)).perform(click());
        onView(withText("Enter Jacket Id"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    /*Check that clicking scan Jacket launches zxing scanner*/
    @Test
    public void testScanJacketButtonClick_launchesScanner() {
        // Simulate click on the scan button
        onView(withId(R.id.scanJacketButton)).perform(click());

        // Verify that the IntentIntegrator class is used for initiating the scan
        intended(allOf(
                hasAction("com.google.zxing.client.android.SCAN"),
                hasAction("com.google.zxing.client.android.SCAN")

                ));
    }
}
//    @Before
//    public void setUp() {
//        // Create a new instance of the fragment
////        fragment = new AssignJacketFragment();
////
////
////        // Start Fragment transaction
////        FragmentActivity activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
////        FragmentManager fragmentManager = activity.getSupportFragmentManager();
////        FragmentTransaction transaction = fragmentManager.beginTransaction();
////
////
////        // Add fragment to  container (R.id.frameLayout)
////        transaction.add(R.id.frameLayout, fragment);
////        transaction.commit();
//    }
//
//
//    @After
//    public void tearDown() throws Exception {
//        // Close the ActivityScenario after the test
//        activityScenario.close();
//    }
//
//
//
//
//    @Test
//    public void testHandleFindJacketButtonClick_launchesPopup_Robolectric() {
//        fragment = new AssignJacketFragment();
//        FragmentActivity activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.frameLayout, fragment);
//        transaction.commit();
//
//
//        //find button
//        Button findJacketButton = fragment.getView().findViewById(R.id.findJacketButton);
//        //click button
//        findJacketButton.performClick();
//
//
//        //Verify dialog appears
//        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
//        assertTrue(dialog.isShowing());
//        onView(withText("Enter Jacket Id")).inRoot(isDialog()).check(matches(isDisplayed()));
//    }


