package com.chauduong.gym.fragment.signin;

import static com.chauduong.gym.fragment.signup.SignUpManager.URL_FIREBASE;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chauduong.gym.R;
import com.chauduong.gym.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInManager {
    private static final String USERS = "users";
    private Context mContext;
    private FirebaseDatabase firebaseDatabase;
    private SignInManagerListener mSignInManagerListener;

    public SignInManager(Context mContext, SignInManagerListener mSignInManagerListener) {
        this.mContext = mContext;
        this.mSignInManagerListener = mSignInManagerListener;
        firebaseDatabase = FirebaseDatabase.getInstance(URL_FIREBASE);
    }

    public void login(User user) {
        DatabaseReference mDatabaseReference = firebaseDatabase.getReference(USERS);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    if (u.getPhoneNumber().equalsIgnoreCase(user.getPhoneNumber()) && u.getPassword().equalsIgnoreCase(user.getPassword())) {
                        u.setOnline(true);
                        updateStatusLogin(u);
                        mSignInManagerListener.onSignInSuccess(u);
                        mDatabaseReference.removeEventListener(this);
                        return;
                    }
                }
                mSignInManagerListener.onSignInError(mContext.getString(R.string.invalid_account));
                mDatabaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mSignInManagerListener.onSignInError(error.getMessage());
            }
        });
    }

    public void updateStatusLogin(User user) {
        DatabaseReference mDatabaseReference = firebaseDatabase.getReference(USERS);
        mDatabaseReference.child(user.getId()).child("online").setValue(user.isOnline());
    }
}
