package com.example.wsbapp3.fragments;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.android.material.datepicker.CompositeDateValidator.allOf;
import static com.google.common.base.CharMatcher.any;
import static com.google.firebase.firestore.Filter.equalTo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.database.JacketProvider;
import com.example.wsbapp3.database.TicketProvider;
import com.example.wsbapp3.models.Jacket;
import com.example.wsbapp3.models.PopupTextInput;
import com.example.wsbapp3.models.Ticket;
import com.google.zxing.integration.android.IntentIntegrator;

@RunWith(AndroidJUnit4.class)
public class AssignJacketFragmentInstrumentedTest {

    @Mock
    FragmentManager mockFragmentManager;

    @Mock
    FragmentTransaction mockFragmentTransaction;

    @Before
    public void setUp() {
        ActivityScenario.launch(MainActivity.class);
        FragmentScenario.launchInContainer(AssignJacketFragment.class);
        MockitoAnnotations.initMocks(this);

        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }


    @Test
    public void testFindJacketButtonDisplayed() {
        onView(withId(R.id.findJacketButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testScanJacketButtonDisplayed() {
        onView(withId(R.id.scanJacketButton)).check(matches(isDisplayed()));
    }

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
        intended(hasAction("com.google.zxing.client.android.SCAN"));

    }

    @Test
    public void testLoadScanAgainFragment() {
        // Mock the ScanAgainFragment
        ScanAgainFragment mockScanAgainFragment = mock(ScanAgainFragment.class);

        // Create an instance of AssignJacketFragment
        AssignJacketFragment fragment = new AssignJacketFragment();

        // Set the mock FragmentManager
        fragment.setFragmentManager(mockFragmentManager);

        // Stub the beginTransaction method of the mock FragmentManager to return mockFragmentTransaction
        when(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction);

        // Simulate that the fragment is attached to an activity
        when(fragment.isAdded()).thenReturn(true);

        // Call the loadScanAgainFragment method
        fragment.loadScanAgainFragment("mockJacketId", "mockTicketId");

        // Verify that the replace method of mockFragmentTransaction is called with the correct parameters
        verify(mockFragmentTransaction).replace(eq(R.id.frameLayout), eq(mockScanAgainFragment));

        // Verify that commit method of mockFragmentTransaction is called
        verify(mockFragmentTransaction).commit();
    }
}