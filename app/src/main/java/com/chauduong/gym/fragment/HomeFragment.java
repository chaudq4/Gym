package com.chauduong.gym.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.TypeAdapter;
import com.chauduong.gym.adapter.TypeListener;
import com.chauduong.gym.databinding.FragmentHomeBinding;
import com.chauduong.gym.model.Type;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TypeListener {
    List<Type> mTypeList;
    FragmentHomeBinding mFragmentHomeBinding;
    TypeAdapter mTypeAdapter;

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
        initDataType();
        if (mTypeAdapter == null)
            mTypeAdapter = new TypeAdapter(mTypeList, getContext());
        mTypeAdapter.setmTypeListener(this);
        mFragmentHomeBinding.rvType.setAdapter(mTypeAdapter);
        mFragmentHomeBinding.rvType.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initDataType() {
        if (mTypeList == null) {
            mTypeList = new ArrayList<>();
            mTypeList.add(new Type(R.drawable.ic_type_chest, getString(R.string.begin_type) + " " + getString(R.string.chest)));
            mTypeList.add(new Type(R.drawable.ic_type_chest, getString(R.string.begin_type) + " " + getString(R.string.lats)));
            mTypeList.add(new Type(R.drawable.ic_type_chest, getString(R.string.begin_type) + " " + getString(R.string.arm)));
            mTypeList.add(new Type(R.drawable.ic_type_chest, getString(R.string.begin_type) + " " + getString(R.string.shoulder)));
            mTypeList.add(new Type(R.drawable.ic_type_chest, getString(R.string.begin_type) + " " + getString(R.string.abs)));
            mTypeList.add(new Type(R.drawable.ic_type_chest, getString(R.string.begin_type) + " " + getString(R.string.glute)));
            mTypeList.add(new Type(R.drawable.ic_type_chest, getString(R.string.begin_type) + " " + getString(R.string.leg)));

        }
    }

    @Override
    public void onTypeClick(Type type) {

    }
}
