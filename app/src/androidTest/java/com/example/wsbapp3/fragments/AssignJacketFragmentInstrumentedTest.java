package com.example.wsbapp3.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.android.material.internal.ContextUtils.getActivity;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.database.JacketProvider;
import com.example.wsbapp3.database.TicketProvider;
import com.example.wsbapp3.models.PopupTextInput;
import com.example.wsbapp3.models.Ticket;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowDialog;

import java.lang.reflect.Method;

@RunWith(RobolectricTestRunner.class)
public class AssignJacketFragmentInstrumentedTest {

    private ActivityScenario<MainActivity> activityScenario;

    MainActivity mainActivity;

    AssignJacketFragment fragment;
    private PopupTextInput mockPopup;

    String jacketId = "12345";
    String ticketId = "ticket123";
    String childId = "child123";
    String journeyId = "journey456";


    @Before
    public void setUp() {
        // Create a new instance of the fragment
        fragment = new AssignJacketFragment();

        // Start Fragment transaction
        FragmentActivity activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Add fragment to  container (R.id.frameLayout)
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @After
    public void tearDown() throws Exception {
        // Close the ActivityScenario after the test
        activityScenario.close();
    }


    @Test
    public void testHandleFindJacketButtonClick_launchesPopup_Robolectric() {
        fragment = new AssignJacketFragment();
        FragmentActivity activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();

        //find button
        Button findJacketButton = fragment.getView().findViewById(R.id.findJacketButton);
        //click button
        findJacketButton.performClick();

        //Verify dialog appears
        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
        assertTrue(dialog.isShowing());
        onView(withText("Enter Jacket Id")).inRoot(isDialog()).check(matches(isDisplayed()));
    }

}

//    @Test
//    public void testQRCodeScanning() throws Exception {
//
//        // Mock the IntentIntegrator class using Mockito to simulate the scan result
//        IntentIntegrator mockIntentIntegrator = Mockito.mock(IntentIntegrator.class);
//        IntentResult mockResult = new IntentResult(IntentIntegrator.REQUEST_CODE).setContents("12345678"); // Mock scanned jacket ID
//        Mockito.when(mockIntentIntegrator.parseActivityResult(anyInt(), anyInt(), any(Intent.class)))
//                .thenReturn(mockResult);
//
//        // **Corrected FragmentScenario usage**
//        FragmentScenario<AssignJacketFragment> fragmentScenario = FragmentScenario.launch(AssignJacketFragment.class);
//
//        // Set up the fragment to use the mocked IntentIntegrator
//        fragmentScenario.onFragment(fragment -> {
//            // Replace the real IntentIntegrator with the mock
//            fragment.setIntegrator(mockIntentIntegrator);
//
//            // Trigger the click event on the "Scan Jacket" button
//            fragment.getView().findViewById(R.id.scanJacketButton).performClick();
//        });
//
//        // Verify that the IntentIntegrator was initiated with the correct settings
//        Mockito.verify(mockIntentIntegrator).setPrompt("Scan Ticket QR Code");
//        Mockito.verify(mockIntentIntegrator).setOrientationLocked(true);
//        Mockito.verify(mockIntentIntegrator).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//        Mockito.verify(mockIntentIntegrator, times(1)).initiateScan(); // Called only once
//    }
//    @Test
//    public void testHandleFindJacketButtonClick_launchesPopup() {
//
//        // Create a new fragment instance
//        AssignJacketFragment fragment = new AssignJacketFragment();
//
//        // Mock the PopupTextInput class (optional, but improves isolation)
//        PopupTextInput mockPopup = Mockito.mock(PopupTextInput.class);
//
//        // Assign the mock popup to the fragment (if possible)
//        // This step depends on how the fragment interacts with the PopupTextInput
//        // If there's no setter or direct assignment, you might need to skip this
//
//        // Trigger the button click
//        fragment.handleFindJacketButtonClick();
//
//        // Verify that the showPopupTextInput method was called on the popup (or fragment)
//        Mockito.verify(mockPopup, times(1)).showPopupTextInput(any(Context.class), anyString(), any(PopupTextInput.InputCallback.class));
//    }
//    @Test
//    public void testHandleFindJacketButtonClick_fetchesJacketAndChecksForDropOff() throws Exception {
//        // Prepare mock data
//        String expectedJacketId = "12345";
//        String ticketId = "ticket123";
//        Ticket mockTicket = Mockito.mock(Ticket.class);
//        Mockito.when(mockTicket.isPickUp()).thenReturn(true); // Simulate drop-off scenario
//
//        // Mock TicketProvider behavior
//        Mockito.when(fragment.ticketProvider.fetchTicketById(ticketId, Mockito.any()))
//                .thenAnswer(invocation -> {
//                    TicketProvider.FetchTicketCallback callback = invocation.getArgument(1);
//                    callback.onTicketFetched(mockTicket);
//                    return null;
//                });
//
//        // Trigger the button click
//        fragment.handleFindJacketButtonClick();
//
//        // Verify interaction with JacketProvider for fetching the jacket
//        Mockito.verify(fragment.jacketProvider).fetchJacketById(expectedJacketId, Mockito.any());
//    }
//
//    @Test
//    public void testCheckCorrectJacket_correctJacketForDropOff_loadsScanAgainFragment() throws Exception {
//        // Prepare mock data
//        String jacketId = "12345";
//        String ticketId = "ticket123";
//        String childId = "child123";
//        String journeyId = "journey456";
//
//        // Mock Ticket and DocumentSnapshot behavior
//        Ticket mockTicket = Mockito.mock(Ticket.class);
//        Mockito.when(mockTicket.isPickUp()).thenReturn(false); // Simulate drop-off scenario
//        Mockito.when(mockTicket.getChildId()).thenReturn(childId);
//        Mockito.when(mockTicket.getJourneyId()).thenReturn(journeyId);
//
//        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
//        Mockito.when(mockDocumentSnapshot.exists()).thenReturn(true);
//        Mockito.when(mockDocumentSnapshot.getString("Jacket")).thenReturn(jacketId);
//
//        // Mock TicketProvider and Firestore behavior
//        Mockito.when(fragment.ticketProvider.fetchTicketById(ticketId, Mockito.any()))
//                .thenAnswer(invocation -> {
//                    TicketProvider.FetchTicketCallback callback = invocation.getArgument(1);
//                    callback.onTicketFetched(mockTicket);
//                    return null;
//                });
//
//        Mockito.when(fragment.db.collection("Journeys").document(journeyId)
//                        .collection("Passengers").document(childId).get())
//                .thenReturn(Task.completedTask(mockDocumentSnapshot));
//
//        // Fragment under test
//        AssignJacketFragment fragment = new AssignJacketFragment();
//        FragmentManager fragmentManager = Mockito.mock(FragmentManager.class);
//        FragmentTransaction fragmentTransaction = Mockito.mock(FragmentTransaction.class);
//        fragment.getParentFragmentManager = () -> fragmentManager;
//        Mockito.when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
//
//        // Call the method with mocked data
//        fragment.checkCorrectJacket(jacketId, ticketId);
//
//        // Verify FragmentTransaction for loading ScanAgainFragment
//        Mockito.verify(fragmentTransaction).replace(R.id.frameLayout, Mockito.any(ScanAgainFragment.class));
//        Mockito.verify(fragmentTransaction).commit();
//    }
//
//    @Test
//    public void testCheckCorrectJacket_incorrectJacketForDropOff_showsToast() throws Exception {
//        // Prepare mock data
//        String jacketId = "12345";
//        String ticketId = "ticket123";
//        String childId = "child123";
//        String journeyId = "journey456";
//
//        // Mock Ticket and DocumentSnapshot behavior
//        Ticket mockTicket = Mockito.mock(Ticket.class);
//        Mockito.when(mockTicket.isPickUp()).thenReturn(false); // Simulate drop-off scenario