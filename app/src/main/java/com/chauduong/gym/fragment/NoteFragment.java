package com.chauduong.gym.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.FragmentNoteBinding;

public class NoteFragment extends Fragment {
    FragmentNoteBinding mFragmentNoteBinding;
    public NoteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentNoteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_note,container,false);
        return mFragmentNoteBinding.getRoot();
    }
}