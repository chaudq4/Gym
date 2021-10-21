package com.chauduong.gym.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chauduong.gym.R;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Inbox;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;

public class DataBindingAdapter {
    private static final String IMAGE_CHAT = "imagechat";

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }


    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url != null && url.length() != 0)
            Glide.with(view.getContext())
                    .load(url)
                    .into(view);


    }

    @BindingAdapter("bind:imageUrlDatabase")
    public static void loadImageDatabase(ImageView view, String url) {
        if (url != null && url.length() != 0) {
            CircularProgressDrawable drawable = new CircularProgressDrawable(view.getContext());
            drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            // set all other properties as you would see fit and start it
            drawable.start();
            Glide.with(view.getContext())
                    .asBitmap().thumbnail()
                    .load(url)
                    .placeholder(drawable)
                    .into(new BitmapImageViewTarget(view) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            // Do bitmap magic here
                            if (resource == null) return;
                            super.setResource(resource);
                            view.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("bind:textNameConversation")
    public static void setTextNameConversation(TextView view, Inbox inbox) {
        String content;
        if (new SessionManager(view.getContext()).getUser().getId().equalsIgnoreCase(inbox.getTo().getId())) {
            content = inbox.getFrom().getName();
        } else {
            content = inbox.getTo().getName();
        }
        view.setText(content);
    }

    @SuppressLint("ResourceAsColor")
    @BindingAdapter("bind:imageUrlConversation")
    public static void loadImageConversation(ImageView view, Inbox inbox) {
        String url;
        if (new SessionManager(view.getContext()).getUser().getId().equalsIgnoreCase(inbox.getTo().getId())) {
            url = inbox.getFrom().getAvatar();
        } else {
            url = inbox.getTo().getAvatar();
        }
        if (url == null || url.length() == 0) {
            view.setImageDrawable(new ColorDrawable(Color.GRAY));
        }
        if (url != null && url.length() != 0) {
            CircularProgressDrawable drawable = new CircularProgressDrawable(view.getContext());
            drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            // set all other properties as you would see fit and start it
            drawable.start();
            Glide.with(view.getContext())
                    .asBitmap().thumbnail()
                    .load(url)
                    .placeholder(drawable)
                    .into(new BitmapImageViewTarget(view) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            // Do bitmap magic here
                            if (resource == null) return;
                            super.setResource(resource);
                        }
                    });
        }
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("bind:imageSrc")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"bind:high", "bind:weight"})
    public static void setBMI(TextView textView, float high, int weight) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        textView.setText(String.valueOf(df.format(weight / (high * high))));
    }

    @BindingAdapter("bind:sethigh")
    public static void setTextHigh(TextView textView, float value) {
        textView.setText(value + "m");
    }

    @BindingAdapter("bind:setweight")
    public static void setTextWeight(TextView textView, float value) {
        textView.setText(value + "kg");
    }


}