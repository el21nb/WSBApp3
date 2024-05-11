package com.example.wsbapp3.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.database.ChildProvider;
import com.example.wsbapp3.database.JourneyProvider;
import com.example.wsbapp3.database.TicketProvider;
import com.example.wsbapp3.fragments.BusStopsFragment;
import com.example.wsbapp3.models.BusStop;
import com.example.wsbapp3.models.Child;
import com.example.wsbapp3.models.Journey;
import com.example.wsbapp3.models.Ticket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
 * https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime
 */
public class RouteFragment extends Fragment implements OnMapReadyCallback {

    private OnMapReadyCallback callback;
    private boolean pointsPlotted = false;
    private boolean routeDrawn = false;

    private String currentJourneyId;

    private String journeyDateTime;

    // Button to open list of bus stops
    AppCompatButton busstopbutton;
    TextView routetext;

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
        //Get location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }
        //Show location on map
        googleMap.setMyLocationEnabled(true);
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
        routetext = v.findViewById(R.id.routetext);
        getJourneyDateTime();

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

    private void getJourneyDateTime() {
        currentJourneyId = ((MainActivity) requireActivity()).getCurrentJourneyId();
        JourneyProvider journeyProvider = new JourneyProvider();
        journeyProvider.fetchJourneyById(currentJourneyId, new JourneyProvider.FetchJourneyCallback() {
            //Fetch and set ticket object
            @Override
            public void onJourneyFetched(Journey journey) {
                journeyDateTime = journey.getJourneyDateTime();
                routetext.setText(journeyDateTime);
            }

            @Override
            public void onJourneyNotFound() {
                Log.d("RF", "Error fetching journey ");
            }

            @Override
            public void onFetchFailed(String errorMessage) {
                Log.d("RF", "Error fetching journey " + errorMessage);
            }
        });
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
                            googleMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
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
                        PolylineOptions polylineOptions = new PolylineOptions().addAll(points).color(Color.parseColor("#2f6ea8")).width(8);
                        googleMap.addPolyline(polylineOptions);
                    }
                } else {
                    Log.d("RF", "No such document");
                }
            }
        });
    }

}