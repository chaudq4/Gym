package com.chauduong.gym.utils;

import static com.chauduong.gym.fragment.signup.SignUpManager.URL_FIREBASE;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chauduong.gym.R;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataBindingAdapter {
    private static final String IMAGE_CHAT = "imagechat";
    private static final String USERS = "users";

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
        CircularProgressDrawable drawable = new CircularProgressDrawable(view.getContext());
        drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        if (url != null && url.length() != 0)
            Glide.with(view.getContext())
                    .load(url)
                    .placeholder(drawable)
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
        User user;
        if (new SessionManager(view.getContext()).getUser().getId().equalsIgnoreCase(inbox.getTo().getId())) {
            user = inbox.getFrom();
        } else {
            user = inbox.getTo();
        }

        FirebaseDatabase.getInstance(URL_FIREBASE).getReference().child(USERS).child(user.getId()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() instanceof String) {
                    String name = snapshot.getValue(String.class);
                    view.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    @BindingAdapter("bind:setStatusConversation")
    public static void setStatusConversation(ImageView view, Inbox inbox) {
        User user;
        if (new SessionManager(view.getContext()).getUser().getId().equalsIgnoreCase(inbox.getTo().getId())) {
            user = inbox.getFrom();
        } else {
            user = inbox.getTo();
        }
        FirebaseDatabase.getInstance(URL_FIREBASE).getReference().child(USERS).child(user.getId()).child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() instanceof Boolean) {
                    boolean isOnline = snapshot.getValue(Boolean.class);
                    view.setImageResource(isOnline ? R.color.bg_online_chat : R.color.bg_offline_chat);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    @BindingAdapter("bind:setTextConversation")
    public static void setTextConversation(TextView textview, Conversation conversation) {
        boolean isYou = new SessionManager(textview.getContext()).getUser().getId().equalsIgnoreCase(conversation.getInbox().getFrom().getId());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm");
        Date resulted = new Date(conversation.getInbox().getTime());
        String time = sdf.format(resulted);
        String msg = conversation.getInbox().getMsg();
        if (conversation.getInbox().getMsg() != null && conversation.getInbox().getMsg().length() > 10) {
            msg = conversation.getInbox().getMsg().substring(0, 10) + "...";
        }
        String content;
        if ((conversation.getInbox().getMsg() == null || conversation.getInbox().getMsg().length() == 0) && conversation.getInbox().getLink().length() != 0) {
            if (isYou) {
                content = textview.getContext().getString(R.string.you) + ": " + textview.getContext().getText(R.string.sent_image) + "   ." + time;
            } else {
                content = textview.getContext().getText(R.string.sent_image) + "   ." + time;
            }

        } else {
            if (isYou) {
                content = textview.getContext().getString(R.string.you) + ": " + msg + "   ." + time;
            } else {
                content = msg + "   ." + time;
            }
        }
        textview.setText(content);
    }

    @BindingAdapter("bind:setTime")
    public static void setTime(TextView textview, long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm dd-MM");
        Date resulted = new Date(time);
        textview.setText(sdf.format(resulted));
    }

    @BindingAdapter("bind:setStyleFont")
    public static void setStyleFont(TextView textView, Inbox inbox) {
        boolean isRead = inbox.isRead();
        if (inbox.getFrom().getId().equalsIgnoreCase(new SessionManager(textView.getContext()).getUser().getId())) {
            isRead = true;
        }
        textView.setTextColor(isRead ? Color.GRAY : Color.BLACK);
        textView.setTypeface(null, isRead ? Typeface.NORMAL : Typeface.BOLD);
    }


}