package com.chauduong.gym.ui;

import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ActivitySettingBinding;
import com.chauduong.gym.manager.session.SessionManager;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    ActivitySettingBinding activitySettingBinding;
    SessionManager mSessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        initSession();
        initView();
    }

    private void initView() {
        activitySettingBinding.swFinger.setChecked(mSessionManager.isFinger());
        activitySettingBinding.swFinger.setOnCheckedChangeListener(this);
    }

    private void initSession() {
        mSessionManager = new SessionManager(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mSessionManager.setKeyIsFinger(isChecked);
    }
}
