package com.chauduong.gym.activity;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConversationManager implements ChildEventListener {
    private static final String CONVERSATION = "conversation";
    private static final String LAST_MSG = "lastestmsg";
    private static final String TAG = "ConversationManager";
    private Context mContext;
    private ConversationManagerListener managerListener;
    private SessionManager sessionManager;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public ConversationManager(Context mContext, ConversationManagerListener managerListener) {
        this.mContext = mContext;
        this.managerListener = managerListener;
        sessionManager = new SessionManager(mContext);
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    public void sentInbox(Inbox inbox) {
        inbox.setFrom(sessionManager.getUser());
        mDatabaseReference = FirebaseDatabase.getInstance("https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(CONVERSATION);
        String id = mDatabaseReference.child(inbox.getFrom().getPhoneNumber()).child(inbox.getTo().getPhoneNumber()).push().getKey();
        inbox.setId(id);

        mDatabaseReference.child(inbox.getFrom().getPhoneNumber()).child(inbox.getTo().getPhoneNumber()).child(id).setValue(inbox);
        mDatabaseReference.child(inbox.getFrom().getPhoneNumber()).child(inbox.getTo().getPhoneNumber()).child(LAST_MSG).setValue(inbox);

        mDatabaseReference.child(inbox.getTo().getPhoneNumber()).child(inbox.getFrom().getPhoneNumber()).child(id).setValue(inbox);
        mDatabaseReference.child(inbox.getTo().getPhoneNumber()).child(inbox.getFrom().getPhoneNumber()).child(LAST_MSG).setValue(inbox);
    }

    public void getAllConversation(User user) {


    }

    public void getAllInbox(User toUser, int index) {
        mDatabaseReference = mFirebaseDatabase.getReference(CONVERSATION);
        if (index >= 2)
            mDatabaseReference.child(sessionManager.getUser().getPhoneNumber()).child(toUser.getPhoneNumber()).removeEventListener(this);
        mDatabaseReference.child(sessionManager.getUser().getPhoneNumber()).child(toUser.getPhoneNumber()).limitToLast(10 * index).addChildEventListener(this);

    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if (!snapshot.getKey().equalsIgnoreCase(LAST_MSG)) {
            Log.i(TAG, "onChildAdded: " + snapshot.getValue());
            managerListener.onGetItemSuccessInbox(snapshot.getValue(Inbox.class));
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.i(TAG, "onChildChanged: ");
        managerListener.onAddNewInbox();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Log.i(TAG, "onChildRemoved: ");
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.i(TAG, "onChildMoved: ");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.i(TAG, "onCancelled: ");
    }
}
