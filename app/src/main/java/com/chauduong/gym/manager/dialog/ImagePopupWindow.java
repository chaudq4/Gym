package com.chauduong.gym.manager.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.PopupImageBinding;

public class ImagePopupWindow implements View.OnClickListener {
    private static final String TAG = "ImagePopupWindow";
    private PopupWindow imagePopup;
    private PopupImageBinding popupImageBinding;
    private Context mContext;
    private boolean isShow;

    public ImagePopupWindow(Context mContext) {
        this.mContext = mContext;
        imagePopup = PopupHelper.newBasicPopupWindow(mContext);
        popupImageBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.popup_image, null, false);
        imagePopup.setContentView(popupImageBinding.getRoot());
        popupImageBinding.btnClose.setOnClickListener(this);
        popupImageBinding.btnMore.setOnClickListener(this);
        isShow = false;
    }

    public void showImagePopup(View parent, String url) {
        Glide.with(mContext)
                .asBitmap()
                .load(url)
                .into(new BitmapImageViewTarget(popupImageBinding.imgImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        // Do bitmap magic here
                        if (resource == null) return;
                        popupImageBinding.imgImage.setImageBitmap(resource);
                    }
                });

//        popupImageBinding.imgImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imagePopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        imagePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        imagePopup.setBackgroundDrawable(new ColorDrawable(mContext.getColor(R.color.white)));
        imagePopup.showAtLocation(parent, Gravity.CENTER, 0, 0);
        isShow = true;
    }

    public boolean isShow() {
        return isShow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                Log.i(TAG, "onClick: close");
                isShow = false;
                imagePopup.dismiss();
                break;
            case R.id.btnMore:
                break;
            default:
                break;
        }
    }

    public void dissmiss() {
        if (imagePopup != null && imagePopup.isShowing()) {
            isShow = false;
            imagePopup.dismiss();
        }
    }
}
