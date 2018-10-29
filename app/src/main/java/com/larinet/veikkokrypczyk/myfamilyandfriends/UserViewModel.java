package com.larinet.veikkokrypczyk.myfamilyandfriends;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.larinet.veikkokrypczyk.myfamilyandfriends.Entities.UserEntity;

import java.util.ArrayList;


public class UserViewModel extends AndroidViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "UserViewModel";
    private ArrayList<UserEntity> mCachedUsers = new ArrayList<>();
    private ArrayList<UserEntity> mUserList = new ArrayList<>();

    public UserViewModel(Application application) {
        super(application);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }


    public void insert(UserEntity userEntity) {
        mCachedUsers.clear();
        Query query = db.collection("users").whereEqualTo("id", userEntity.getId());
        // check if the user exist else add him
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        UserEntity user = document.toObject(UserEntity.class);
                        mCachedUsers.add(user);
                    }
                    if(mCachedUsers == null || mCachedUsers.isEmpty()) {
                        db.collection("users").add(userEntity).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "onSuccess: Added User");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: Error inserting doc " + e.toString());
                            }
                        });
                    } else {
                        Log.d(TAG, "onComplete: User Exists");
                    }
                }else {
                    Log.e(TAG, "onComplete: ERROR");
                }
            }
        });
    }
    public ArrayList<UserEntity> getUsers(){
        Query query = db.collection("users");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        UserEntity user = document.toObject(UserEntity.class);
                        mUserList.add(user);
                    }
                }else {
                    Log.e(TAG, "onComplete: ERROR");
                }
            }
        });
        return mUserList;
    }
}
