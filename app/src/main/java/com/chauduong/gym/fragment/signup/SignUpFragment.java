package com.chauduong.gym.fragment.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.chauduong.gym.MainActivity;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.FragmentSignupBinding;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.User;

public class SignUpFragment extends Fragment implements View.OnClickListener, SignUpPresenterListener {
    public static final String USER = "USER";
    private FragmentSignupBinding mFragmentSignupBinding;
    private SignUpPresenter mSignUpPresenter;
    private SessionManager mSessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSignupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);
        return mFragmentSignupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initPresenter();
    }

    private void initView() {
        mFragmentSignupBinding.btnSignUp.setOnClickListener(this);
    }

    private void initPresenter() {
        mSignUpPresenter = new SignUpPresenter(getContext(), this);
    }

    public SignUpFragment() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                String name = mFragmentSignupBinding.edtName.getText().toString().trim();
                String phoneNumber = mFragmentSignupBinding.edtPhoneNumber.getText().toString().trim();
                String passWord = mFragmentSignupBinding.edtPassword.getText().toString().trim();
                mSignUpPresenter.signUp(name, phoneNumber, passWord);
                DialogManager.getInstance(getContext()).showProgressDialog(getFragmentManager(),null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSignUpSuccess(User user) {
        mSessionManager = new SessionManager(getContext());
        mSessionManager.createSignIn(user);
        DialogManager.getInstance(getContext()).dissmissProgressDialog();
        Intent intent = new Intent();
        intent.putExtra(USER, user);
        intent.setClass(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignUpError(String msg) {
        DialogManager.getInstance(getContext()).dissmissProgressDialog();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
