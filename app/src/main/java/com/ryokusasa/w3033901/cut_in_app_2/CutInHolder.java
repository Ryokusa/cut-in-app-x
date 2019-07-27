package com.ryokusasa.w3033901.cut_in_app_2;


import android.graphics.drawable.Drawable;

import com.ryokusasa.w3033901.cut_in_app_2.Dialog.AppData;

//カットインとイベントをまとめたホルダー
public class CutInHolder {
    private final String TAG = "CutInHolder";

    private EventType eventType;
    private AppData appData;
    private int cutInId;


    //コンストラクタ
    public CutInHolder(EventType eventType, int cutInId) {
        //イベントネーム
        setEventType(eventType);

        //カットイン
        setCutInId(cutInId);

    }

    public CutInHolder(EventType eventType, int cutInId, AppData appData){
        this(eventType, cutInId);
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

    public CutInHolder setCutInId(int cutInId) {
        this.cutInId = cutInId;
        return this;
    }

    public int getCutInId() {
        return cutInId;
    }

    public CutInHolder setAppData(AppData appData) {
        this.appData = appData;
        return this;
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