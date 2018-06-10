package com.example.inna.sleepy2.FirebaseServices;

import com.firebase.client.DataSnapshot;

public interface OnGetDataListener {
    void onSuccess(DataSnapshot dataSnapshot);
    void onStart();
    void onFailure();
}
