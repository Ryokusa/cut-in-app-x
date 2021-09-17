package com.ryokusasa.cut_in_app.CutIn;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ryokusasa.cut_in_app.Dialog.AppData;
import com.ryokusasa.cut_in_app.EventType;
import com.ryokusasa.cut_in_app.UtilCommon;

import java.io.Serializable;
import java.util.ArrayList;

//カットインとイベントをまとめたホルダー
public class CutInHolder implements Serializable {
    private final String TAG = "CutInHolder";

    private EventType eventType;
    transient private AppData appData;
    private String packageName;
    transient private CutIn cutIn;
    private String cutInName;


    //コンストラクタ
    public CutInHolder(EventType eventType, CutIn cutIn) {
        //イベントネーム
        setEventType(eventType);

        //カットイン
        setCutIn(cutIn);

    }

    //データ読み込み時に不足変数をロード
    public boolean loadComponent(ArrayList<CutIn> cutInList) {
        PackageManager pm = UtilCommon.getInstance().getPackageManager();

        if(eventType == EventType.APP_NOTIFICATION) {
            ApplicationInfo appInfo;
            try {
                appInfo = pm.getApplicationInfo(packageName, 0);
                appData = new AppData(packageName, pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(packageName));
            } catch (PackageManager.NameNotFoundException e) {
                Log.i(TAG, "アプリが存在しません");
                return false;
            }
        }

        for (CutIn cutIn : cutInList){
            if(cutIn.title.equals(cutInName)){
                this.cutIn = cutIn;
                return true;
            }
        }
        return false;
    }

    public CutInHolder(EventType eventType, CutIn cutIn, AppData appData){
        this(eventType, cutIn);
        this.setAppData(appData);
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
        this.cutInName = cutIn.title;
        return this;
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
        this.packageName = appData.getPackageName();
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