package com.chauduong.gym.fragment.signin;


import static com.chauduong.gym.fragment.signup.SignUpFragment.USER;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.MainActivity;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.FragmentSigninBinding;
import com.chauduong.gym.fragment.signup.SignUpFragment;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.User;
import com.chauduong.gym.utils.FingerHelper;
import com.chauduong.gym.utils.Util;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class SignInFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String TAG = "SignInFragment ";
    FragmentSigninBinding mFragmentSigninBinding;
    SignInViewModel mSignInViewModel;
    SessionManager mSessionManager;
    FingerHelper mFingerHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSigninBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);
        return mFragmentSigninBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSesstion();
        initViewModel();
        initFinger();
        initView();
        if (mSessionManager.isFirtTime()) {
            initAnimation();
        } else {
            mFragmentSigninBinding.layoutParent.setVisibility(View.VISIBLE);
            mFragmentSigninBinding.layoutTransaction.setVisibility(View.GONE);
            mFragmentSigninBinding.layoutParent.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        }
        registerObserver();

    }

    private void initFinger() {
        if (mFingerHelper == null)
            mFingerHelper = new FingerHelper(getContext(), new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    Log.i(TAG, "onAuthenticationError: " + errorCode + " " + errString);
                    if (errorCode == 7) {
                        mFragmentSigninBinding.btnFinger.setImageResource(R.drawable.ic_baseline_fingerprint_24_off);
                        Util.showSnackbar(mFragmentSigninBinding.getRoot(), getString(R.string.more_than_five_authentic));
                    }
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                    Log.i(TAG, "onAuthenticationHelp: " + helpCode);
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    Log.i(TAG, "onAuthenticationSucceeded: ");
                    mSignInViewModel.setPhoneNumber(mSessionManager.getUser().getPhoneNumber());
                    mSignInViewModel.setPassword(mSessionManager.getKeyPassword());
                    mSignInViewModel.login();
                    super.onAuthenticationSucceeded(result);
                }

                @Override
                public void onAuthenticationFailed() {
                    Log.i(TAG, "onAuthenticationFailed: ");
                    super.onAuthenticationFailed();
                }
            });
    }


    private void registerObserver() {
        mSignInViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra(USER, user);
                    Objects.requireNonNull(getActivity()).finish();
                    mSessionManager = new SessionManager(Objects.requireNonNull(getContext()));
                    mSessionManager.createSignIn(user);
                    mSessionManager.setIsSignIn(true);
                    if (mFragmentSigninBinding.cbRememberAccount.isChecked()) {
                        mSessionManager.putRememberLogin(user.getPhoneNumber(), user.getPassword(), true);
                    } else {
                        mSessionManager.putRememberLogin(user.getPhoneNumber(), user.getPassword(), false);
                    }
//                    DialogManager.getInstance(getContext()).dissmissProgressDialog();
                    startActivity(intent);
                }
            }
        });

        mSignInViewModel.getMsgSignError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
//                DialogManager.getInstance(getContext()).dissmissProgressDialog();
            }
        });
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


    private void initViewModel() {
        mSignInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        mFragmentSigninBinding.setViewModel(mSignInViewModel);
    }

    private void initView() {
        mFragmentSigninBinding.btnSignIn.setOnClickListener(this);
        mFragmentSigninBinding.tvSignUp.setOnClickListener(this);
        mFragmentSigninBinding.edtPhoneNumber.setOnEditorActionListener(this);
        mFragmentSigninBinding.edtPhoneNumber.setImeActionLabel("Next", KeyEvent.KEYCODE_ENTER);
        mFragmentSigninBinding.edtPassword.setOnEditorActionListener(this);
        mFragmentSigninBinding.edtPassword.setImeActionLabel("Enter", KeyEvent.KEYCODE_ENTER);
        mFragmentSigninBinding.btnFinger.setOnClickListener(this);
        if (mSessionManager.isFinger() && mFingerHelper.isActiveBio()) {
            mFragmentSigninBinding.btnFinger.setImageResource(R.drawable.ic_baseline_fingerprint_24);
        } else {
            mFragmentSigninBinding.btnFinger.setImageResource(R.drawable.ic_baseline_fingerprint_24_off);
        }
        if (mFragmentSigninBinding.cbShowPass.isChecked()) {
            mFragmentSigninBinding.edtPassword.setTransformationMethod(null);
        } else {
            mFragmentSigninBinding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
        mFragmentSigninBinding.cbShowPass.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked) {
                mFragmentSigninBinding.edtPassword.setTransformationMethod(null);
            } else {
                mFragmentSigninBinding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });


        if (mSessionManager.isRememberLogin()) {
            mFragmentSigninBinding.cbRememberAccount.setChecked(true);
            mSignInViewModel.setPhoneNumber(mSessionManager.getLoginPhoneNumber());
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

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignUp:
                getFragmentManager().beginTransaction().replace(R.id.fl, new SignUpFragment())
                        .addToBackStack(null).commit();
                break;
            case R.id.btnSignIn:
                DialogManager.getInstance(getContext()).showProgressDialog(getFragmentManager(), "Đang đăng nhập");
                mSignInViewModel.login();
                break;
            case R.id.btnFinger:
                onFingerClick();
                break;
            default:
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void onFingerClick() {
        if (!(mSessionManager.isFinger() && mFingerHelper.isActiveBio())) {
            Util.showSnackbar(mFragmentSigninBinding.getRoot(), getString(R.string.havent_active_finger));
            return;
        }
        mFingerHelper.isAvailable();
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
//                    DialogManager.getInstance(getContext()).showProgressDialog(getFragmentManager(), "Đang đăng nhập");
                    mSignInViewModel.login();
                }
                break;
            default:
                break;
        }
        return false;

    }
}
