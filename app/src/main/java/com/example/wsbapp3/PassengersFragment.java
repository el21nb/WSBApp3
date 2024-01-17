
package com.example.wsbapp3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayAdapter<String[]> adapter;

    private List<String[]> passengerList;

    private ListView listView;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PassengersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengersFragment newInstance(String param1, String param2) {
        PassengersFragment fragment = new PassengersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        passengerList = new ArrayList<>();
        adapter = new CustomPassengerAdapter(requireContext(), R.layout.custom_passenger_row, passengerList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passengers, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Move the ListView initialization here
        listView = view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        // Now call readPassengers
        readPassengers(((MainActivity) requireActivity()).getCurrentJourneyId());
    }

    public void readPassengers(String journeyId) {
        JourneyProvider provider = new JourneyProvider();

        Log.d("RBSD", "***1 ");

        db.collection("Journeys")
                .document(journeyId)
                .collection("Passengers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            passengerList.clear();
                            for (QueryDocumentSnapshot passengerDocument : task.getResult()) {
                                String jacket = passengerDocument.getString("Jacket");
                                // Use toObject to convert the HashMap to a Child object
                                Child child = passengerDocument.get("Child", Child.class);
                                if (child != null) {
                                    String firstName = child.getFirstName();
                                    String lastName = child.getLastName();
                                    String id = child.getId();

                                    String[] itemString = new String[2];
                                    itemString[0] = firstName +" "+ lastName + " (" + id + " )";
                                    itemString[1] = "Jacket: " + jacket;
                                    passengerList.add(itemString);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("RBSD", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }



}
//package com.example.wsbapp3;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toolbar;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link PassengersFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class PassengersFragment extends Fragment  {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private ArrayAdapter<String[]> adapter;
//
//    private List<String[]> passengerList;
//
//    private ListView list;
//
//
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    public PassengersFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PassengersFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static PassengersFragment newInstance(String param1, String param2) {
//        PassengersFragment fragment = new PassengersFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        list = findViewbyId(R.id.list);
//        passengerList = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, passengerList);
//        list.setAdapter(adapter);
//        readPassengers(((MainActivity)getActivity()).getCurrentJourneyId());
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v =  inflater.inflate(R.layout.fragment_passengers, container, false);
//
//
//
//        return v;
//
//    }
//
//    public void readPassengers(String journeyId) {
//        JourneyProvider provider = new JourneyProvider();
//
//        Log.d("RBSD", "***1 ");
//
//        db.collection("Journeys")
//                .document(journeyId)
//                .collection("Passengers")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//
//
//                            for (QueryDocumentSnapshot passengerDocument : task.getResult()) {
//                                String jacket = passengerDocument.getString("Jacket");
//
//                                // Assuming child is a nested object
//                                DocumentSnapshot childSnapshot = (DocumentSnapshot) passengerDocument.get("Child");
//                                if (childSnapshot != null) {
//                                    String firstName = childSnapshot.getString("firstName");
//                                    String lastName = childSnapshot.getString("lastName");
//                                    String id = childSnapshot.getString("id");
//
//                                    String[] itemString = new String[2];
//                                    itemString[0] = firstName + lastName + " (" + id + " )";
//                                    itemString[1] = "Jacket: " + jacket;
//                                    passengerList.add(itemString);
//                                    adapter.notifyDataSetChanged();
//
//                                }
//                            }
//
//                            }
//                        } else {
//                            Log.d("RBSD", "Error getting documents: ", task.getException());
//                        }
//
//                });
//    }
//
//}
