package com.larinet.veikkokrypczyk.myfamilyandfriends;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.larinet.veikkokrypczyk.myfamilyandfriends.TrackedBy.LocationListFragment;
import com.larinet.veikkokrypczyk.myfamilyandfriends.TrackedBy.TrackedByFragment;
import com.larinet.veikkokrypczyk.myfamilyandfriends.Entities.UserEntity;
import com.larinet.veikkokrypczyk.myfamilyandfriends.TrackedBy.UserListFragment;
import com.larinet.veikkokrypczyk.myfamilyandfriends.Login.LoginFragment;
import com.larinet.veikkokrypczyk.myfamilyandfriends.Map.MapFragment;

public class MainActivity extends AppCompatActivity implements LoginListener, UserListFragment.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private ViewPagerAdapter mLoginOnlyAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    private ViewPagerAdapter mFullFeatureAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    private ViewPager mViewPager;
    private TabLayout mtabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mAuth = FirebaseAuth.getInstance();

        mViewPager = findViewById(R.id.pager);
        // Add Fragments to adapter one by one
        mLoginOnlyAdapter.addFragment(new LoginFragment(), "Login");
        mFullFeatureAdapter.addFragment(new LoginFragment(), "Login");
        mFullFeatureAdapter.addFragment(new MapFragment(), "Map");
        mFullFeatureAdapter.addFragment(new TrackedByFragment(), "TrackedBy");
        mViewPager.setAdapter(mLoginOnlyAdapter);
        mtabLayout = findViewById(R.id.tabs);
        mtabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mtabLayout.removeAllTabs();
            mViewPager.setAdapter(mFullFeatureAdapter);
            mtabLayout.setupWithViewPager(mViewPager);
        } else {
            mtabLayout.removeAllTabs();
            mViewPager.setAdapter(mLoginOnlyAdapter);
            mtabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void AuthStateChange(FirebaseUser user) {
        if (user != null) {
            mtabLayout.removeAllTabs();
            mViewPager.setAdapter(mFullFeatureAdapter);
            mtabLayout.setupWithViewPager(mViewPager);
        } else {
            mtabLayout.removeAllTabs();
            mViewPager.setAdapter(mLoginOnlyAdapter);
            mtabLayout.setupWithViewPager(mViewPager);
        }
    }

    //called when a user is selected on tracked by
    @Override
    public void onUserSelected(UserEntity user) {
        // only way to obtain the fragment in current implementation
        TrackedByFragment trackFragment = (TrackedByFragment) mFullFeatureAdapter.getItem(2);
        LocationListFragment locationFragment = (LocationListFragment) trackFragment.getChildFragmentManager().findFragmentById(R.id.locationListFragment);
        locationFragment.getLocationDataForUser(user);
    }
}


