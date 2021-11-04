package com.chauduong.gym.fragment.bodyinformation;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ActivityBodyInformationBinding;
import com.chauduong.gym.databinding.DialogUpdateBodyInformationBinding;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.model.BodyInformation;
import com.chauduong.gym.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class BodyInformationActivity extends AppCompatActivity implements BodyInformationListener {
    private ActivityBodyInformationBinding mActivityBodyInformationBinding;
    private BodyInformationViewModel mBodyInformationViewModel;
    private BodyInformationAdapter bodyInformationAdapter;
    private AlertDialog updateAlertDialog;
    private DialogUpdateBodyInformationBinding mDialogUpdateBodyInformationBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBodyInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_body_information);
        initView();
        initViewModel();
    }


    private void initViewModel() {
        mBodyInformationViewModel = ViewModelProviders.of(this).get(BodyInformationViewModel.class);
        mBodyInformationViewModel.getAllBodyInformation();
        mBodyInformationViewModel.getBodyListMutableLiveData().observe(this, new Observer<List<BodyInformation>>() {
            @Override
            public void onChanged(List<BodyInformation> bodyInformationList) {
                bodyInformationAdapter.setBodyInformationList(bodyInformationList);
                bodyInformationAdapter.notifyDataSetChanged();
            }
        });
        mBodyInformationViewModel.getIsUpdateSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (updateAlertDialog != null && updateAlertDialog.isShowing()) {
                    if (aBoolean) {
                        Util.showSnackbar(mActivityBodyInformationBinding.getRoot(), getString(R.string.update_body_success), Util.TYPE_SNACK_BAR_SUCCESS);
                    } else {
                        Util.showSnackbar(mActivityBodyInformationBinding.getRoot(), getString(R.string.update_body_error), Util.TYPE_SNACK_BAR_WRONG);
                    }
                    updateAlertDialog.dismiss();
                }
            }
        });
    }

    private void initView() {
        List<BodyInformation> bodyInformations = new ArrayList<>();
        bodyInformationAdapter = new BodyInformationAdapter(bodyInformations, this);
        mActivityBodyInformationBinding.rvBodyInformation.setAdapter(bodyInformationAdapter);
    }


    @SuppressLint("ResourceType")
    @Override
    public void onBodyInformationClick(BodyInformation bodyInformation) {
        mDialogUpdateBodyInformationBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_update_body_information, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogSlideAnim);
        builder.setView(mDialogUpdateBodyInformationBinding.getRoot());
        mDialogUpdateBodyInformationBinding.setBody(bodyInformation);
        updateAlertDialog = builder.create();
        updateAlertDialog.show();
        updateAlertDialog.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
        updateAlertDialog.setCanceledOnTouchOutside(false);
        mDialogUpdateBodyInformationBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateAlertDialog != null && updateAlertDialog.isShowing())
                    updateAlertDialog.dismiss();
            }
        });
        mDialogUpdateBodyInformationBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateAlertDialog != null && updateAlertDialog.isShowing())
                    mBodyInformationViewModel.updateBodyInformation(mDialogUpdateBodyInformationBinding.getBody());
            }
        });
        mDialogUpdateBodyInformationBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateAlertDialog != null && updateAlertDialog.isShowing())
                    mBodyInformationViewModel.deleteBodyInformation(mDialogUpdateBodyInformationBinding.getBody());
            }
        });
        updateAlertDialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(updateAlertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        updateAlertDialog.getWindow().setAttributes(lp);
    }
}
