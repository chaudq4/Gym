package com.chauduong.gym.ui;

import static com.chauduong.gym.fragment.signup.SignUpFragment.USER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.chauduong.gym.MainActivity;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ActivityLoginBinding;
import com.chauduong.gym.fragment.signin.SignInFragment;
import com.chauduong.gym.manager.database.DatabaseListener;
import com.chauduong.gym.manager.database.DatabaseManager;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Type;
import com.chauduong.gym.model.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding mActivityLoginBinding;
    SignInFragment mSignInFragment;
    boolean doubleBackToExitPressedOnce = false;
    SessionManager mSessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        readSession();
        initView();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void readSession() {
        mSessionManager = new SessionManager(this);
        if (mSessionManager.isSignIn()) {
            User user = mSessionManager.getUser();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USER, user);
            finish();
            mSessionManager = new SessionManager(this);
            mSessionManager.createSignIn(user);
            DialogManager.getInstance(this).dissmissProgressDialog();
            startActivity(intent);
        }
    }

    private void initView() {
        if (mSignInFragment == null) mSignInFragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl, mSignInFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
}
