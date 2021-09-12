package com.ryokusasa.cut_in_app.CutIn;


import android.graphics.drawable.Drawable;

import com.ryokusasa.cut_in_app.Dialog.AppData;
import com.ryokusasa.cut_in_app.EventType;

import java.io.Serializable;

//カットインとイベントをまとめたホルダー
public class CutInHolder implements Serializable {
    private final String TAG = "CutInHolder";

    private EventType eventType;
    private AppData appData;
    private CutIn cutIn;


    //コンストラクタ
    public CutInHolder(EventType eventType, CutIn cutIn) {
        //イベントネーム
        setEventType(eventType);

        //カットイン
        setCutIn(cutIn);

    }

    public CutInHolder(EventType eventType, CutIn cutIn, AppData appData){
        this(eventType, cutIn);
        this.appData = appData;
    }

    public CutInHolder setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public CutIn getCutIn() {
        return cutIn;
    }

    public CutInHolder setCutIn(CutIn cutIn) {
        this.cutIn = cutIn;
        return this;
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public AppData getAppData() {
        return appData;
    }

    public Drawable getAppIcon(){
        if(appData != null){
            return appData.getIconDrawable();
        }
        return null;
    }
}