package com.example.wsbapp3.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wsbapp3.R;
import com.example.wsbapp3.fragments.RouteFragment;
import com.example.wsbapp3.fragments.ScanFragment;
import com.example.wsbapp3.database.DatabaseProvider;
import com.example.wsbapp3.databinding.ActivityMainBinding;
import com.example.wsbapp3.fragments.HomeFragment;

/**
 * Main Activity houses all the fragment accessible from the bottom nav bar and the home screen menu
 */
public class MainActivity extends AppCompatActivity {

    //ID for the current journey, used by several fragments
    private String currentJourneyId;

    private ActivityMainBinding binding;

    public String getCurrentJourneyId() {
        return currentJourneyId;
    }

    public void setCurrentJourneyId(String currentJourneyId) {
        this.currentJourneyId = currentJourneyId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create View
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialise with home fragment
        replaceFragment(new HomeFragment());

        //Set bottom nav menu
        binding.bottomNavigationView.setItemActiveIndicatorColor(null);
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.route) {
                replaceFragment(new RouteFragment());
            } else if (itemId == R.id.scan) {
                replaceFragment(new ScanFragment());
            }
            return true;
        });

        //Initialise current journey Id, can be changed
        currentJourneyId = "AppelbeesK102102024AM";
        //Preload database, does not need to be done every time.
        //TODO: move to a cloud function
        //DatabaseProvider provider = new DatabaseProvider();
        //provider.initialiseDatabase(currentJourneyId); //load database


    }

    /**
     * Replace the fragment in the container above the bottom nav bar
     *
     * @param fragment new fragment to be loaded
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack
        fragmentTransaction.commit();
    }


}









