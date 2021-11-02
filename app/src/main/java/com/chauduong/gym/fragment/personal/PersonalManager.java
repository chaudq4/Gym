package com.chauduong.gym.fragment.personal;

import static com.chauduong.gym.fragment.signup.SignUpManager.URL_FIREBASE;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.BodyInformation;
import com.chauduong.gym.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PersonalManager {
    public static final String BODY_INFORMATION = "bodyinformation";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private PersonalManagerListener mPersonalManagerListener;
    private Context mContext;
    private SessionManager mSessionManager;

    public PersonalManager(Context mContext, PersonalManagerListener mPersonalManagerListener) {
        this.mPersonalManagerListener = mPersonalManagerListener;
        this.mContext = mContext;
        firebaseDatabase = FirebaseDatabase.getInstance(URL_FIREBASE);
        mSessionManager = new SessionManager(mContext);
    }

    public void addBodyInformation(BodyInformation bodyInformation) {
        User user = mSessionManager.getUser();
        mDatabaseReference = firebaseDatabase.getReference(BODY_INFORMATION);
        String id = mDatabaseReference.child(user.getPhoneNumber()).push().getKey();
        if (id == null) {
            mPersonalManagerListener.onAddBodyInformationFail();
            return;
        }
        bodyInformation.setId(id);
        mDatabaseReference.child(user.getPhoneNumber()).child(id).setValue(bodyInformation)
                .addOnSuccessListener(unused -> mPersonalManagerListener.onAddBodyInformationSuccess())
                .addOnFailureListener(e -> mPersonalManagerListener.onAddBodyInformationFail());

    }

    public void getAllBodyInformation() {
        User user = mSessionManager.getUser();
        mDatabaseReference = firebaseDatabase.getReference(BODY_INFORMATION);
        mDatabaseReference.child(user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BodyInformation> bodyInformations = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    BodyInformation b = d.getValue(BodyInformation.class);
                    bodyInformations.add(b);
                }
                mPersonalManagerListener.onGetAllBodyInformationSuccess(bodyInformations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mPersonalManagerListener.onGetAllBodyInformationError(error.getMessage());
            }
        });
    }

    public void searchBodyInformation(long millisFromDate, long millisToDate) {
        User user = mSessionManager.getUser();
        mDatabaseReference = firebaseDatabase.getReference(BODY_INFORMATION);
        mDatabaseReference.child(user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BodyInformation> bodyInformations = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    BodyInformation b = d.getValue(BodyInformation.class);
                    if (b.getDate() <= millisToDate && b.getDate() >= millisFromDate)
                        bodyInformations.add(b);
                }
                mPersonalManagerListener.onSearchBodyInformationSuccess(bodyInformations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mPersonalManagerListener.onSearchBodyInformationError(error.getMessage());
            }
        });
    }
}
