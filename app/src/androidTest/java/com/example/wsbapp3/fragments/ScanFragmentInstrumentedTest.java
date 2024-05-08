package com.example.wsbapp3.fragments;

import static android.text.InputType.TYPE_CLASS_TEXT;

import androidx.fragment.app.Fragment;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static androidx.core.util.Predicate.not;
import static androidx.core.util.SparseBooleanArrayKt.containsKey;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isFocusable;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isFocused;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.android.material.datepicker.CompositeDateValidator.allOf;
import static com.google.common.base.CharMatcher.any;
import static com.google.firebase.firestore.Filter.equalTo;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.database.JacketProvider;
import com.example.wsbapp3.database.TicketProvider;
import com.example.wsbapp3.models.Jacket;
import com.example.wsbapp3.models.PopupTextInput;
import com.example.wsbapp3.models.Ticket;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*Tests for the AssignJacketFragment*/
@RunWith(AndroidJUnit4.class)
public class ScanFragmentInstrumentedTest {
    @Before
    public void setUp() {
        //Launch activity and fragment within it
        ActivityScenario.launch(MainActivity.class);
        FragmentScenario.launchInContainer(ScanFragment.class);
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /*Test correct UI display*/
    @Test
    public void testFindTicketButtonDisplayed() {
        onView(withId(R.id.findTicketButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testScanTicketButtonDisplayed() {
        onView(withId(R.id.scanTicketButton)).check(matches(isDisplayed()));
    }
    /*Tests if clicking Scan Ticket launches zxing scanner*/
    @Test
    public void testScanTicketButtonClick_launchesScanner() {
        //simulate scan button click
        onView(withId(R.id.scanTicketButton)).perform(click());

        // Verify scan is initiated
        intended(hasAction("com.google.zxing.client.android.SCAN"));

    }
    /*tests if clicking Find Ticket Manually launches a pop-up text input dialogue*/
    @Test
    public void testHandleFindTicketButtonClick_launchesPopup() {
        //simulate button click
        onView(withId(R.id.findTicketButton)).perform(click());

        //verify popup dialog visible
        onView(withText("Enter Ticket Id"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }
}