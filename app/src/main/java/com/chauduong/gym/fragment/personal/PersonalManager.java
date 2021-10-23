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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}
