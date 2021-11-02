package com.chauduong.gym.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ActivityAccountBinding;
import com.chauduong.gym.databinding.DialogChangePassBinding;
import com.chauduong.gym.databinding.DialogProgressUploadBinding;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.User;
import com.chauduong.gym.utils.Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, DatePickerDialog.OnDateSetListener {
    private static final int SELECT_IMAGE_FROM_GALLERY = 1;
    private ActivityAccountBinding activityAccountBinding;
    private AccountViewModel accountViewModel;
    private SessionManager mSessionManager;
    private AlertDialog mChangePassDialog;
    private DialogChangePassBinding dialogChangePassBinding;
    private boolean isValidOld;
    private boolean isValidNew;
    private boolean isValidRenew;
    private DialogProgressUploadBinding dialogProgressBinding;
    private AlertDialog progressUploadDialog;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        initSession();
        initViewModel();
        initView();
    }

    private void initView() {
        activityAccountBinding.btnChangePass.setOnClickListener(this);
        activityAccountBinding.btnUpdateImage.setOnClickListener(this);
        activityAccountBinding.btnUpdate.setOnClickListener(this);
        activityAccountBinding.edtDob.setOnClickListener(this);
    }

    private void initSession() {
        mSessionManager = new SessionManager(this);
    }

    private void initViewModel() {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.listenChangeUser(mSessionManager.getUser());
        activityAccountBinding.setUser(accountViewModel);
        accountViewModel.getIsUpdatePassSuccess().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if (mChangePassDialog != null && mChangePassDialog.isShowing()) {
                    if (strings[0].equalsIgnoreCase("0")) {
                        Util.showSnackbar(activityAccountBinding.getRoot(), getString(R.string.update_password_fail), Util.TYPE_SNACK_BAR_WARNING);
                        mChangePassDialog.dismiss();
                    }
                    if (strings[0].equalsIgnoreCase("1")) {
                        Util.showSnackbar(activityAccountBinding.getRoot(), getString(R.string.update_password_success), Util.TYPE_SNACK_BAR_SUCCESS);
                        mChangePassDialog.dismiss();
                    }
                }
            }
        });

        accountViewModel.getProgressUpload().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                dialogProgressBinding.pbUpload.setProgress((int) Math.round(aDouble));
                dialogProgressBinding.txtProgress.setText((int) Math.round(aDouble) + "%");
            }
        });

        accountViewModel.getIsUpdateAvatarSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (progressUploadDialog != null && progressUploadDialog.isShowing()) {
                    if (aBoolean) {
                        Util.showSnackbar(activityAccountBinding.getRoot(), getString(R.string.update_avatar_success), Util.TYPE_SNACK_BAR_SUCCESS);
                    } else {
                        Util.showSnackbar(activityAccountBinding.getRoot(), getString(R.string.update_avatar_fail), Util.TYPE_SNACK_BAR_WRONG);
                    }
                    progressUploadDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangePass:
                openChangePassDialog();
                break;
            case R.id.btnUpdateImage:
                openSelectImage();
            case R.id.btnUpdate:
                updateInformation();
                break;
            case R.id.edtDob:
                openSelectDate();
                break;
            default:
                break;
        }
    }

    private void openSelectDate() {
        new DatePickerDialog(this, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateInformation() {
        boolean isFullValid = Util.checkValidEditText(activityAccountBinding.getRoot(), activityAccountBinding.edtName)
                && Util.checkValidEditText(activityAccountBinding.getRoot(), activityAccountBinding.edtDob)
                && Util.checkValidEditText(activityAccountBinding.getRoot(), activityAccountBinding.edtEmail)
                && Util.checkValidEditText(activityAccountBinding.getRoot(), activityAccountBinding.edtAddress);
        if (isFullValid) {
            String name = activityAccountBinding.edtName.getText().toString();
            String dob = activityAccountBinding.edtDob.getText().toString();
            String email = activityAccountBinding.edtEmail.getText().toString();
            String address = activityAccountBinding.edtAddress.getText().toString();
            boolean male = false;
            if (activityAccountBinding.rbMale.isChecked()) {
                male = true;
            }
            if (activityAccountBinding.rbFemale.isChecked()) {
                male = false;
            }
            User user = new User();
            user.setDob(dob);
            user.setName(name);
            user.setEmail(email);
            user.setMale(male);
            user.setAddress(address);
            accountViewModel.updateInformationAccount(user);
        }
    }

    private void openSelectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, SELECT_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            dialogProgressBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_progress_upload, null, false);
            accountViewModel.uploadFile(data.getData());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.upload_image));
            builder.setView(dialogProgressBinding.getRoot());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                dialogProgressBinding.imgUpload.setImageBitmap(tmp);
                bitmap.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressUploadDialog = builder.create();
            dialogProgressBinding.pbUpload.setProgress(0);
            dialogProgressBinding.txtProgress.setText(0 + "%");
            progressUploadDialog.show();
        }
    }

    private void openChangePassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogChangePassBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_change_pass, null, false);
        builder.setView(dialogChangePassBinding.getRoot());
        dialogChangePassBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangePassDialog != null && mChangePassDialog.isShowing())
                    mChangePassDialog.dismiss();
            }
        });
        dialogChangePassBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(isValidOld && isValidNew && isValidRenew)) {
                    Util.showSnackbar(dialogChangePassBinding.getRoot(), getString(R.string.invalid_pass), Util.TYPE_SNACK_BAR_WARNING);
                    return;
                }

                accountViewModel.updatePassword(mSessionManager.getUser(), dialogChangePassBinding.edtOldPass.getText().toString(), dialogChangePassBinding.edtNewPass.getText().toString());
            }
        });

        dialogChangePassBinding.edtOldPass.addTextChangedListener(this);
        dialogChangePassBinding.edtNewPass.addTextChangedListener(this);
        dialogChangePassBinding.edtReNewPass.addTextChangedListener(this);
        mChangePassDialog = builder.create();
        mChangePassDialog.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
        mChangePassDialog.setCanceledOnTouchOutside(false);
        mChangePassDialog.setCancelable(false);
        mChangePassDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        if (mChangePassDialog != null && !mChangePassDialog.isShowing())
            mChangePassDialog.show();
        mChangePassDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //old
        if (dialogChangePassBinding.edtOldPass.getText().toString().length() >= 6) {
            dialogChangePassBinding.imgCheckOldPass.setVisibility(View.VISIBLE);
            dialogChangePassBinding.imgCheckOldPass.setImageResource(R.drawable.ic_baseline_done_pass_24);
            isValidOld = true;
        } else {
            isValidOld = false;
            if (dialogChangePassBinding.edtOldPass.getText().toString().length() == 0) {
                dialogChangePassBinding.imgCheckOldPass.setVisibility(View.GONE);
            } else {
                dialogChangePassBinding.imgCheckOldPass.setVisibility(View.VISIBLE);
                dialogChangePassBinding.imgCheckOldPass.setImageResource(R.drawable.ic_baseline_close_pass_24);
            }
        }
        //new
        if (dialogChangePassBinding.edtNewPass.getText().toString().length() >= 6 && !s.toString().equalsIgnoreCase(dialogChangePassBinding.edtOldPass.getText().toString())) {
            dialogChangePassBinding.imgCheckNewPass.setVisibility(View.VISIBLE);
            dialogChangePassBinding.imgCheckNewPass.setImageResource(R.drawable.ic_baseline_done_pass_24);
            isValidNew = true;
        } else {
            isValidNew = false;
            if (dialogChangePassBinding.edtNewPass.getText().toString().length() == 0) {
                dialogChangePassBinding.imgCheckNewPass.setVisibility(View.GONE);
            } else {
                dialogChangePassBinding.imgCheckNewPass.setVisibility(View.VISIBLE);
                dialogChangePassBinding.imgCheckNewPass.setImageResource(R.drawable.ic_baseline_close_pass_24);
            }
        }
        //renew
        if (dialogChangePassBinding.edtReNewPass.getText().toString().length() >= 6 && s.toString().equalsIgnoreCase(dialogChangePassBinding.edtNewPass.getText().toString())) {
            dialogChangePassBinding.imgCheckReNewPass.setVisibility(View.VISIBLE);
            dialogChangePassBinding.imgCheckReNewPass.setImageResource(R.drawable.ic_baseline_done_pass_24);
            isValidRenew = true;
        } else {
            isValidRenew = false;
            if (dialogChangePassBinding.edtReNewPass.getText().toString().length() == 0) {
                dialogChangePassBinding.imgCheckReNewPass.setVisibility(View.GONE);
            } else {
                dialogChangePassBinding.imgCheckReNewPass.setVisibility(View.VISIBLE);
                dialogChangePassBinding.imgCheckReNewPass.setImageResource(R.drawable.ic_baseline_close_pass_24);
            }
        }

    }


    private void updateLabel(Calendar myCalendar, EditText editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel(myCalendar, activityAccountBinding.edtDob);
    }
}
