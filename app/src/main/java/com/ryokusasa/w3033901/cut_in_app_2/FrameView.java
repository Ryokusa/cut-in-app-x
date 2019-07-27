package com.ryokusasa.w3033901.cut_in_app_2;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryokusasa.w3033901.cut_in_app_2.Dialog.AppDialog;

//設定されたカットインを表示するview

public class FrameView extends LinearLayout {
    private TextView eventName;
    private TextView cutInName;
    private ImageView thumbnail;
    private ImageView appIcon;
    private CutInHolder cutInHolder;
    private int id;

    public FrameView(final Activity activity, final CutInHolder cutInHolder){
        super(activity);

        //レイアウト展開
        LayoutInflater.from(activity).inflate(R.layout.frame, this);
        eventName = (TextView)findViewById(R.id.eventName);
        cutInName = (TextView)findViewById(R.id.cutInName);
        thumbnail = (ImageView)findViewById(R.id.thumbnail);
        appIcon = (ImageView)findViewById(R.id.appIcon);
        appIcon.setVisibility(INVISIBLE);
        appIcon.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

        this.cutInHolder = cutInHolder;

        appIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDialog appDialog = new AppDialog();
                appDialog.showWithTask(activity.getFragmentManager(), "appDialog", activity, cutInHolder);
            }
        });
    }

    public FrameView setEventType(EventType eventType){
        eventName.setText(eventType.getString());
        return this;
    }

    public FrameView setCutInName(CharSequence name){
        cutInName.setText(name);
        return this;
    }

    public FrameView setThumbnail(Drawable drawable){
        thumbnail.setImageDrawable(drawable);
        return this;
    }

    public FrameView setAppIcon(@Nullable Drawable drawable){
        if(drawable != null) {
            appIcon.setImageDrawable(drawable);
            appIcon.setVisibility(VISIBLE);
            appIcon.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
        return this;
    }


}
