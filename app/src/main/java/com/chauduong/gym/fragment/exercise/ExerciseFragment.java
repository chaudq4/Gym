package com.chauduong.gym.fragment.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.ExcersieListener;
import com.chauduong.gym.adapter.ExcersiseAdapter;
import com.chauduong.gym.databinding.FragmentExerciseBinding;
import com.chauduong.gym.fragment.detail.DetailFragment;
import com.chauduong.gym.model.Exercise;
import com.chauduong.gym.model.Type;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment implements ExcersieListener {
    FragmentExerciseBinding mExerciseBinding;
    Type mType;
    ExcersiseAdapter mExcersiseAdapter;
    List<Exercise> mExerciseList;
    DetailFragment mDetailFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mExerciseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise, container, false);
        return mExerciseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        mType = (Type) bundle.getSerializable("TYPE");
        initData();
        initView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        if (mExcersiseAdapter == null) {
            mExcersiseAdapter = new ExcersiseAdapter(mExerciseList, getContext());
        }
        mExerciseBinding.rvListEx.setAdapter(mExcersiseAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mExerciseBinding.rvListEx.setLayoutManager(linearLayoutManager);
        mExcersiseAdapter.setmExcersieListener(this);
    }

    private void initData() {
        if (mType != null)
            mExerciseBinding.txtNameType.setText(mType.getName());
        if (mExerciseList == null) {
            mExerciseList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Exercise exercise = new Exercise();
                exercise.setName("Cử tạ với tạ đơn " + i);
                exercise.setEquipment("Tạ đòn " + i);
                exercise.setForce("Keo " + i);
                exercise.setLevel("Vua " + i);
                exercise.setMain("Co dui sau " + i);
                exercise.setType("Cardio " + i);
                mExerciseList.add(exercise);
            }
        }
    }

    @Override
    public void onExerciseClick(Exercise exercise) {
        mDetailFragment = new DetailFragment();
        getFragmentManager().beginTransaction().replace(R.id.fl, mDetailFragment)
                .addToBackStack(null)
                .commit();
    }

}
