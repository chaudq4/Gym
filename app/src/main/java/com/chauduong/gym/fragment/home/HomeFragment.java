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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.TypeAdapter;
import com.chauduong.gym.databinding.FragmentHomeBinding;
import com.chauduong.gym.manager.database.DatabaseManager;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.model.Type;
import com.chauduong.gym.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment implements TypeAdapter.TypeListener, SearchView.OnQueryTextListener {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding mFragmentHomeBinding;
    private TypeAdapter mTypeAdapter;
    private HomeViewModel homeViewModel;

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
        initViewModel();
        initView();
        initDataType();
    }


    private void initViewModel() {
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        homeViewModel.getListMutableLiveData().observe(this, new Observer<List<Type>>() {
            @Override
            public void onChanged(List<Type> typeList) {
                DialogManager.getInstance(getContext()).dissmissProgressDialog();
                mTypeAdapter.setmTypeList(typeList);
                mTypeAdapter.notifyDataSetChanged();
            }
        });

        homeViewModel.getCancelGetAll().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                DialogManager.getInstance(getContext()).dissmissProgressDialog();
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        homeViewModel.getSearchError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                DialogManager.getInstance(getContext()).dissmissProgressDialog();
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        if (mTypeAdapter == null)
            mTypeAdapter = new TypeAdapter(homeViewModel.getListMutableLiveData().getValue(), getContext());

        mTypeAdapter.setmTypeListener(this);
        mFragmentHomeBinding.rvType.setAdapter(mTypeAdapter);
        mFragmentHomeBinding.sv.setOnQueryTextListener(this);
    }

    private void initDataType() {
        DialogManager.getInstance(getContext()).showProgressDialog(getFragmentManager(), getString(R.string.load_data));
        homeViewModel.getAllType();
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
            homeViewModel.searchType(query);
        else
            homeViewModel.getAllType();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() != 0)
            homeViewModel.searchType(newText);
        else
            homeViewModel.getAllType();
        return false;
    }

}
