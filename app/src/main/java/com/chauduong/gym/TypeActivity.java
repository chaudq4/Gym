package com.chauduong.gym;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.chauduong.gym.databinding.ActivityTypeBinding;
import com.chauduong.gym.model.Type;

public class TypeActivity extends AppCompatActivity {
    ActivityTypeBinding mActivityTypeBinding;
    Type mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityTypeBinding = DataBindingUtil.setContentView(this, R.layout.activity_type);
        initData();
    }

    private void initData() {
        mType = (Type) getIntent().getSerializableExtra("TYPE");
        if (mType != null)
            mActivityTypeBinding.txtNameType.setText(mType.getName());
    }
}
