package com.ryokusasa.w3033901.cut_in_app_2;


//カットインとイベントをまとめたホルダー
public class CutInHolder {
    private final String TAG = "CutInHolder";

    private EventType eventType;
    private String packageName = null;
    private int cutInId;


    //コンストラクタ
    public CutInHolder(EventType eventType, int cutInId) {
        //イベントネーム
        setEventType(eventType);

        //カットイン
        setCutInId(cutInId);

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

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}