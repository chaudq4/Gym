package com.chauduong.gym.manager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chauduong.gym.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpManager {
    public static final String URL_FIREBASE="https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static String USERS = "users";
    SignUpManagerListener mSignUpManagerListener;
    private Context mContext;
    FirebaseDatabase mFirebaseDatabase;

    public SignUpManager(SignUpManagerListener mSignUpManagerListener, Context mContext) {
        this.mSignUpManagerListener = mSignUpManagerListener;
        this.mContext = mContext;
        mFirebaseDatabase = FirebaseDatabase.getInstance(URL_FIREBASE);
    }

    public void signUp(User user) {
        checkDuplicate(user);
    }

    public void checkDuplicate(User user) {
        List<User> users = new ArrayList<>();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User user = d.getValue(User.class);
                    users.add(user);
                }
                for (User u : users) {
                    if (u.getPhoneNumber().equalsIgnoreCase(user.getPhoneNumber())) {
                        mSignUpManagerListener.onSignUpError("Tài khoản đã tồn tại");
                        mDatabaseReference.removeEventListener(this);
                        return;
                    }
                }
                registerUser(user);
                mDatabaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mSignUpManagerListener.onSignUpError(databaseError.getMessage());
            }
        });
    }

    private void registerUser(User user) {
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        String id = mDatabaseReference.push().getKey();
        user.setId(id);
        mDatabaseReference.child(id).setValue(user);
        mSignUpManagerListener.onSignUpSuccess(user);

    }
}
