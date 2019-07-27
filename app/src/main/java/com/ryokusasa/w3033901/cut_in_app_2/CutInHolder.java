package com.ryokusasa.w3033901.cut_in_app_2;


//カットインとイベントをまとめたホルダー
public class CutInHolder implements Cloneable {
    private final String TAG = "CutInHolder";

    private String eventName;
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
}