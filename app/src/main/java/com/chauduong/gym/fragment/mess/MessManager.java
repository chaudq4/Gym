package com.chauduong.gym.fragment.mess;

import static com.chauduong.gym.fragment.signup.SignUpManager.URL_FIREBASE;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chauduong.gym.R;
import com.chauduong.gym.fragment.signin.SignInManagerListener;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessManager {
    private static final String USERS = "users";
    private FirebaseDatabase firebaseDatabase;
    private MessManagerListener mMessManagerListener;

    public MessManager(MessManagerListener mMessManagerListener) {
        this.mMessManagerListener = mMessManagerListener;
        firebaseDatabase = FirebaseDatabase.getInstance(URL_FIREBASE);
    }

    public void getAllListUserForChat(Context mContext) {
        SessionManager sessionManager = new SessionManager(mContext);
        DatabaseReference mDatabaseReference = firebaseDatabase.getReference(USERS);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    if (!u.getPhoneNumber().equalsIgnoreCase(sessionManager.getUser().getPhoneNumber()))
                        userList.add(u);
                }
                mMessManagerListener.onGetAllUserSuccess(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mMessManagerListener.onGetAllUserError(error.getMessage());
            }
        });
    }
}
