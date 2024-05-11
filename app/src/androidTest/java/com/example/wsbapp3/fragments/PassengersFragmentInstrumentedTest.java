package com.example.wsbapp3.fragments;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import androidx.test.ext.junit.runners.AndroidJUnit4;
@RunWith(RobolectricTestRunner.class)
public class PassengersFragmentInstrumentedTest {
    FirebaseFirestore mockFirestore;
    PassengersFragment fragment;

    @Before
    public void setUp() {
        // Mock Firestore behavior
        mockFirestore = Mockito.mock(FirebaseFirestore.class);
        // Define mock behavior for queries (success, empty, error)
        fragment = new PassengersFragment();
        fragment.setDb(mockFirestore);
    }

//    private List<QueryDocumentSnapshot> createMockDocuments(String journeyId) {
//        // Create mock documents with child data
//        List<QueryDocumentSnapshot> mockDocs = new ArrayList<>();
//        // ... (add mock documents)
//        return mockDocs;
//    }
}

