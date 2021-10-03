package com.chauduong.gym.manager.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.DialogVideoBinding;
import com.chauduong.gym.model.ProgressDialog;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

public class DialogManager implements View.OnClickListener {
    private static DialogManager instance = null;
    public static final int PLAY_VIDEO = 1;
    private Context mContext;
    private boolean isShow;
    private PopupWindow mPlayVideoPopupWindow;
    private YouTubePlayer mYouTubePlayer;
    private ProgressDialog mDialogProgress;

    public static DialogManager getInstance(Context mContext) {
        if (instance == null)
            instance = new DialogManager(mContext);
        return instance;
    }

    private DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    public void showDialog(int type) {
        switch (type) {

        }

    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void dismiss() {
        if (mPlayVideoPopupWindow.isShowing())
            mPlayVideoPopupWindow.dismiss();
    }

    public void showPlayVideo(View viewParent, String url) {
        DialogVideoBinding dialogVideoBinding = DataBindingUtil.inflate(((Activity) mContext).getLayoutInflater(), R.layout.dialog_video, null, false);
        mPlayVideoPopupWindow = PopupHelper.newBasicPopupWindow(mContext);
        mPlayVideoPopupWindow.setContentView(dialogVideoBinding.getRoot());
        mPlayVideoPopupWindow.setAnimationStyle(R.style.popup_window_animation);
        mPlayVideoPopupWindow.showAtLocation(viewParent, Gravity.CENTER, 0, 0);

        ViewGroup root = (ViewGroup) ((Activity) mContext).getWindow().getDecorView().getRootView();
        isShow = true;
        applyDim(root, 0.7f);
        ((AppCompatActivity) mContext).getLifecycle().addObserver(dialogVideoBinding.ytVideo);
        dialogVideoBinding.ytVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(url, 0);
                youTubePlayer.play();
            }
        });
        mPlayVideoPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShow = false;
                clearDim(root);
                dialogVideoBinding.ytVideo.release();
            }
        });
        dialogVideoBinding.rlCloseVideo.setOnClickListener(this);

    }

    public static void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlCloseVideo:
                dismiss();
                break;
        }
    }

    public void showProgressDialog(FragmentManager fragmentManager, String title) {
        mDialogProgress = ProgressDialog.newInstance(title);
        mDialogProgress.show(fragmentManager, null);
    }

    public void dissmissProgressDialog() {
        if (mDialogProgress != null
                && mDialogProgress.getDialog() != null
                && mDialogProgress.getDialog().isShowing()
                && !mDialogProgress.isRemoving()) {
            //dialog is showing so do something
            mDialogProgress.dismiss();
        }
    }
}
