package com.example.wsbapp3.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.models.PopupTextInput;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
@RunWith(AndroidJUnit4.class)
public class AssignJacketFragmentInstrumentedTest {

    private ActivityScenario<MainActivity> activityScenario;
    private PopupTextInput mockPopup;

    @Before
    public void setUp() throws Exception {
        // Launch the MainActivity
        activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> {
            // Create an instance of AssignJacketFragment and add it to the activity
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            AssignJacketFragment fragment = new AssignJacketFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragment, null);  // Use null tag for simplicity
            transaction.commitNow();
        });
        mockPopup = Mockito.mock(PopupTextInput.class);
    }

    @After
    public void tearDown() throws Exception {
        // Close the ActivityScenario after the test
        activityScenario.close();
    }

    @Test
    public void testFindJacketButtonOpensPopup() {
        // Arrange
        mockPopup = Mockito.mock(PopupTextInput.class);

        // Launch the activity and wait for fragment to resume
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        Espresso.registerIdlingResources(new CountingIdlingResource(frag)); // Replace with your idling resource
        scenario.onActivity(activity -> {
            // ... rest of your setup code
        });

        // Now access the fragment and its views
        AssignJacketFragment fragment = (AssignJacketFragment) getFragmentByTag(null); // Assuming no tag used
        fragment.findJacketButton.performClick();

        Espresso.unregisterIdlingResources();

        // Assert
        Mockito.verify(mockPopup, Mockito.times(1)).showPopupTextInput(
                Mockito.eq(fragment.requireContext()),
                Mockito.anyString(),
                Mockito.any(PopupTextInput.InputCallback.class));
    }

    // Helper method to get the fragment from the FragmentManager by tag
    private Fragment getFragmentByTag(String tag) {
        final Fragment[] fragment = new Fragment[1];
        activityScenario.onActivity(activity -> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragment[0] = fragmentManager.findFragmentByTag(tag);
        });
        return fragment[0];
    }
}
//@RunWith(AndroidJUnit4.class)
//
//public class AssignJacketFragmentInstrumentedTest {
//
//    @Before
//    public void setUp() throws Exception {
//        Class<MainActivity> mainActivityClass = MainActivity.class;
//        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(mainActivityClass);
//        scenario.onActivity(activity -> {
//            FragmentManager fragmentManager = activity.getSupportFragmentManager();
//            AssignJacketFragment fragment= new AssignJacketFragment();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.frameLayout, fragment);  // Replace with container layout ID
//            transaction.commitNow();
//        });
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//    private void setupFragmentMocks(AssignJacketFragment fragment) {
//        FragmentManager fragmentManager = Mockito.mock(FragmentManager.class);
//        FragmentTransaction fragmentTransaction = Mockito.mock(FragmentTransaction.class);
//        Mockito.when(fragment.getParentFragmentManager()).thenReturn(fragmentManager);
//        Mockito.when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
//    }
//
//    @Test
//    public void testFindJacketButtonOpensPopup() {
//        PopupTextInput mockPopup = Mockito.mock(PopupTextInput.class);
//        AssignJacketFragment fragment = new AssignJacketFragment(mockPopup);
//        setupFragmentMocks(fragment);
//        //click
//        fragment.findJacketButton.performClick();
//
//        // Assert pop up opens
//        Mockito.verify(mockPopup, Mockito.times(1)).showPopupTextInput(
//                Mockito.eq(fragment.requireContext()),
//                Mockito.anyString(),
//                Mockito.any(PopupTextInput.InputCallback.class));
//    }
//
//
//}