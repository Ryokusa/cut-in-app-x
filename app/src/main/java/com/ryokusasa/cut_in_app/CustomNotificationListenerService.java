package com.ryokusasa.cut_in_app;

import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

//通知リスナーサービス
//前回と違って別サービス
//理由はこのリスナーサービスのbind対象が 本来MainActivityでないため、不具合が生じる
public class CustomNotificationListenerService extends NotificationListenerService {
    private final String TAG = "CNLS";

    //通知が来たとき
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        super.onNotificationPosted(sbn);

        //アプリ名を取得
        PackageManager pm = getPackageManager();
        String appName;
        try {
            appName = pm.getApplicationLabel(pm.getApplicationInfo(sbn.getPackageName(), 0)).toString();
        }catch(PackageManager.NameNotFoundException e){
            //名前が見つからなかった場合
            appName = "";
        }

        //通知内容連絡
        /*
        01-23 22:14:02.525 23162-23182/com.example.fripl.cut_in_app I/CutInService: onNotificationPosted:LINE
        01-23 22:14:02.525 23162-23182/com.example.fripl.cut_in_app I/CutInService: チャケ : 新着メッセージがあります。
        01-23 22:14:03.630 23162-23183/com.example.fripl.cut_in_app I/CutInService: onNotificationPosted:LINE
        01-23 22:14:03.630 23162-23183/com.example.fripl.cut_in_app I/CutInService: チャケ : いでうらひろゆきかっこいい
         */
        Log.i(TAG, "onNotificationPosted:" + appName);
        if(sbn.getNotification().tickerText != null){
            Log.i(TAG, "" + sbn.getNotification().tickerText.toString());
        }


        final String appNameFinal = appName;

        //別スレッドからUIを操作するのでメインスレッドにまかせる
        //アプリ名より対応したカットイン再生
        /*
        handler.post(new Runnable() {
            @Override
            public void run() {
                play(appNameFinal);
            }
        });
        */
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i(TAG, "onNotificationRemoved");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
