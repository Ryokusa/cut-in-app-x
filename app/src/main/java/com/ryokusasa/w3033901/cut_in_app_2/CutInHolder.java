package com.ryokusasa.w3033901.cut_in_app_2;


//カットインとイベントをまとめたホルダー
public class CutInHolder implements Cloneable {
    private final String TAG = "CutInHolder";

    private String eventName;
    private int cutInId;


    //コンストラクタ
    public CutInHolder(String eventName, int cutInId) {
        //イベントネーム
        this.eventName = eventName;

        //カットイン
        this.cutInId = cutInId;

    }

    //再生
    public void play() {
        //TODO 再生処理
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setCutInId(int cutInId) {
        this.cutInId = cutInId;
    }

    public int getCutInId() {
        return cutInId;
    }
}