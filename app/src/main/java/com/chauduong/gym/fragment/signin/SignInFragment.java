package com.chauduong.gym.fragment.signin;


import static com.chauduong.gym.fragment.signup.SignUpFragment.USER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.chauduong.gym.MainActivity;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.FragmentSigninBinding;
import com.chauduong.gym.fragment.signup.SignUpFragment;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.User;

public class SignInFragment extends Fragment implements View.OnClickListener, SignInPresenterListener, TextView.OnEditorActionListener {
    private static final String TAG = "SignInFragment ";
    FragmentSigninBinding mFragmentSigninBinding;
    SignInPresenter mSignInPresenter;
    SessionManager mSessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSigninBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);
        return mFragmentSigninBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSesstion();
        initView();
        if (mSessionManager.isFirtTime()) {
            initAnimation();
        } else {
            mFragmentSigninBinding.layoutParent.setVisibility(View.VISIBLE);
            mFragmentSigninBinding.layoutTransaction.setVisibility(View.GONE);
            mFragmentSigninBinding.layoutParent.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        }
        initPresenter();
    }

    private void initSesstion() {
        mSessionManager = new SessionManager(getContext());
    }

    private void initAnimation() {
        Animation animation;
        animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);

        mFragmentSigninBinding.imgLogoTransaction.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSessionManager.setIsFirstTime(false);
                        animation.setAnimationListener(null);
                        mFragmentSigninBinding.layoutParent.setVisibility(View.VISIBLE);
                        mFragmentSigninBinding.layoutTransaction.setVisibility(View.GONE);
                        mFragmentSigninBinding.layoutParent.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                    }
                }, 1500);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mFragmentSigninBinding.txt1.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_to_right));
        mFragmentSigninBinding.txt3.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_to_right));
        mFragmentSigninBinding.txt2.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_to_left));
        mFragmentSigninBinding.txt4.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_to_left));
    }


    private void initPresenter() {
        mSignInPresenter = new SignInPresenter(this, getContext());
    }

    private void initView() {
        mFragmentSigninBinding.btnSignIn.setOnClickListener(this);
        mFragmentSigninBinding.tvSignUp.setOnClickListener(this);
        mFragmentSigninBinding.edtPhoneNumber.setOnEditorActionListener(this);
        mFragmentSigninBinding.edtPhoneNumber.setImeActionLabel("Next", KeyEvent.KEYCODE_ENTER);
        mFragmentSigninBinding.edtPassword.setOnEditorActionListener(this);
        mFragmentSigninBinding.edtPassword.setImeActionLabel("Enter", KeyEvent.KEYCODE_ENTER);
        if(mFragmentSigninBinding.cbShowPass.isChecked()){
            mFragmentSigninBinding.edtPassword.setTransformationMethod(null);
        }else {
            mFragmentSigninBinding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
        mFragmentSigninBinding.cbShowPass.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked) {
                mFragmentSigninBinding.edtPassword.setTransformationMethod(null);
            }
            else{
                mFragmentSigninBinding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

//        mFragmentSigninBinding.imgShowPass.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Log.i(TAG, "onLongClick: ");
//                mFragmentSigninBinding.edtPassword.setTransformationMethod(null);
//                mFragmentSigninBinding.imgShowPass.setImageResource(R.drawable.ic_show_pass_close);
//                return false;
//            }
//        });
//        mFragmentSigninBinding.imgShowPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mFragmentSigninBinding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
//                mFragmentSigninBinding.imgShowPass.setImageResource(R.drawable.ic_show_pass_open);
//                Log.i(TAG, "onClick: ");
//            }
//        });

        if (mSessionManager.isRememberLogin()) {
            mFragmentSigninBinding.cbRememberAccount.setChecked(true);
            mFragmentSigninBinding.edtPhoneNumber.setText(mSessionManager.getLoginPhoneNumber());
            mFragmentSigninBinding.edtPassword.setText(mSessionManager.getLoginPassWord());
        } else {
            mFragmentSigninBinding.cbRememberAccount.setChecked(false);
        }
        if (mSessionManager.isFirtTime()) {
            mFragmentSigninBinding.layoutParent.setVisibility(View.GONE);
            mFragmentSigninBinding.layoutTransaction.setVisibility(View.VISIBLE);
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
                DialogManager.getInstance(getContext()).showProgressDialog(getFragmentManager(), "Đang đăng nhập");

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
        DialogManager.getInstance(getContext()).dissmissProgressDialog();
        startActivity(intent);

    }

    @Override
    public void onSignInError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        DialogManager.getInstance(getContext()).dissmissProgressDialog();
    }

    @Override
    public void onSignUpClick() {
        getFragmentManager().beginTransaction().replace(R.id.fl, new SignUpFragment())
                .addToBackStack(null).commit();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.edtPhoneNumber:
                if (actionId == KeyEvent.KEYCODE_ENTER) {
                  mFragmentSigninBinding.edtPassword.setFocusable(View.FOCUSABLE);
                }
                break;
            case R.id.edtPassword:
                if (actionId == KeyEvent.KEYCODE_ENTER) {
                    String phoneNumber = mFragmentSigninBinding.edtPhoneNumber.getText().toString().trim();
                    String passWord = mFragmentSigninBinding.edtPassword.getText().toString().trim();
                    DialogManager.getInstance(getContext()).showProgressDialog(getFragmentManager(), "Đang đăng nhập");
                    mSignInPresenter.login(phoneNumber, passWord);
                }
                break;
            default:
                break;
        }
        return false;

    }
}
