package com.example.wsbapp3.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wsbapp3.R;
import com.example.wsbapp3.fragments.RouteFragment;
import com.example.wsbapp3.fragments.ScanFragment;
import com.example.wsbapp3.database.DatabaseProvider;
import com.example.wsbapp3.databinding.ActivityMainBinding;
import com.example.wsbapp3.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Main Activity houses all the fragment accessible from the bottom nav bar and the home screen menu
 */
public class MainActivity extends AppCompatActivity {

    //ID for the current journey, used by several fragments
    private String currentJourneyId;

    ActivityMainBinding binding;

    public String getCurrentJourneyId() {
        return currentJourneyId;
    }

    public void setCurrentJourneyId(String currentJourneyId) {
        this.currentJourneyId = currentJourneyId;
    }

    /**
     * Get permission to show notifications
     */

    final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    Toast.makeText(MainActivity.this, "Warning: no notifications will be shown!", Toast.LENGTH_SHORT).show();
                }
            });

    /**
     * onCreate:
     * inflateLayouts, set Listeners, subscribe to notification channel
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialise current journey Id, can be changed
        setCurrentJourneyId("AppelbeesK102102024AM");

        //Create View
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Subscribe to notifications
        FirebaseMessaging.getInstance().subscribeToTopic("statuses")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("notifications", "Subscribed");
                    } else {
                        Log.e("notifications", "Not subscribed: " + task.getException());
                    }
                });

        //load home fragment
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


    }

    /**
     * Replace the fragment in the container above the bottom nav bar
     *
     * @param fragment new fragment to be loaded
     */
    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack to allow back navigation
        fragmentTransaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}









