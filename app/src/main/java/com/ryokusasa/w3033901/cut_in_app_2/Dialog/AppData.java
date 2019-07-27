package com.ryokusasa.w3033901.cut_in_app_2.Dialog;

import android.graphics.drawable.Drawable;

/**
 * Created by Ryokusasa on 2017/12/17.
 * アプリの各種データ
 */

public class AppData {
    private String appName;
    private Drawable iconDrawable;
    private String packageName;
    private boolean isUsed;

    public AppData(String packageName, String appName, Drawable iconDrawable){
        this.packageName = packageName;
        this.appName = appName;
        this.iconDrawable = iconDrawable;
        this.isUsed = false;
    }

    public AppData(String packageName, String appName, Drawable iconDrawable, boolean isUsed){
        this.packageName = packageName;
        this.appName = appName;
        this.iconDrawable = iconDrawable;
        this.isUsed = isUsed;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public void setIconId(Drawable iconDrawable)
    {
        this.iconDrawable = iconDrawable;
    }

    public String getAppName()
    {
        return this.appName;
    }

    public Drawable getIconDrawable()
    {
        return this.iconDrawable;
    }

    public boolean isUsed(){
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public void setPackageName(String packageName){ this.packageName = packageName; }
    public String getPackageName(){return this.packageName; }


}