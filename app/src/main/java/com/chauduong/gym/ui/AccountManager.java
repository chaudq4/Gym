package com.chauduong.gym.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.IOException;
import java.util.UUID;

public class AccountManager {
    private static final String USERS = "users";
    private static final String IMAGE_ACCOUT = "imageaccout";
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private AccountManagerListener accountManagerListener;
    private StorageReference mStorageReference;
    private Context mContext;

    public AccountManager(Context mContext, AccountManagerListener accountManagerListener) {
        this.mContext = mContext;
        this.accountManagerListener = accountManagerListener;
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseStorage = FirebaseStorage.getInstance("gs://gymgc-55cfd.appspot.com");
    }

    public void listenerAccount(User user) {
        mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.child(user.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                Log.i("chauanh", "onDataChange: " + u.toString());
                accountManagerListener.onUserChange(u);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changePass(User user, String oldPass, String newPass) {
        mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.child(user.getId()).child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String password = snapshot.getValue(String.class);
                if (oldPass.equalsIgnoreCase(password)) {
                    mDatabaseReference.child(user.getId()).child("password").setValue(newPass)
                            .addOnSuccessListener(command -> accountManagerListener.onChangePassSuccess(newPass))
                            .addOnFailureListener(command -> accountManagerListener.onChangePassFail());
                } else {
                    accountManagerListener.onChangePassFail();
                }
                mDatabaseReference.child(user.getId()).child("password").removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                accountManagerListener.onChangePassFail();
                mDatabaseReference.child(user.getId()).child("password").removeEventListener(this);
            }
        });
    }

    public void uploadImage(Uri filePath) {
        mStorageReference = mFirebaseStorage.getReference(IMAGE_ACCOUT);
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
                                accountManagerListener.onUploadImageAccountSuccess(downloadUrl.toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            accountManagerListener.onUploadImageAccountFail();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            accountManagerListener.onProgressUpload(progress);
                        }
                    });
        }
    }

    public void updateLinkAvatar(String url) {
        User user = new SessionManager(mContext).getUser();
        mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.child(user.getId()).child("avatar").setValue(url).addOnFailureListener(command -> accountManagerListener.onUploadImageAccountFail())
                .addOnSuccessListener(command -> accountManagerListener.onUpdateImageSuccess());
    }

    public void updateInformationAccount(User newUser) {

        User user = new SessionManager(mContext).getUser();
        mDatabaseReference = mFirebaseDatabase.getReference(USERS);
        mDatabaseReference.child(user.getId()).child("address").setValue(newUser.getAddress()).addOnFailureListener(command -> accountManagerListener.onUpdateInformationAccountFail());
        mDatabaseReference.child(user.getId()).child("dob").setValue(newUser.getDob()).addOnFailureListener(command -> accountManagerListener.onUpdateInformationAccountFail());
        mDatabaseReference.child(user.getId()).child("email").setValue(newUser.getEmail()).addOnFailureListener(command -> accountManagerListener.onUpdateInformationAccountFail());
        mDatabaseReference.child(user.getId()).child("male").setValue(newUser.isMale()).addOnFailureListener(command -> accountManagerListener.onUpdateInformationAccountFail());
        mDatabaseReference.child(user.getId()).child("name").setValue(newUser.getName()).addOnFailureListener(command -> accountManagerListener.onUpdateInformationAccountFail());
        accountManagerListener.onUpdateInformationAccountSuccess();

    }
}
