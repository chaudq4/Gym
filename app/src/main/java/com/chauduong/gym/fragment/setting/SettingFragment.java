package com.chauduong.gym.fragment.setting;


import android.content.Intent;
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
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.ui.LoginActivity;

public class SettingFragment extends Fragment implements View.OnClickListener {
    FragmentSettingBinding mFragmentSettingBinding;
    private SessionManager mSessionManager;

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return mFragmentSettingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentSettingBinding.layoutSetting.btnLogout.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                mSessionManager = new SessionManager(getContext());
                mSessionManager.clearSession();
                getActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}