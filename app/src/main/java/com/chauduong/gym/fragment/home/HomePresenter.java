package com.chauduong.gym.fragment.home;

import android.content.Context;

import com.chauduong.gym.manager.database.DatabaseListener;
import com.chauduong.gym.manager.database.DatabaseManager;
import com.chauduong.gym.model.Type;

import java.util.List;

public class HomePresenter implements DatabaseListener {
    private Context mContext;
    private HomePresenterListener mHomePresenterListener;

    public HomePresenter(Context mContext, HomePresenterListener mHomePresenterListener) {
        this.mContext = mContext;
        this.mHomePresenterListener = mHomePresenterListener;
    }

    public void getAllType(){
        DatabaseManager.getInstance(mContext,this).getAllType();
    }

    public void searchType(String key){
        DatabaseManager.getInstance(mContext,this).searchType(key);
    }
    @Override
    public void onSuccessGetAllType(List<Type> typeList) {
        if (mHomePresenterListener!=null){
            mHomePresenterListener.onGetAllTypeSuccess(typeList);
        }
    }

    @Override
    public void onCancelGetAllTye(String msg) {
        if(mHomePresenterListener!=null){
            mHomePresenterListener.onGetAllTypeError(msg);
        }
    }

    @Override
    public void onSearchTypeSuccess(List<Type> typeList) {
        if(mHomePresenterListener!=null)
            mHomePresenterListener.onSearchTypeSuccess(typeList);
    }

    @Override
    public void onSearchTypeError(String msg) {
        if (mHomePresenterListener!=null)
            mHomePresenterListener.onSearchTypeError(msg);

    }


}
