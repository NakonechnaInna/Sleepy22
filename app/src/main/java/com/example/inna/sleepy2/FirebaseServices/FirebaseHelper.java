package com.example.inna.sleepy2.FirebaseServices;


import com.example.inna.sleepy2.UserModel;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FirebaseHelper {
    private static Firebase mRootRef = new Firebase("https://sleepy22db.firebaseio.com/");

    public static void getUserDataSnapshot(String login, final OnGetDataListener listener) {
        listener.onStart();
        mRootRef.child("Users").child(login).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listener.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                        listener.onFailure();
                    }

                });
    }
    public static void addUser(UserModel user) {
        mRootRef.child("Users").child(user.Login).setValue(user);
    }

    public static void readData(Firebase ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onFailure();
            }
        });

    }
}
