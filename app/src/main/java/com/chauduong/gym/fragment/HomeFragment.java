package com.chauduong.gym.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.TypeAdapter;
import com.chauduong.gym.adapter.TypeListener;
import com.chauduong.gym.databinding.FragmentHomeBinding;
import com.chauduong.gym.manager.DatabaseListener;
import com.chauduong.gym.manager.DatabaseManager;
import com.chauduong.gym.model.Type;
import com.chauduong.gym.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TypeListener, DatabaseListener {
    private static final int MSG_UPDATE_RV = 1;
    private static List<Type> mTypeList;
    @SuppressLint("StaticFieldLeak")
    static FragmentHomeBinding mFragmentHomeBinding;
    @SuppressLint("StaticFieldLeak")
    static TypeAdapter mTypeAdapter;

    public HomeFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return mFragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initDataType();
    }


    private void initView() {
        if (mTypeList == null) mTypeList = new ArrayList<>();
        if (mTypeAdapter == null) mTypeAdapter = new TypeAdapter(mTypeList, getContext());
        mTypeAdapter.setmTypeListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mFragmentHomeBinding.rvType.setLayoutManager(llm);
        mFragmentHomeBinding.rvType.setAdapter(mTypeAdapter);

    }

    private void initDataType() {
        DatabaseManager.getInstance(getActivity(), this).getAllType();
    }

    @Override
    public void onTypeClick(Type type) {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra("TYPE", type);
        startActivity(intent);
    }

    @Override
    public void onSuccessGetAllType(List<Type> typeList) {
        mTypeList.clear();
        mTypeList.addAll(typeList);
        mTypeAdapter.setmTypeList(mTypeList);
    }

    @Override
    public void onCancelGetAllTye(String msg) {

    }


}
