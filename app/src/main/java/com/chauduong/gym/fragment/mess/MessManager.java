package com.chauduong.gym.fragment.mess;

import static com.chauduong.gym.fragment.signup.SignUpManager.URL_FIREBASE;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chauduong.gym.R;
import com.chauduong.gym.fragment.signin.SignInManagerListener;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Conversation;
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
import java.util.Random;
import java.util.UUID;

public class MessManager {
    private static final String USERS = "users";
    private static final String CONVERSATION = "conversation";
    private FirebaseDatabase firebaseDatabase;
    private static final String TAG = "MessManager";
    private MessManagerListener mMessManagerListener;
    private SessionManager sessionManager;

    public MessManager(MessManagerListener mMessManagerListener, Context mContext) {
        this.mMessManagerListener = mMessManagerListener;
        firebaseDatabase = FirebaseDatabase.getInstance(URL_FIREBASE);
        sessionManager = new SessionManager(mContext);
    }

    public void getAllListUserForChat() {
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

    public void getAllConversation() {
        DatabaseReference mDatabaseReference = firebaseDatabase.getReference(CONVERSATION);
        mDatabaseReference.child(sessionManager.getUser().getPhoneNumber()).orderByChild("lastestmsg/time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Conversation> conversations = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Inbox inbox = d.child("lastestmsg").getValue(Inbox.class);
                    Conversation conversation = new Conversation();
                    conversation.setInbox(inbox);
                    if (inbox != null)
                        conversations.add(0, conversation);
                    Log.i(TAG, "onDataChange: " + conversation.toString());
                }
                mMessManagerListener.onGetItemConversation(conversations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        for (int i = 0; i < 100; i++) {
//            User user = new User();
//            user.setName("Chau " + new Random().nextInt(100));
//            Conversation conversation = new Conversation();
//            conversation.setToUser(user);
//            conversation.setLastMsg("lastMsg " + new StringBuilder(UUID.randomUUID().toString()).substring(0, 15));
//            mMessManagerListener.onGetItemConversation(conversation);
////            try {
////                Thread.sleep(1000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
//            }
//        }
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    User user = new User();
//                    user.setName("Chau " + new Random().nextInt(100));
//                    Conversation conversation = new Conversation();
//                    conversation.setToUser(user);
//                    conversation.setLastMsg("lastMsg " + new StringBuilder(UUID.randomUUID().toString()).substring(0, 15));
//                    mMessManagerListener.onGetItemConversation(conversation);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });

    }

    public void updateStatusRead(Conversation conversation) {
        firebaseDatabase.getReference().child(CONVERSATION).child(conversation.getInbox().getTo().getPhoneNumber()).child(conversation.getInbox().getFrom().getPhoneNumber())
                .child("lastestmsg").child("read").setValue(true);
    }
}
