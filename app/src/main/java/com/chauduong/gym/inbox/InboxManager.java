package com.chauduong.gym.inbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class InboxManager implements ChildEventListener {
    private static final int TOTAL_VISIBLE_COUNT = 10;
    private static final String CONVERSATION = "conversation";
    private static final String LAST_MSG = "lastestmsg";
    private static final String USERS = "users";
    private static final String TAG = "ConversationManager";
    private static final String IMAGE_CHAT = "imagechat";
    private InboxManagerListener inboxManagerListener;
    private SessionManager sessionManager;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private Context mContext;

    public InboxManager(Context mContext, InboxManagerListener inboxManagerListener) {
        this.mContext = mContext;
        this.inboxManagerListener = inboxManagerListener;
        sessionManager = new SessionManager(mContext);
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseStorage = FirebaseStorage.getInstance("gs://gymgc-55cfd.appspot.com");

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

    public void getPagingInbox(User toUser, int index) {
        mDatabaseReference = mFirebaseDatabase.getReference(CONVERSATION);
        if (index >= 2)
            mDatabaseReference.child(sessionManager.getUser().getPhoneNumber()).child(toUser.getPhoneNumber()).removeEventListener(this);
        mDatabaseReference.child(sessionManager.getUser().getPhoneNumber()).child(toUser.getPhoneNumber()).limitToLast(TOTAL_VISIBLE_COUNT * index).addChildEventListener(this);

    }

    public void uploadImage(Uri filePath) {
        mStorageReference = mFirebaseStorage.getReference(IMAGE_CHAT);
        if (filePath != null) {
            StorageReference ref = mStorageReference.child("images/" + UUID.randomUUID().toString());
            Bitmap bitmap, tmp;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), filePath);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (tmp.getWidth() > 2000)
                tmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            else
                tmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] bytes = baos.toByteArray();
            bitmap.recycle();
            tmp.recycle();
            ref.putBytes(bytes)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getTask().isComplete()) {
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();
                                inboxManagerListener.onSuccessUpload(downloadUrl.toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            inboxManagerListener.onFailed(e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            inboxManagerListener.onProgressUpload(progress);
                        }
                    });
        }
    }

    public void listenToUserChange(User toUser) {
        mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.child(toUser.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inboxManagerListener.onToUserChange(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if (!snapshot.getKey().equalsIgnoreCase(LAST_MSG)) {
            inboxManagerListener.onGetItemSuccessInbox(snapshot.getValue(Inbox.class));
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        inboxManagerListener.onAddNewInbox();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Log.i(TAG, "onChildRemoved: ");
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.i(TAG, "onCancelled: ");
    }

    public void upLoadFileBitmap(Bitmap bitmap) {
        mStorageReference = mFirebaseStorage.getReference(IMAGE_CHAT);
        if (bitmap != null) {
            StorageReference ref = mStorageReference.child("images/" + UUID.randomUUID().toString());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Log.i(TAG, "upLoadFileBitmap: "+tmp.getWidth()+" "+tmp.getHeight());
            if (tmp.getWidth() > 2000)
                tmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            else
                tmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] bytes = baos.toByteArray();
            tmp.recycle();
            ref.putBytes(bytes)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getTask().isComplete()) {
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();
                                inboxManagerListener.onSuccessUpload(downloadUrl.toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            inboxManagerListener.onFailed(e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            Log.i(TAG, "onProgress: "+progress);
                            inboxManagerListener.onProgressUpload(progress);
                        }
                    });
        }
    }
}
