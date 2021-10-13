package com.chauduong.gym.fragment.home;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.manager.database.DatabaseListener;
import com.chauduong.gym.manager.database.DatabaseManager;
import com.chauduong.gym.model.Type;

import java.util.List;

public class HomeViewModel extends ViewModel implements DatabaseListener {
    private MediatorLiveData<List<Type>> listMutableLiveData = new MediatorLiveData<>();
    private MutableLiveData<String> cancelGetAll = new MutableLiveData<>();
    private MutableLiveData<String> searchError = new MutableLiveData<>();
    private DatabaseManager mDatabaseManager = new DatabaseManager(this);



    public MutableLiveData<List<Type>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public MutableLiveData<String> getCancelGetAll() {
        return cancelGetAll;
    }

    public MutableLiveData<String> getSearchError() {
        return searchError;
    }

    public void getAllType() {
        mDatabaseManager.getAllType();
    }

    public void searchType(String key) {
        mDatabaseManager.searchType(key);
    }

    @Override
    public void onSuccessGetAllType(List<Type> typeList) {
        listMutableLiveData.setValue(typeList);
    }

    @Override
    public void onCancelGetAllTye(String msg) {
        cancelGetAll.setValue(msg);
    }

    @Override
    public void onSearchTypeSuccess(List<Type> typeList) {
        listMutableLiveData.setValue(typeList);
    }

    @Override
    public void onSearchTypeError(String msg) {
        searchError.setValue(msg);
    }

}
