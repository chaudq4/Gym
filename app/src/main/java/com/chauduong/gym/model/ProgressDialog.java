package com.chauduong.gym.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.DialogProgressBinding;

public class ProgressDialog extends DialogFragment {

    private static final String TITLE = "title";

    public static ProgressDialog newInstance(String title) {
        ProgressDialog dialog = new ProgressDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        dialog.setArguments(args);
        return dialog;
    }

    public ProgressDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        DialogProgressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_progress, null, false);
        String data = getArguments().getString(TITLE, "");
        AlertDialog alertDialog=new AlertDialog.Builder(activity, R.style.Base_Theme_AppCompat_Dialog)
                .setView(binding.getRoot())
                .create();

        return alertDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

}
