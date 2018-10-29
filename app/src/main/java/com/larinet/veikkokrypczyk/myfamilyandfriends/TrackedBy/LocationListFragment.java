package com.larinet.veikkokrypczyk.myfamilyandfriends.TrackedBy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.larinet.veikkokrypczyk.myfamilyandfriends.Entities.UserEntity;
import com.larinet.veikkokrypczyk.myfamilyandfriends.Entities.LocationEntity;
import com.larinet.veikkokrypczyk.myfamilyandfriends.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationListFragment extends Fragment {
    ProgressBar progressBar;
    RecyclerView locationList;
    private static final String TAG = "LocationListFragment";
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Query query;
    private FirestoreRecyclerOptions<LocationEntity> response;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.location_progressBar);
        locationList = view.findViewById(R.id.locationdata_rv);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        locationList.setLayoutManager(linearLayoutManager);
        currentUser = mAuth.getCurrentUser();
        UserEntity user = new UserEntity(currentUser.getDisplayName(), currentUser.getUid());
        getInitialData(user);
    }

    public void getInitialData(UserEntity user){
        progressBar.setVisibility(View.VISIBLE);
        //before this query works you need to create an index in firestore
        query = db.collection("locations").orderBy("date", Query.Direction.DESCENDING).whereEqualTo("id", user.getId());

        response = new FirestoreRecyclerOptions.Builder<LocationEntity>()
                .setQuery(query, LocationEntity.class)
                .build();

        attachRecyclerViewAdapter();
        adapter.notifyDataSetChanged();
    }
    // called from Mainactivity when another user is selected
    public void getLocationDataForUser(UserEntity user){
        progressBar.setVisibility(View.VISIBLE);
        adapter.stopListening();
        query = db.collection("locations").orderBy("date", Query.Direction.DESCENDING).whereEqualTo("id", user.getId());

        response = new FirestoreRecyclerOptions.Builder<LocationEntity>()
                .setQuery(query, LocationEntity.class)
                .build();

        attachRecyclerViewAdapter();
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    private void attachRecyclerViewAdapter() {
        adapter = new FirestoreRecyclerAdapter<LocationEntity, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(FriendsHolder holder, int position, LocationEntity model) {
                progressBar.setVisibility(View.GONE);
                holder.locationLat.setText(Double.toString(model.getGeoPoint().getLatitude()));
                holder.locationLong.setText(Double.toString(model.getGeoPoint().getLongitude()));
                holder.userName.setText(model.getUserName());
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.GERMAN);
                String time = sdf.format(model.getDate());
                holder.date.setText(time);


                holder.itemView.setOnClickListener(v -> {
                    Snackbar.make(locationList, Double.toString(model.getGeoPoint().getLatitude())+", "+Double.toString(model.getGeoPoint().getLongitude()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recyclerview_row, group, false);
                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        locationList.setAdapter(adapter);
    }


    public class FriendsHolder extends RecyclerView.ViewHolder {
        TextView locationLat;
        TextView locationLong;
        TextView userName;
        TextView date;

        public FriendsHolder(View itemView) {
            super(itemView);
            locationLat = itemView.findViewById(R.id.locationLat);
            locationLong = itemView.findViewById(R.id.locationLong);
            userName = itemView.findViewById(R.id.userName);
            date = itemView.findViewById(R.id.date);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}