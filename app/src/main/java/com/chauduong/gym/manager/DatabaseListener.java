package com.chauduong.gym.manager;

import com.chauduong.gym.model.Type;

import java.util.List;

public interface DatabaseListener {
    void onSuccessGetAllType(List<Type> typeList);
    void onCancelGetAllTye(String msg);
}
