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
import com.chauduong.gym.databinding.FragmentPersonalBinding;

public class PersonalFragment extends Fragment {
    FragmentPersonalBinding mFragmentPersonalBinding;

    public PersonalFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentPersonalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal, container, false);
        return mFragmentPersonalBinding.getRoot();
    }
}