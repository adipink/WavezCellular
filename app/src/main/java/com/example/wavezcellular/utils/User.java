package com.example.wavezcellular.utils;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wavezcellular.activities.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User {
    private String name;
    private String email;

    public User(){

    }

    public User(String name, String email){
        setName(name);
        setEmail(email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String generateGuest() {
            long id = System.currentTimeMillis();
            String guest = "Guest" + id;
        return guest;
    }

    public static void getUserName(FirebaseUser user, final UserNameListener listener) {
        String userId = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String displayName = dataSnapshot.child("name").getValue(String.class);
                    listener.onUserNameFetched(displayName);
                } else {
                    listener.onUserNameFetchFailed("User data doesn't exist in the database");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onUserNameFetchFailed("Error fetching user data: " + databaseError.getMessage());
            }
        });
    }

    public interface UserNameListener {
        void onUserNameFetched(String displayName);
        void onUserNameFetchFailed(String errorMessage);
    }
}
