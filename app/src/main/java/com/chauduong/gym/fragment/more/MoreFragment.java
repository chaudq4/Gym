package com.chauduong.gym.fragment.more;


import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.DialogConfirmLogoutBinding;
import com.chauduong.gym.databinding.FragmentMoreBinding;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.ui.LoginActivity;
import com.chauduong.gym.ui.SettingActivity;

import java.util.Objects;

public class MoreFragment extends Fragment implements View.OnClickListener {
    FragmentMoreBinding mFragmentMoreBinding;
    private SessionManager mSessionManager;
    private MoreViewModel moreViewModel;
    private AlertDialog alertDialogLogout;

    public MoreFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentMoreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        return mFragmentMoreBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentMoreBinding.layoutMore.btnLogout.setOnClickListener(this::onClick);
        mFragmentMoreBinding.layoutMore.btnSetting.setOnClickListener(this);
        mSessionManager = new SessionManager(getContext());
        initViewModel();
    }

    private void initViewModel() {
        moreViewModel = ViewModelProviders.of(getActivity()).get(MoreViewModel.class);
        moreViewModel.setListenerUser(mSessionManager.getUser());
        mFragmentMoreBinding.layoutIndex.setViewModel(moreViewModel);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                showConfirmLogout();
                break;
            case R.id.btnSetting:
                onSettingClick();
                break;
            default:
                break;
        }
    }

    private void onSettingClick() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivity(intent);
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
        dialogConfirmLogoutBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialogLogout != null && alertDialogLogout.isShowing())
                    alertDialogLogout.dismiss();
            }
        });
        dialogConfirmLogoutBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialogLogout != null && alertDialogLogout.isShowing())
                    alertDialogLogout.dismiss();
                mSessionManager.clearSession();
                mSessionManager.getUser().setOnline(false);
                moreViewModel.updateStatusUser(mSessionManager.getUser());
                Objects.requireNonNull(getActivity()).finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        alertDialogLogout.getWindow().setGravity(CENTER);
    }
}