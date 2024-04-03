package com.example.wsbapp3.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.fragments.BusStopsFragment;
import com.example.wsbapp3.models.BusStop;
import com.example.wsbapp3.models.Child;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Displays a nested google maps MapFragment.
 * TODO: set up to display a route
 * Button to open BusStopsFragment.
 * Adapted from:
 * https://stackoverflow.com/questions/15433820/mapfragment-in-fragment-alternatives
 */
public class RouteFragment extends Fragment implements OnMapReadyCallback {

    private OnMapReadyCallback callback;
    private boolean pointsPlotted = false;
    private boolean routeDrawn = false;

    // Button to open list of bus stops
    AppCompatButton busstopbutton;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!pointsPlotted) {
            plotPoints(googleMap);
            pointsPlotted = true;
        }
        if (!routeDrawn) {
            drawRoute(googleMap);
            routeDrawn = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route, container, false);

        // Initialize the callback
        callback = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        busstopbutton = v.findViewById(R.id.busstopbutton);
        busstopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStopsFragment busStopsFragment = BusStopsFragment.newInstance();
                // Use FragmentManager to replace the current fragment with ScanFragment
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, busStopsFragment).addToBackStack(null).commit();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void plotPoints(GoogleMap googleMap) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Journeys").document(((MainActivity) requireActivity()).getCurrentJourneyId()).collection("JourneyBusStops").orderBy("arrivalTime").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<LatLng> waypoints = new ArrayList<>();
                    for (QueryDocumentSnapshot busStopDoc : task.getResult()) {
                        BusStop busStop = busStopDoc.get("busStop", BusStop.class);
                        if (busStop != null) {
                            String name = busStop.getName();
                            Double latitude = busStop.getLatitude();
                            Double longitude = busStop.getLongitude();
                            Log.d("RF", "Got stop " + name + " lat " + latitude + " long " + longitude);
                            LatLng location = new LatLng(latitude, longitude);
                            waypoints.add(location);
                            googleMap.addMarker(new MarkerOptions().position(location).title(name));
                        }
                    }
                    if (!waypoints.isEmpty()) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waypoints.get(0), 14)); // Zoom level adjustable
                    }
                } else {
                    Log.d("RF", "Error getting documents: ");
                }
            }
        });
    }

    private void drawRoute(GoogleMap googleMap) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentJourneyId = ((MainActivity) requireActivity()).getCurrentJourneyId();

        db.collection("Journeys").document(currentJourneyId).collection("Routes").document("routeData").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<LatLng> points = new ArrayList<>();
                    Map<String, Map<String, Double>> data = (Map<String, Map<String, Double>>) documentSnapshot.getData().get("points");
                    for (int i = 0; i < data.size(); i++) {
                        Map<String, Double> point = data.get(String.valueOf(i));
                        if (point != null) {
                            Double latitude = point.get("latitude");
                            Double longitude = point.get("longitude");
                            LatLng latLng = new LatLng(latitude, longitude);
                            points.add(latLng);
                        }
                    }
                    // Add polyline to map
                    if (!points.isEmpty()) {
                        PolylineOptions polylineOptions = new PolylineOptions().addAll(points).color(Color.BLUE).width(8);
                        googleMap.addPolyline(polylineOptions);
                    }
                } else {
                    Log.d("RF", "No such document");
                }
            }
        });
    }
}
//public class RouteFragment extends Fragment implements OnMapReadyCallback {
//
//    // Declare callback variable
//    private OnMapReadyCallback callback;
//
//    // Button to open list of bus stops
//    AppCompatButton busstopbutton;
//
//    /**
//     * Callback is triggered when the map is available to be manipulated.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        plotPoints(googleMap);
//        drawRoute(googleMap);
//    }
//
//    /**
//     * Sets up the MapFragment and initializes the button click listener.
//     */
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_route, container, false);
//
//        // Initialize the callback
//        callback = this;
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
//
//        busstopbutton = v.findViewById(R.id.busstopbutton);
//        busstopbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BusStopsFragment busStopsFragment = BusStopsFragment.newInstance();
//                // Use FragmentManager to replace the current fragment with ScanFragment
//                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, busStopsFragment).addToBackStack(null).commit();
//            }
//        });
//
//        return v;
//    }
//
//    /**
//     * Sets up nested MapFragment.
//     */
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
//    }
//
//    private void plotPoints(GoogleMap googleMap) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Journeys").document(((MainActivity) requireActivity()).getCurrentJourneyId()).collection("JourneyBusStops").orderBy("arrivalTime").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                if (task.isSuccessful()) {
//
//                    List<LatLng> waypoints = new ArrayList<>();
//
//                    for (QueryDocumentSnapshot busStopDoc : task.getResult()) {
//                        BusStop busStop = busStopDoc.get("busStop", BusStop.class);
//                        if (busStop != null) {
//                            String name = busStop.getName();
//                            Double latitude = busStop.getLatitude();
//                            Double longitude = busStop.getLongitude();
//                            Log.d("RF", "Got stop " + name + " lat " + latitude + " long " + longitude);
//                            LatLng location = new LatLng(latitude, longitude);
//                            waypoints.add(location);
//                            googleMap.addMarker(new MarkerOptions().position(location).title(name));
//                        }
//                    }
//                    DocumentSnapshot stop1 = task.getResult().getDocuments().get(0);
//                    BusStop busStop1 = stop1.get("busStop", BusStop.class);
//
//                    if (busStop1 != null) {
//                        String name1 = busStop1.getName();
//                        Double latitude1 = busStop1.getLatitude();
//                        Double longitude1 = busStop1.getLongitude();
//                        Log.d("RF", "Got stop 1 at lat 1 " + latitude1 + " long 1 " + longitude1);
//                        LatLng location1 = new LatLng(latitude1, longitude1);
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 16)); // Zoom level adjustable
//                    }
//                } else {
//                    Log.d("RF", "Error getting documents: ");
//                }
//
//            }
//        });
//
//    }
//
//    private void drawRoute(GoogleMap googleMap) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String currentJourneyId = ((MainActivity) requireActivity()).getCurrentJourneyId();
//
//        db.collection("Journeys").document(currentJourneyId).collection("Routes").document("routeData").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    List<LatLng> points = new ArrayList<>();
//                    Map<String, Map<String, Double>> data = (Map<String, Map<String, Double>>) documentSnapshot.getData().get("points");
//                    int index = 0;
//                    for (Map.Entry<String, Map<String, Double>> entry : data.entrySet()) {
//                        Double latitude = entry.getValue().get("latitude");
//                        Double longitude = entry.getValue().get("longitude");
//                        LatLng latLng = new LatLng(latitude, longitude);
//                        points.add(latLng);
//                        index++;
//                        Log.d("RF", "Point " + index + " at lat " + latitude + " long " + longitude);
//                    }
//
//                    // Add polyline to map
//                    if (!points.isEmpty()) {
//                        PolylineOptions polylineOptions = new PolylineOptions().addAll(points).color(Color.RED).width(5);
//                        googleMap.addPolyline(polylineOptions);
//                    }
//                } else {
//                    Log.d("RF", "No such document");
//                }
//            }
//        });
//    }
//}

