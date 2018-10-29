package com.larinet.veikkokrypczyk.myfamilyandfriends.TrackedBy;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.larinet.veikkokrypczyk.myfamilyandfriends.Entities.UserEntity;
import com.larinet.veikkokrypczyk.myfamilyandfriends.R;

public class UserListFragment extends Fragment {
    // this one is very similar to the locationlist just with less fields to display
    ProgressBar progressBar;
    RecyclerView locationList;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private OnItemSelectedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement UserListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.user_progressBar);
        locationList = view.findViewById(R.id.userdata_rv);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        locationList.setLayoutManager(linearLayoutManager);
        getUsers();
    }
    public interface OnItemSelectedListener {
        void onUserSelected(UserEntity user);
    }

    private void getUsers(){
        Query query = db.collection("users");

        FirestoreRecyclerOptions<UserEntity> response = new FirestoreRecyclerOptions.Builder<UserEntity>()
                .setQuery(query, UserEntity.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<UserEntity, UserHolder>(response) {
            @Override
            public void onBindViewHolder(UserHolder holder, int position, UserEntity model) {
                progressBar.setVisibility(View.GONE);
                holder.userName.setText(model.getUserName());

                holder.itemView.setOnClickListener(v -> {
                    //passes the user to the mainactivity
                    listener.onUserSelected(model);
                });
            }

            @Override
            public UserHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recycvlerview_user_row, group, false);
                return new UserHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        locationList.setAdapter(adapter);
    }


    public class UserHolder extends RecyclerView.ViewHolder {
        TextView userName;

        public UserHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
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