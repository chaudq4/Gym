package com.chauduong.gym.manager.database;

import com.chauduong.gym.model.Type;

import java.util.List;

public interface DatabaseListener {
    void onSuccessGetAllType(List<Type> typeList);
    void onCancelGetAllTye(String msg);
    void onSearchTypeSuccess(List<Type> typeList);
    void onSearchTypeError(String msg);
}
