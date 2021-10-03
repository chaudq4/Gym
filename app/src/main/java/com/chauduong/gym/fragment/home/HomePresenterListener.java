package com.chauduong.gym.fragment.home;

import com.chauduong.gym.model.Type;

import java.util.List;

public interface HomePresenterListener {
    void onGetAllTypeSuccess(List<Type> typeList);
    void onGetAllTypeError(String msg);
    void onSearchTypeSuccess(List<Type> typeList);
    void onSearchTypeError(String msg);
}
