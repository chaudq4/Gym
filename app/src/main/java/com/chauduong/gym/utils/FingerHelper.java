package com.chauduong.gym.utils;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;

import com.chauduong.gym.R;

public class FingerHelper {
    private static final int REQUEST_CODE = 1;
    Context mContext;
    BiometricPrompt.AuthenticationCallback biometricCallback;

    public FingerHelper(Context mContext, BiometricPrompt.AuthenticationCallback biometricCallback) {
        this.mContext = mContext;
        this.biometricCallback = biometricCallback;
    }

    private void showAuthentic() {
        BiometricPrompt.Builder builder = new BiometricPrompt.Builder(mContext)
                .setTitle(mContext.getString(R.string.title_login_finger))
                .setDescription(mContext.getString(R.string.description_login_finger))
                .setNegativeButton(" ", mContext.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        BiometricPrompt biometricPrompt = builder.build();
        biometricPrompt.authenticate(new CancellationSignal(), mContext.getMainExecutor(), biometricCallback);

    }


    public void isAvailable() {
        BiometricManager biometricManager = BiometricManager.from(mContext);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                showAuthentic();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(mContext, mContext.getText(R.string.hardware_not_support_finger), Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(mContext, mContext.getText(R.string.BIOMETRIC_ERROR_HW_UNAVAILABLE), Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                 Prompts the user to create credentials that your app accepts.
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getString(R.string.title_setting_finger));
                builder.setMessage(mContext.getString(R.string.active_finger));
                builder.setIcon(R.drawable.ic_baseline_fingerprint_24);
                builder.setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                        ((Activity) mContext).startActivityForResult(enrollIntent, REQUEST_CODE);
                    }
                });
                builder.setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
    }

    public boolean isActiveBio() {
        BiometricManager biometricManager = BiometricManager.from(mContext);
        return isBiometricPromptEnabled() && isSdkVersionSupported() && biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }


    private boolean isBiometricPromptEnabled() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    private boolean isSdkVersionSupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }


}
