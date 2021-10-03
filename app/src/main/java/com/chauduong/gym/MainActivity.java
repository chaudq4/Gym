package com.chauduong.gym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chauduong.gym.adapter.ViewPagerFragmentAdapter;
import com.chauduong.gym.databinding.ActivityMainBinding;
import com.chauduong.gym.databinding.ItemTabBinding;
import com.chauduong.gym.fragment.food.FoodFragment;
import com.chauduong.gym.fragment.home.HomeFragment;
import com.chauduong.gym.fragment.note.NoteFragment;
import com.chauduong.gym.fragment.personal.PersonalFragment;
import com.chauduong.gym.fragment.setting.SettingFragment;
import com.chauduong.gym.utils.Util;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

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
            R.drawable.ic_baseline_more_vert_24
    };
    private boolean doubleBackToExitPressedOnce=false;

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
            ItemTabBinding itemTabBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.item_tab,null,false);
            itemTabBinding.icon.setImageResource(icon[i]);
            activityMainBinding.tblayout.getTabAt(i).setCustomView(itemTabBinding.getRoot());
        }
        activityMainBinding.tblayout.addOnTabSelectedListener(this);
        Util.updateIconTab(activityMainBinding.tblayout.getTabAt(activityMainBinding.tblayout.getSelectedTabPosition()).getCustomView().findViewById(R.id.icon),getResources().getDimensionPixelSize(R.dimen.icon_tab_size_selected),getResources().getDimensionPixelSize(R.dimen.icon_tab_size_selected));
        ((ImageView) activityMainBinding.tblayout.getTabAt(activityMainBinding.tblayout.getSelectedTabPosition()).getCustomView().findViewById(R.id.icon)).setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mFoodFragment = new FoodFragment();
        mNoteFragment = new NoteFragment();
        mPersonalFragment = new PersonalFragment();
        mSettingFragment = new SettingFragment();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()!=0){
            super.onBackPressed();
        }else {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.back_again_to_exit), Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Util.updateIconTab(tab.getCustomView().findViewById(R.id.icon),getResources().getDimensionPixelSize(R.dimen.icon_tab_size_selected),
                getResources().getDimensionPixelSize(R.dimen.icon_tab_size_selected));
        ((ImageView)tab.getCustomView().findViewById(R.id.icon)).setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Util.updateIconTab(tab.getCustomView().findViewById(R.id.icon),getResources().getDimensionPixelSize(R.dimen.icon_tab_size_unselected),
                getResources().getDimensionPixelSize(R.dimen.icon_tab_size_unselected));
        ((ImageView)tab.getCustomView().findViewById(R.id.icon)).setColorFilter(getColor(R.color.tab_unselected), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Util.updateIconTab(tab.getCustomView().findViewById(R.id.icon),getResources().getDimensionPixelSize(R.dimen.icon_tab_size_selected),
                getResources().getDimensionPixelSize(R.dimen.icon_tab_size_selected));
        ((ImageView)tab.getCustomView().findViewById(R.id.icon)).setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }
}