//    private void drawRoute(GoogleMap googleMap) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String currentJourneyId = ((MainActivity) requireActivity()).getCurrentJourneyId();
//
//        db.collection("Journeys").document(currentJourneyId).collection("Routes").document("routeData").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//
//
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    List<LatLng> points = new ArrayList<>();
//                    Map<String, Map<String, Double>> data = (Map<String, Map<String, Double>>) documentSnapshot.getData().get("points");
//                    int index = 0;
//                    for (Map.Entry<String, Map<String, Double>> entry : data.entrySet()) {
//                        Double latitude = entry.getValue().get("latitude");
//                        Double longitude = entry.getValue().get("longitude");
//                        LatLng latLng = new LatLng(latitude, longitude);
//                        points.add(latLng);
//                        index++;
//                        Log.d("RF", "Point " + index + " at lat " + latitude + " long " + longitude);
//                    }
//
//                    // Add polyline to map
//                    if (!points.isEmpty()) {
//                        PolylineOptions polylineOptions = new PolylineOptions().addAll(points).color(Color.RED).width(5);
//                        googleMap.addPolyline(polylineOptions);
//                    }
//                } else {
//                    Log.d("RF", "No such document");
//                }
//            }
//        });
//    }
//}
//
//public class RouteFragment extends Fragment {
//
//    //Button to open list of bus stops
//    AppCompatButton busstopbutton;
//
//    /**
//     * Callback allows the map to be manipulated.
//     */
//
//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        /**
//         * Callback is triggered when the map is available to be manipulated.
//         */
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            plotPoints(googleMap);
//            drawRoute(googleMap);
//        }
//
//
//        /**
//         * OnCreateView finds the views, sets up the MapFragment, and sets onClickListener for the
//         * bus stop list button.
//         *
//         * @param inflater           The LayoutInflater object that can be used to inflate
//         *                           any views in the fragment,
//         * @param container          If non-null, this is the parent view that the fragment's
//         *                           UI should be attached to.  The fragment should not add the view itself,
//         *                           but this can be used to generate the LayoutParams of the view.
//         * @param savedInstanceState If non-null, this fragment is being re-constructed
//         *                           from a previous saved state as given here.
//         * @return the view
//         */
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.fragment_route, container, false);
//
//            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
//
//            if (mapFragment != null) {
//                mapFragment.getMapAsync(this.callback);
//            }
//            busstopbutton = v.findViewById(R.id.busstopbutton);
//            busstopbutton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    BusStopsFragment busStopsFragment = BusStopsFragment.newInstance();
//                    // Use FragmentManager to replace the current fragment with ScanFragment
//                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, busStopsFragment).addToBackStack(null)  // Add to back stack so the user can navigate back
//                            .commit();
//                }
//            });
//            return v;
//        }
//
//        /**
//         * Sets up nested MapFragment
//         *
//         * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
//         * @param savedInstanceState If non-null, this fragment is being re-constructed
//         *                           from a previous saved state as given here.
//         */
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//            super.onViewCreated(view, savedInstanceState);
//            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
//            if (mapFragment != null) {
//                mapFragment.getMapAsync(callback);
//            }
//        }
//
//        private void plotPoints(GoogleMap googleMap) {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("Journeys").document(((MainActivity) requireActivity()).getCurrentJourneyId()).collection("JourneyBusStops").orderBy("arrivalTime").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                    if (task.isSuccessful()) {
//
//                        List<LatLng> waypoints = new ArrayList<>();
//
//                        for (QueryDocumentSnapshot busStopDoc : task.getResult()) {
//                            BusStop busStop = busStopDoc.get("busStop", BusStop.class);
//                            if (busStop != null) {
//                                String name = busStop.getName();
//                                Double latitude = busStop.getLatitude();
//                                Double longitude = busStop.getLongitude();
//                                Log.d("RF", "Got stop " + name + " lat " + latitude + " long " + longitude);
//                                LatLng location = new LatLng(latitude, longitude);
//                                waypoints.add(location);
//                                googleMap.addMarker(new MarkerOptions().position(location).title(name));
//                            }
//                        }
//                        DocumentSnapshot stop1 = task.getResult().getDocuments().get(0);
//                        BusStop busStop1 = stop1.get("busStop", BusStop.class);
//
//                        if (busStop1 != null) {
//                            String name1 = busStop1.getName();
//                            Double latitude1 = busStop1.getLatitude();
//                            Double longitude1 = busStop1.getLongitude();
//                            Log.d("RF", "Got stop 1 at lat 1 " + latitude1 + " long 1 " + longitude1);
//                            LatLng location1 = new LatLng(latitude1, longitude1);
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 16)); // Zoom level adjustable
//                        }
//                    } else {
//                        Log.d("RF", "Error getting documents: ";
//                    }
//
//                }
//            });
//
//        }
//
//        private void drawRoute(GoogleMap googleMap) {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            String currentJourneyId = ((MainActivity) requireActivity()).getCurrentJourneyId();
//
//            db.collection("Journeys").document(currentJourneyId).collection("Routes").document("routeData").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    if (documentSnapshot.exists()) {
//                        List<LatLng> points = new ArrayList<>();
//                        Map<String, Map<String, Double>> data = (Map<String, Map<String, Double>>) documentSnapshot.getData().get("points");
//
//                        for (Map.Entry<String, Map<String, Double>> entry : data.entrySet()) {
//                            Double latitude = entry.getValue().get("latitude");
//                            Double longitude = entry.getValue().get("longitude");
//                            LatLng latLng = new LatLng(latitude, longitude);
//                            points.add(latLng);
//                        }
//
//                        // Add polyline to map
//                        if (!points.isEmpty()) {
//                            PolylineOptions polylineOptions = new PolylineOptions().addAll(points).color(Color.RED).width(5);
//                            googleMap.addPolyline(polylineOptions);
//                        }
//                    } else {
//                        Log.d("RF", "No such document");
//                    }
//                }
//            });
//        }
//
//    }
//}
//
//
