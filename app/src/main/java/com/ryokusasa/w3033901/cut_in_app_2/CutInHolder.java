package com.ryokusasa.w3033901.cut_in_app_2;


import java.io.Serializable;

//カットインとイベントをまとめたホルダー
public class CutInHolder {
    private final String TAG = "CutInHolder";

    //eventName定数
    public final static String SCREEN_ON = "ScreenOn";
    public final static String LOW_BATTERY = "LowBattery";

    private String eventName;
    private String packageName = null;
    private int cutInId;


    //コンストラクタ
    public CutInHolder(String eventName, int cutInId) {
        //イベントネーム
        setEventName(eventName);

        //カットイン
        setCutInId(cutInId);

    }

    //再生
    public void play() {
        //TODO 再生処理
    }

    public CutInHolder setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public String getEventName() {
        return eventName;
    }

    public CutInHolder setCutInId(int cutInId) {
        this.cutInId = cutInId;
        return this;
    }

    public int getCutInId() {
        return cutInId;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}