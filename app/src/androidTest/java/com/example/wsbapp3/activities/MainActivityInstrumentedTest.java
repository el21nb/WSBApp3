package com.example.wsbapp3.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wsbapp3.R;
import com.example.wsbapp3.fragments.HomeFragment;
import com.example.wsbapp3.fragments.RouteFragment;
import com.example.wsbapp3.fragments.ScanFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

/*Tests for mainActivity: fragment housing with bottom nav bar*/
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    private MainActivity mainActivity;

    private ActivityScenario<MainActivity> scenario;
    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            mainActivity = activity;
        });
    }

    /*Tests for bottom navigation bar*/
    @Test
    public void testHomeFragmentLoad() {
        scenario.onActivity(activity -> {
            Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            assertTrue(fragment instanceof HomeFragment);
        });

    }
    @Test
    public void testBottomNavScanClick() {
        onView(withId(R.id.scan)).perform(click());
        scenario.onActivity(activity -> {
            Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            assertTrue(fragment instanceof ScanFragment);
        });
    }
    @Test
    public void testBottomNavRouteClick() {
        onView(withId(R.id.route)).perform(click());
        scenario.onActivity(activity -> {
            Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            assertTrue(fragment instanceof RouteFragment);
        });
    }
}
