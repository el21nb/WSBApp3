package com.example.wsbapp3.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wsbapp3.adapters.InfoAdapter;
import com.example.wsbapp3.models.ChildListItem;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.R;
import com.example.wsbapp3.adapters.InfoAdapter;
import com.example.wsbapp3.models.InfoListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fragment to display a recyclerview of expandable information tabs
 * Accessed via home screen

 */
public class InfoFragment extends Fragment {

    //Adapter for the parent RV
    InfoAdapter infoAdapter;

    //List of Bus Stops
    ArrayList<InfoListItem> infoListItemArrayList;
    List<Pair<String, String>> infoSegments;

    //Parent recyclerview
    RecyclerView rvParent;

    /**
     * InfoFragment: contains a recyclerview of expandable info tabs.
     * Recyclerview populated by local data, at the bottom of this fragment.
     */
    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment.
     *
     * @return new fragment
     */
    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Called when fragment layout is created.
     * Sets the textview to display the current date.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        rvParent = v.findViewById(R.id.RVparent);

        //Initialise info list for RV
        infoListItemArrayList = new ArrayList<>();

        //call method to populate the RVs
        populateInfoList();

        return v;
    }

    /**
     * Populates the recyclerview from the strings in this fragment
     */
    public void populateInfoList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initialize Firestore
        infoListItemArrayList = new ArrayList<>();
        makeInfoList();
                            for (Pair<String, String> infoPair : infoSegments) {

                                if (infoPair != null) {
                                    String heading = infoPair.first;
                                    String body = infoPair.second;

                                    //Create infoListItem
                                    InfoListItem infoListItem = new InfoListItem(heading, body);
                                    infoListItemArrayList.add(infoListItem);
                                }

    }
        infoAdapter = new InfoAdapter(requireActivity(), requireActivity().getSupportFragmentManager(), infoListItemArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        rvParent.setLayoutManager(linearLayoutManager);
        rvParent.setAdapter(infoAdapter);

    }

    /**
     * Build list of heading-body pairs, from local strings.
     */
    public void makeInfoList() {
        infoSegments = new ArrayList<>();
        infoSegments.add(new Pair<>("Using the App",
                "It is recommended that you familiarise yourself with all the information in this advice tab, before your first Walking Bus Journey. \n \n " +
                        "The app has three main  tabs on the bottom navigation menu: ROUTE, HOME, SCAN\n" +
                        "\n" +
                        "ROUTE:\n" +
                        "- View the map visualisation of today’s route, and your location, to help you plan and navigate.\n" +
                        "- Click on the markers to view the bus stops name\n" +
                        "- Follow the button at the bottom of the screen to view the route in detail, including scheduled arrival times, addresses, and which children are booked to be collected/ dropped-off at each stop\n" +
                        "- Click on any child’s booking to view their details, including medical information and emergency contacts \n" +
                        "\n" +
                        "HOME:\n" +
                        "- The home menu offers navigation to all parts of the app, including the Route and Scan tabs\n" +
                        "\n" +
                        "SCAN:\n" +
                        "- The scan tab is where children are registered on/off the bus, and jackets are de-assigned.\n" +
                        "- Jackets and tickets both have scannable QR codes, but alternatively you can manually type in their codes.\n" +
                        "- At every pick-up and drop-off, a ticket and jacket must be scanned. This marks the transfer of responsibility for the child to or from the bus driver.\n" +
                        "\n" +
                        "OUTWARD JOURNEY PROCESS:\n" +
                        "- Bus stop pick up: scan the ticket of the parent/ carer to register the child onto the bus, then scan a jacket to assign it to the child.\n" +
                        "- School drop off: scan the ticket of the teacher/ staff member to register the child off the bus, and transfer responsibility, then scan a jacket to de-assign it from the child.\n" +
                        "\n" +
                        "RETURN JOURNEY PROCESS:\n" +
                        "- School pick up: scan the ticket of the teacher/ staff member to register the child onto the bus, then scan a jacket to assign it to the child.\n" +
                        "- School drop off: scan the ticket of the parent/ carer to register the child off the bus, and transfer responsibility, then scan a jacket to assign it to the child.\n"));
        infoSegments.add(new Pair<>("Using the Wearable Devices","The WSB jackets have pockets for the wearable devices.\n" +
                "Before scanning the jacket, make sure to turn on the wearable device via the wake-up button.\n" +
                "These devices send an alert to this app if they go out of range\n"));
        infoSegments.add(new Pair<>("WSB Journey Prerequisites\n","WSB Drivers must have an up to date DBS check and must inform the school if anything might change the status of that check\n" +
                "WSB Drivers must be first aid trained and have a first-aid kit accessible on each journey.\n" +
                "WSB Drivers must have attended a Pedestrian Road Safety course.\n" +
                "Each route should have an up to date risk-assessment, and the driver should familiarise themself with it before the journey\n" +
                "Driver must ensure they have enough high-visibility jackets, and charged wearable devices for all children.\n" +
                "Driver or conductor must carry a first-aid kit\n" +
                "If the driver is unable to conduct a journey, they must notify users via a status update or contacting the school.\n"));
        infoSegments.add(new Pair<>("Rules for WSB Journeys\n","The WSB must not exceed the following adult to child ratios\n" +
                "At least 2 adults per bus, and not exceeding a ratio of 1 adult: 6 children\n" +
                "Everyone in the bus, including adults, must wear a fluorescent and reflective jacket.\n" +
                "Outside of daylight hours, the driver should wear a white chest  light, and the conductor a red back light.\n" +
                "Driver’s phone must be turned on, with internet and location services enabled throughout the journey, to allow for bus location monitoring.\n" +
                "No children should be accepted onto the bus or released off the bus without a valid ticket being checked.\n" +
                "If this app alerts the driver that a child’s wearable device has gone out of range, the driver must stop the bus and locate the child.\n"));
        infoSegments.add(new Pair<>("Dealing with incidents on route\n","MEDICAL INCIDENTS\n" +
                "Familiarise yourself with the medical notes of booked children before the journey. If they are prescribed emergency medication, e.g. inhalers, epipens, ensure that you know where to find it.\n" +
                "Minor medical incidents can be waited to address until the bus reaches the school/ parent/ carer, bute each walking bus should also carry a first aid kit.\n" +
                "For non-emergency medical advice, call 111\n" +
                "In an emergency, call 999 and ask for an ambulance\n" +
                "For any serious incidents, ensure that the school and child’s parents are informed.\n" +
                "School contacts can be found under ‘Useful Contacts’ on this page.\n" +
                "A child’s emergency contacts can be found by clicking on their name on the ‘Onboard passengers’ tab or their bus stop booking.\n" +
                "\n" +
                "ROUTE OBSTRUCTIONS\n" +
                "If the route is unexpectedly obstructed, the Driver can use their discretion to determine a safe route to school. \n" +
                "If you need further advice on this, contact the school.\n" +
                "If a change to a route will result in delays or missing bus stops, please post a status update to notify the school and parents/carers, or contact the school and request that they do the same.\n" +
                "If no safe alternative route can be found, move the walking bus to a safe place, away from roads, and notify parents/carers that they must collect their children.\n" +
                "\n" +
                "BEHAVIOURAL INCIDENTS\n" +
                "The driver should ensure that all passengers are aware of the expectations of the behaviour\n" +
                "The driver should address misbehaviour en-route, and if they deem it necessary, inform the child’s teachers/ parents/ carers after the journey.\n" +
                "If a child is continuing to misbehave on multiple journeys such that it is a safety concern, the driver can request that the child is suspended temporarily or permanently from the WSB scheme.\n" +
                "\n" +
                "DRIVER’S PHONE LOSS/ DAMAGE/ MALFUNCTION\n" +
                "Please use another device to inform the school as soon as possible. Loss of location information will cause an alert, and unless updated, the school will have to dispatch a staff member to the bus’ last recorded location.\n" +
                "\n" +
                "APP FAILURE\n" +
                "Same as above.\n" +
                "At the end of the journey, please contact our support team at support@wsb.com or +44123456789 to report the issue\n" +
                "\n" +
                "INCORRECT BOOKINGS\n" +
                "It is vital that every child onboard the bus is booked to be there and registered onboard. If a parent/carer at a bus stop has not booked, please ensure they do so. It is possible to book onto an active journey.\n" +
                "It is important that the bus stays on schedule where possible: if someone is not at the bus stop on time, the bus should not wait for them. If the child does not arrive at school by other means, it is the schools responsibility to contact the parents.\n" +
                "\n" +
                "DELAYS\n" +
                "If the bus is more than 15 minutes delayed, please post a status update or contact the school.\n"));
        infoSegments.add(new Pair<>("Useful contacts\n","Your school contacts are:\n" +
                "[sch contacts from firestore]\n" +
                "The WSB App support team contacts are:\n" +
                "support@wsb.com\n" +
                "+44123456789 \n"));

    }
}


