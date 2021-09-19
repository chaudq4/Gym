package com.chauduong.gym.fragment;


import static com.chauduong.gym.fragment.SignUpFragment.USER;

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
import com.chauduong.gym.databinding.FragmentSigninBinding;
import com.chauduong.gym.manager.SessionManager;
import com.chauduong.gym.model.User;
import com.chauduong.gym.presenter.SignInPresenterListener;
import com.chauduong.gym.presenter.SignInPresenter;

public class SignInFragment extends Fragment implements View.OnClickListener, SignInPresenterListener {
    FragmentSigninBinding mFragmentSigninBinding;
    SignInPresenter mSignInPresenter;
    SessionManager mSessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSigninBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);
        return mFragmentSigninBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initPresenter();
        readSession();
    }

    private void readSession() {
        mSessionManager = new SessionManager(getContext());
        if (mSessionManager.isSignIn()) {
            User user = mSessionManager.getUser();
            onSignInSuccess(user);
        }
    }

    private void initPresenter() {
        mSignInPresenter = new SignInPresenter(this, getContext());
    }

    private void initView() {
        mFragmentSigninBinding.btnSignIn.setOnClickListener(this);
        mFragmentSigninBinding.tvSignUp.setOnClickListener(this);
        mSessionManager = new SessionManager(getContext());
        if (mSessionManager.isRememberLogin()) {
            mFragmentSigninBinding.cbRememberAccount.setChecked(true);
            mFragmentSigninBinding.edtPhoneNumber.setText(mSessionManager.getLoginPhoneNumber());
            mFragmentSigninBinding.edtPassword.setText(mSessionManager.getLoginPassWord());
        } else {
            mFragmentSigninBinding.cbRememberAccount.setChecked(false);
        }
    }

    public SignInFragment() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignUp:
                mSignInPresenter.signUp();
                break;
            case R.id.btnSignIn:
                String phoneNumber = mFragmentSigninBinding.edtPhoneNumber.getText().toString().trim();
                String passWord = mFragmentSigninBinding.edtPassword.getText().toString().trim();
                mSignInPresenter.login(phoneNumber, passWord);
                break;
            default:
                break;
        }

    }


    @Override
    public void onSignInSuccess(User user) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(USER, user);
        getActivity().finish();
        mSessionManager = new SessionManager(getContext());
        mSessionManager.createSignIn(user);
        if (mFragmentSigninBinding.cbRememberAccount.isChecked()) {
            mSessionManager.putRememberLogin(user.getPhoneNumber(), user.getPassword(), true);
        } else {
            mSessionManager.setIsRemember(false);
        }
        startActivity(intent);

    }

    @Override
    public void onSignInError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpClick() {
        getFragmentManager().beginTransaction().replace(R.id.fl, new SignUpFragment())
                .addToBackStack(null).commit();
    }


}
