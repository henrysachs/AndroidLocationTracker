package com.larinet.veikkokrypczyk.myfamilyandfriends;

import com.google.firebase.auth.FirebaseUser;

public interface LoginListener {
    void AuthStateChange(FirebaseUser user);
}
