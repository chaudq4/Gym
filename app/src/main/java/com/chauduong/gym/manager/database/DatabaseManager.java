package com.chauduong.gym.manager.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.chauduong.gym.model.Exercise;
import com.chauduong.gym.model.Type;
import com.chauduong.gym.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String EXERCISE = "exercise";
    private static final String TYPE = "type";

    @SuppressLint("StaticFieldLeak")
    private static DatabaseManager instance;
    Context mContext;
    DatabaseReference mDatabaseReference;
    DatabaseListener mDatabaseListener;
    FirebaseDatabase mFirebaseDatabase;
    private List<Type> typeList;

    private DatabaseManager(Context mContext, DatabaseListener databaseListener) {
        this.mContext = mContext;
        this.mDatabaseListener = databaseListener;
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    public static DatabaseManager getInstance(Context mContext, DatabaseListener databaseListener) {
        if (instance == null) instance = new DatabaseManager(mContext, databaseListener);
        return instance;
    }

    public void searchType(String key) {
        mDatabaseReference = mFirebaseDatabase.getReference(TYPE);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (typeList == null) typeList = new ArrayList<>();
                typeList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Type type = d.getValue(Type.class);
                    if (type.getName().contains(key))
                        typeList.add(type);
                }
                if (mDatabaseListener != null)
                    mDatabaseListener.onSearchTypeSuccess(typeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (mDatabaseListener != null)
                    mDatabaseListener.onSearchTypeError(databaseError.getMessage());
            }
        });
    }

    public void getAllType() {
        mDatabaseReference = mFirebaseDatabase.getReference(TYPE);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (typeList == null) typeList = new ArrayList<>();
                typeList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Type type = d.getValue(Type.class);
                    typeList.add(type);
                }
                if (mDatabaseListener != null)
                    mDatabaseListener.onSuccessGetAllType(typeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (mDatabaseListener != null)
                    mDatabaseListener.onCancelGetAllTye(databaseError.getMessage());
            }
        });
    }

    public void insertType() {
        mDatabaseReference = FirebaseDatabase.getInstance("https://gymgc-55cfd-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(TYPE);
        String idType = mDatabaseReference.push().getKey();
        Type mType = initType();
        mType.setId(idType);
        mDatabaseReference.child(idType).setValue(mType);
    }

    private List<Exercise> initExerciseList() {
        List<Exercise> exercises = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Exercise e = new Exercise();
            List<String> urlList = new ArrayList<>();
            urlList.add("ex" + i + ": link image1");
            urlList.add("ex" + i + ": link image2");
            urlList.add("ex" + i + ": link image3");
            e.setImageUrlList(urlList);
            e.setVideoUrl("ex+" + i + ": url video");
            e.setType("ex" + i + ": the loai");
            e.setMain("nhom co chinh");
            e.setLevel("muc do");
            e.setForce("luc keo");
            e.setEquipment("dung cu");
            e.setOther("nhom co khac");
            e.setInstruction("Huong dan tap");
            e.setName("Ten bai tap");
            exercises.add(e);
        }
        return exercises;

    }

    private Type initType() {
        Type type = new Type();
        type.setId("id");
        type.setUrlIcon("https://firebasestorage.googleapis.com/v0/b/gymgc-55cfd.appspot.com/o/ic_type_chest.jpg?alt=media&token=62556502-2a7c-49c9-a28d-ec5fb4f20f28");
        type.setName("ten nhom co");
        type.setmExerciseList(initExerciseList());
        return type;
    }

    private User signUp(User user) {

        return null;
    }

}