package com.chauduong.gym.fragment.setting;


import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.DialogConfirmLogoutBinding;
import com.chauduong.gym.databinding.FragmentSettingBinding;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.ui.LoginActivity;
import com.chauduong.gym.utils.Util;

import java.util.Objects;

public class SettingFragment extends Fragment implements View.OnClickListener {
    FragmentSettingBinding mFragmentSettingBinding;
    private SessionManager mSessionManager;
    private SettingViewModel settingViewModel;
    private AlertDialog alertDialogLogout;

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
        mSessionManager = new SessionManager(getContext());
        initViewModel();
    }

    private void initViewModel() {
        settingViewModel = ViewModelProviders.of(getActivity()).get(SettingViewModel.class);
        settingViewModel.setListenerUser(mSessionManager.getUser());
        mFragmentSettingBinding.layoutIndex.setViewModel(settingViewModel);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                showConfirmLogout();
                break;
            case R.id.btnOK:
                if (alertDialogLogout != null && alertDialogLogout.isShowing())
                    alertDialogLogout.dismiss();
                mSessionManager.clearSession();
                Objects.requireNonNull(getActivity()).finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCancel:
                if (alertDialogLogout != null && alertDialogLogout.isShowing())
                    alertDialogLogout.dismiss();
                break;
            default:
                break;
        }
    }

    private void showConfirmLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogConfirmLogoutBinding dialogConfirmLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_confirm_logout, null, false);
        builder.setView(dialogConfirmLogoutBinding.getRoot());
        alertDialogLogout = builder.create();
        alertDialogLogout.show();
        alertDialogLogout.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
        alertDialogLogout.setCancelable(false);
        alertDialogLogout.setCanceledOnTouchOutside(false);
        dialogConfirmLogoutBinding.btnCancel.setOnClickListener(this);
        dialogConfirmLogoutBinding.btnOK.setOnClickListener(this);
        alertDialogLogout.getWindow().setGravity(CENTER);
    }
}