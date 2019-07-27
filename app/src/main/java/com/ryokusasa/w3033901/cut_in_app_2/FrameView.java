package com.ryokusasa.w3033901.cut_in_app_2;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//設定されたカットインを表示するview

public class FrameView extends LinearLayout {
    private TextView eventName;
    private TextView cutInName;
    private ImageView thumbnail;

    public FrameView(Activity activity){
        super(activity);

        //レイアウト展開
        LayoutInflater.from(activity).inflate(R.layout.frame, this);
        eventName = (TextView)findViewById(R.id.eventName);
        cutInName = (TextView)findViewById(R.id.cutInName);
        thumbnail = (ImageView)findViewById(R.id.thumbnail);

    }

    public FrameView setEventName(CharSequence name){
        eventName.setText(name);
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
}
