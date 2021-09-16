package com.chauduong.gym.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ActivityHomeBinding;
import com.chauduong.gym.fragment.ExerciseFragment;
import com.chauduong.gym.manager.DialogManager;
import com.chauduong.gym.model.Type;

public class HomeActivity extends AppCompatActivity {
    Type mType;
    ActivityHomeBinding mActivityHomeBinding;
    ExerciseFragment mExerciseFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initData();
        initView();
    }

    private void initView() {
        mExerciseFragment = new ExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TYPE", mType);
        mExerciseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl, mExerciseFragment)
                .addToBackStack(null)
                .commit();
    }

    private void initData() {
        if (mType == null)
            mType = (Type) getIntent().getSerializableExtra("TYPE");
    }

    @Override
    public void onBackPressed() {
        if (DialogManager.getInstance(this).isShow()) {
            DialogManager.getInstance(this).dismiss();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else
            finish();

    }
}
