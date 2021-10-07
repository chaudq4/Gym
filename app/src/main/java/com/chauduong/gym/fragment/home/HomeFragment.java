package com.chauduong.gym.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.TypeAdapter;
import com.chauduong.gym.databinding.FragmentHomeBinding;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.model.Type;
import com.chauduong.gym.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TypeAdapter.TypeListener, HomePresenterListener, SearchView.OnQueryTextListener {
    private static final String TAG = "HomeFragment";
    private  final int MSG_UPDATE_RV = 1;
    private  List<Type> mTypeList;
    private  FragmentHomeBinding mFragmentHomeBinding;
    private  static TypeAdapter mTypeAdapter;
    private HomePresenter mHomePresenter;

    public HomeFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        mFragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return mFragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        initView();
        initPresenter();
        initDataType();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    private void initPresenter() {
        if (mHomePresenter == null) mHomePresenter = new HomePresenter(getContext(), this);
    }


    private void initView() {
        if (mTypeList == null) mTypeList = new ArrayList<>();
        if (mTypeAdapter == null) mTypeAdapter = new TypeAdapter(mTypeList, getContext());
        mTypeAdapter.setmTypeListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mFragmentHomeBinding.rvType.setLayoutManager(llm);
        mFragmentHomeBinding.rvType.setAdapter(mTypeAdapter);
        mFragmentHomeBinding.sv.setOnQueryTextListener(this);
    }

    private void initDataType() {
        DialogManager.getInstance(getContext()).showProgressDialog(getFragmentManager(), "Đang tải dữ liệu");
        mHomePresenter.getAllType();
    }

    @Override
    public void onTypeClick(Type type) {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra("TYPE", type);
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() != 0)
            mHomePresenter.searchType(query);
        else
            mHomePresenter.getAllType();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() != 0)
            mHomePresenter.searchType(newText);
        else
            mHomePresenter.getAllType();
        return false;
    }


    @Override
    public void onGetAllTypeSuccess(List<Type> typeList) {
        DialogManager.getInstance(getContext()).dissmissProgressDialog();
        mTypeAdapter.getmTypeList().clear();
        mTypeAdapter.getmTypeList().addAll(typeList);
        mTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAllTypeError(String msg) {
        DialogManager.getInstance(getContext()).dissmissProgressDialog();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchTypeSuccess(List<Type> typeList) {
        mTypeList.clear();
        mTypeList.addAll(typeList);
        mTypeAdapter.setmTypeList(mTypeList);
    }

    @Override
    public void onSearchTypeError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
