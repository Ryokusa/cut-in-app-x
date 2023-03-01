package com.ryokusasa.cut_in_app

import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

//通知リスナーサービス
class CustomNotificationListenerService : NotificationListenerService() {
    companion object {
        const val TAG = "CustomNotificationListenerService"
    }

    private lateinit var utilCommon: UtilCommon
    override fun onCreate() {
        super.onCreate()
        utilCommon = application as UtilCommon
        Log.i(TAG, "起動")
    }

    //通知が来たとき
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        //アプリ名を取得
        val pm = packageManager
        val appName: String = try {
            pm.getApplicationLabel(pm.getApplicationInfo(sbn.packageName, 0)).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            //名前が見つからなかった場合
            ""
        }

        //通知内容連絡
        /*
        01-23 22:14:02.525 23162-23182/com.example.fripl.cut_in_app I/CutInService: onNotificationPosted:LINE
        01-23 22:14:02.525 23162-23182/com.example.fripl.cut_in_app I/CutInService: チャケ : 新着メッセージがあります。
        01-23 22:14:03.630 23162-23183/com.example.fripl.cut_in_app I/CutInService: onNotificationPosted:LINE
        01-23 22:14:03.630 23162-23183/com.example.fripl.cut_in_app I/CutInService: チャケ : いでうらひろゆきかっこいい
         */Log.i(TAG, "onNotificationPosted:$appName")
        if (sbn.notification.tickerText != null) {
            Log.i(TAG, "" + sbn.notification.tickerText.toString())
        }

        //TODO: 対応したカットイン再生
        utilCommon.play(sbn.packageName)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        Log.i(TAG, "onNotificationRemoved")
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }
}