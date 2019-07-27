package com.ryokusasa.w3033901.cut_in_app_2;


import android.graphics.drawable.Drawable;

import com.ryokusasa.w3033901.cut_in_app_2.Dialog.AppData;

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

    //再生
    public void play() {
        //TODO 再生処理
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