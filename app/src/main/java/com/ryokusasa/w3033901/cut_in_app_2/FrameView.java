package com.ryokusasa.w3033901.cut_in_app_2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

//設定されたカットインを表示するview

public class FrameView extends LinearLayout {

    public FrameView(Context context, AttributeSet attrs){
        super(context, attrs);

        //レイアウト展開
        LayoutInflater.from(context).inflate(R.layout.frame, this);
    }

    public FrameView(Context context){
        super(context);

        //レイアウト展開
        LayoutInflater.from(context).inflate(R.layout.frame, this);
    }

    public FrameView setEventName(CharSequence name){
        TextView eventName = (TextView)findViewById(R.id.eventName);
        eventName.setText(name);

        return this;
    }

    public FrameView setCutInName(CharSequence name){
        TextView eventName = (TextView)findViewById(R.id.cutInName);
        eventName.setText(name);

        return this;
    }
}
