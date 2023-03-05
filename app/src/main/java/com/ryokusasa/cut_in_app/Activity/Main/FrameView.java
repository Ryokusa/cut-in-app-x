package com.ryokusasa.cut_in_app.Activity.Main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryokusasa.cut_in_app.Activity.SelCutIn.SelCutInActivity;
import com.ryokusasa.cut_in_app.CutIn.CutInHolder;
import com.ryokusasa.cut_in_app.Dialog.AppDialog;
import com.ryokusasa.cut_in_app.EventType;
import com.ryokusasa.cut_in_app.R;
import com.ryokusasa.cut_in_app.UtilCommon;

//設定されたカットインを表示するview

public class FrameView extends LinearLayout {
    private final TextView eventName;
    private final TextView cutInName;
    private final ImageView thumbnail;
    private final ImageView appIcon;
    private final UtilCommon utilCommon;

    public FrameView(final MainActivity activity, final CutInHolder cutInHolder){
        super(activity);

        utilCommon = (UtilCommon) activity.getApplication();

        //レイアウト展開
        LayoutInflater.from(activity).inflate(R.layout.frame, this);
        eventName = findViewById(R.id.eventName);
        cutInName = findViewById(R.id.cutInName);
        thumbnail = findViewById(R.id.thumbnail);
        appIcon = findViewById(R.id.appIcon);
        appIcon.setVisibility(INVISIBLE);
        appIcon.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

        //設定
        setEventType(cutInHolder.getEventType());
        setCutInName(cutInHolder.getCutIn().title);
        setThumbnail(cutInHolder.getCutIn().getThumbnail());
        setAppIcon(cutInHolder.getAppIcon());

        //クリックリスナー
        appIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDialog appDialog = new AppDialog();
                appDialog.showWithTask(activity.getSupportFragmentManager(), "appDialog", activity, cutInHolder);
            }
        });

        thumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //インデックスを渡して画面遷移
                UtilCommon utilCommon = (UtilCommon)activity.getApplication();
                Intent intent = new Intent(activity, SelCutInActivity.class);
                intent.putExtra("id", utilCommon.cutInHolderList.indexOf(cutInHolder));
                intent.putExtra("id", utilCommon.cutInHolderList.indexOf(cutInHolder));
                activity.startActivity(intent);
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
