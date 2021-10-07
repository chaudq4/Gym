package com.chauduong.gym.fragment.mess;

import android.content.Context;

import com.chauduong.gym.model.User;

import java.util.List;

public class MessPresenter implements MessManagerListener {
    Context mContext;
    MessPresenterListener mMessPresenterListener;
    MessManager mMessManager;
    public MessPresenter(Context mContext, MessPresenterListener mMessPresenterListener) {
        this.mContext = mContext;
        this.mMessPresenterListener = mMessPresenterListener;
        mMessManager= new MessManager(mContext,this);
    }
    public void getAllListUser(){
        if (mMessManager!=null){
            mMessManager.getAllListUser();
        }
    }

    @Override
    public void onGetAllUserSuccess(List<User> userList) {
        if (mMessPresenterListener!=null)
        mMessPresenterListener.onGetAllUserSuccess(userList);
    }

    @Override
    public void onGetAllUserError(String msg) {
        if (mMessPresenterListener!=null){
            mMessPresenterListener.onGetAllUserError(msg);
        }

    }
}
