package com.chauduong.gym.fragment.more;

import androidx.annotation.NonNull;

import com.chauduong.gym.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoreDatabaseManager {
    private static final String USERS = "users";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private MoreDatabaseListener mMoreDatabaseListener;

    public MoreDatabaseManager(MoreDatabaseListener mMoreDatabaseListener) {
        this.mMoreDatabaseListener = mMoreDatabaseListener;
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    public void listenUserChange(User user) {
        mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.child(user.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMoreDatabaseListener.onUserChange(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateStatusUser(User user){
        mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.child(user.getId()).child("online").setValue(user.isOnline());
    }
}
