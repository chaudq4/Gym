package com.chauduong.gym.fragment.personal;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.DialogAddinfomationbodyBinding;
import com.chauduong.gym.databinding.DialogConfirmLogoutBinding;
import com.chauduong.gym.databinding.FragmentPersonalBinding;
import com.chauduong.gym.model.BodyInformation;
import com.chauduong.gym.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private FragmentPersonalBinding mFragmentPersonalBinding;
    private PersonalViewModel mPersonalViewModel;
    private AlertDialog addDialog;

    public PersonalFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentPersonalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal, container, false);
        return mFragmentPersonalBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewModel();
    }

    private void initViewModel() {
        mPersonalViewModel = ViewModelProviders.of(this).get(PersonalViewModel.class);
        mPersonalViewModel.getIsAddSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (addDialog != null && addDialog.isShowing()) {
                    addDialog.dismiss();
                    if (aBoolean) {
                        Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getContext().getString(R.string.add_bodyinfor_success), Util.TYPE_SNACK_BAR_SUCCESS);
                    } else {
                        Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getContext().getString(R.string.add_bodyinfor_faild), Util.TYPE_SNACK_BAR_WRONG);
                    }
                }
            }
        });
    }

    private void initView() {
        mFragmentPersonalBinding.btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                showDialogAdd();
                break;
            default:
                break;
        }
    }

    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogAddinfomationbodyBinding dialogAddinfomationbodyBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_addinfomationbody, null, false);
        builder.setCustomTitle(dialogAddinfomationbodyBinding.getRoot());
        builder.setIcon(R.drawable.ic_baseline_addchart_24);
        addDialog = builder.create();
        addDialog.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
        addDialog.setCancelable(false);
        addDialog.setCanceledOnTouchOutside(false);
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, dialogAddinfomationbodyBinding.edtDate);
            }

        };
        dialogAddinfomationbodyBinding.edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dialogAddinfomationbodyBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addDialog != null && addDialog.isShowing())
                    addDialog.dismiss();
            }
        });
        dialogAddinfomationbodyBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = dialogAddinfomationbodyBinding.getRoot();
                boolean isFullValid = Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtHeight)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtWeight)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtMuscle)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtFat)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtProtetin)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtMineral)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtWater);
                if (isFullValid) {
                    String height = dialogAddinfomationbodyBinding.edtHeight.getText().toString();
                    String weight = dialogAddinfomationbodyBinding.edtWeight.getText().toString();
                    String muscle = dialogAddinfomationbodyBinding.edtMuscle.getText().toString();
                    String fat = dialogAddinfomationbodyBinding.edtFat.getText().toString();
                    String protein = dialogAddinfomationbodyBinding.edtProtetin.getText().toString();
                    String mineral = dialogAddinfomationbodyBinding.edtMineral.getText().toString();
                    String water = dialogAddinfomationbodyBinding.edtWater.getText().toString();
                    String date = dialogAddinfomationbodyBinding.edtDate.getText().toString();
                    BodyInformation bodyInformation = new BodyInformation(height, weight, muscle, fat, protein, mineral, water, date);
                    mPersonalViewModel.addBodyInformation(bodyInformation);
                }
            }
        });
        addDialog.show();
        addDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void updateLabel(Calendar myCalendar, EditText editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }
}