package com.chauduong.gym.fragment.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.FragmentFoodBinding;

public class FoodFragment extends Fragment {
    FragmentFoodBinding mFragmentFoodBinding;

    public FoodFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFoodBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false);
        return mFragmentFoodBinding.getRoot();
    }
}
