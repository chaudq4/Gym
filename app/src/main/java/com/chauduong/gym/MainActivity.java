package com.chauduong.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.util.Log;

import com.chauduong.gym.adapter.ViewPagerFragmentAdapter;
import com.chauduong.gym.databinding.ActivityMainBinding;
import com.chauduong.gym.fragment.FoodFragment;
import com.chauduong.gym.fragment.HomeFragment;
import com.chauduong.gym.fragment.NoteFragment;
import com.chauduong.gym.fragment.PersonalFragment;
import com.chauduong.gym.fragment.SettingFragment;
import com.chauduong.gym.manager.DatabaseListener;
import com.chauduong.gym.manager.DatabaseManager;
import com.chauduong.gym.model.Type;
import com.chauduong.gym.utils.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding activityMainBinding;
    HomeFragment mHomeFragment;
    FoodFragment mFoodFragment;
    NoteFragment mNoteFragment;
    PersonalFragment mPersonalFragment;
    SettingFragment mSettingFragment;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    int icon[] = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_food_bank_24,
            R.drawable.ic_baseline_sticky_note_2_24,
            R.drawable.ic_baseline_person_24,
            R.drawable.ic_baseline_settings_24
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    private void initView() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initFragment();
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPagerFragmentAdapter.addFragment(mHomeFragment);
        mViewPagerFragmentAdapter.addFragment(mFoodFragment);
        mViewPagerFragmentAdapter.addFragment(mNoteFragment);
        mViewPagerFragmentAdapter.addFragment(mPersonalFragment);
        mViewPagerFragmentAdapter.addFragment(mSettingFragment);
        activityMainBinding.vpager.setAdapter(mViewPagerFragmentAdapter);
        activityMainBinding.tblayout.setupWithViewPager(activityMainBinding.vpager);
        for (int i = 0; i < mViewPagerFragmentAdapter.getCount(); i++) {
            activityMainBinding.tblayout.getTabAt(i).setIcon(icon[i]);
        }
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mFoodFragment = new FoodFragment();
        mNoteFragment = new NoteFragment();
        mPersonalFragment = new PersonalFragment();
        mSettingFragment = new SettingFragment();
    }
}