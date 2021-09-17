package com.ryokusasa.cut_in_app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ryokusasa.cut_in_app.Activity.MainActivity;

/**
 * Created by Ryokusasa on 2017/12/18.
 */

public class CutInService extends Service{

    private final String TAG = "FilterService";
    private final IBinder iBinder = new ServiceBinder();
    View v;                 //カットインを表示するView
    LinearLayout layout;
    WindowManager windowManager;

    UtilCommon utilCommon;

    //別スレッドから実行するためのHandler
    //実際は通知受け取り時は別スレッドなため、再生処理をメインスレッドに渡すため
    Handler handler;

    //自身を返す
    public class ServiceBinder extends Binder{
        CutInService getService(){
            return CutInService.this;
        }
    }

    @Override
    public void onCreate()
    {
        //メインスレッドのHandler取得
        handler = new Handler();

        utilCommon = (UtilCommon) getApplication();

        //リスナーサービス起動
        Intent i = new Intent(CutInService.this, CustomNotificationListenerService.class);
        startService(i);
    }

    @Override
    public IBinder onBind(Intent intent){
        Log.i(TAG, "onBind");
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        Log.i(TAG, "unBind");
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //カットイン等ウィンドウの設定
        utilCommon.windowSetting();

        //通知作成
        Intent testIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, testIntent, 0);
        Notification notification;

        notification = new Notification.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle("カットインアプリ")
                .setContentText("動作中")
                .setSmallIcon(R.mipmap.ic_launcher).build();

        startForeground(startId, notification);
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy()");

        //オーバーレイウィンドウ削除
        utilCommon.removeWindow();

        //通知リスナー削除
        Intent i = new Intent(CutInService.this, CustomNotificationListenerService.class);
        stopService(i);

        super.onDestroy();
    }


}
