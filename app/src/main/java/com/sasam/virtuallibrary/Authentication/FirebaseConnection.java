package com.sasam.virtuallibrary.Authentication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConnection {

    public static DatabaseReference databaseRef(String ref){
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        return mFirebaseDatabase.getReference(ref);
    }
}
