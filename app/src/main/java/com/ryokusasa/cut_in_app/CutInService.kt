package com.ryokusasa.cut_in_app

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ryokusasa.cut_in_app.Activity.MainActivity
import com.ryokusasa.cut_in_app.CustomNotificationListenerService

/**
 * Created by Ryokusasa on 2017/12/18.
 */
class CutInService : Service() {

    companion object {
        const val TAG = "CutInService"
    }

    private val iBinder: IBinder = ServiceBinder()
    private lateinit var utilCommon: UtilCommon

    //別スレッドから実行するためのHandler
    //実際は通知受け取り時は別スレッドなため、再生処理をメインスレッドに渡すため
    private lateinit var handler: Handler

    //自身を返す
    inner class ServiceBinder : Binder() {
        val service: CutInService
            get() = this@CutInService
    }

    override fun onCreate() {
        Log.i(TAG, "onCreate()")

        //メインスレッドのHandler取得
        handler = Handler(Looper.getMainLooper())
        utilCommon = application as UtilCommon
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "onBind")
        return iBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(TAG, "unBind")
        return true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        //カットイン等ウィンドウの設定
        utilCommon.windowSetting()

        //通知作成
        val testIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, testIntent, PendingIntent.FLAG_IMMUTABLE)
        val notification: Notification
        val channelID =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(
                "cut_in_service",
                "CutIn_Service"
            ) else ""
        notification = NotificationCompat.Builder(this, channelID)
            .setContentIntent(pendingIntent)
            .setContentTitle("カットインアプリ")
            .setContentText("動作中")
            .setSmallIcon(R.mipmap.ic_launcher).build()
        startForeground(startId, notification)
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")

        //オーバーレイウィンドウ削除
        utilCommon.removeWindow()

        super.onDestroy()
    }
}