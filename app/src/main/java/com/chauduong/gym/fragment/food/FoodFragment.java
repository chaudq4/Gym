package com.chauduong.gym.fragment.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.FragmentFoodBinding;

public class FoodFragment extends Fragment {
    FragmentFoodBinding mFragmentFoodBinding;
    FoodViewModel foodViewModel;

    public FoodFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFoodBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false);
        return mFragmentFoodBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodViewModel = ViewModelProviders.of(getActivity()).get(FoodViewModel.class);

        foodViewModel.getStringMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
        foodViewModel.gen();
    }
}
