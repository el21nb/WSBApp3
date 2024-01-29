package com.example.wsbapp3;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wsbapp3.Journey;
import com.example.wsbapp3.JourneyProvider;
import com.example.wsbapp3.databinding.ActivityMainBinding;
import com.example.wsbapp3.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
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
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
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

        currentJourneyId = "KelmpE01102024AM";
        DatabaseProvider provider = new DatabaseProvider();
        //provider.initialiseDatabase(currentJourneyId); //load database


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack
        fragmentTransaction.commit();
    }


    }









