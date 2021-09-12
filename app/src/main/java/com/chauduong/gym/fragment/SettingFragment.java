package com.chauduong.gym.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {
    FragmentSettingBinding mFragmentSettingBinding;

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return mFragmentSettingBinding.getRoot();
    }
}