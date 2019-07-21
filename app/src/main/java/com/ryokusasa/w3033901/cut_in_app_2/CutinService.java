package com.ryokusasa.w3033901.cut_in_app_2;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Ryokusasa on 2017/12/18.
 */

public class CutinService extends Service{

    private final String TAG = "FilterService";
    private final IBinder iBinder = new ServiceBinder();
    private boolean onView = false;
    View v;
    LinearLayout layout;
    WindowManager windowManager;

    //自身を返す
    public class ServiceBinder extends Binder{
        CutinService getService(){
            return CutinService.this;
        }
    }

    @Override
    public void onCreate()
    {

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

        //二度実行禁止
        onView = true;

        //レイアウト読み込む用
        final LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

        //重ね合わせするViewの設定
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );

        //タッチ用
        final WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        //WindowManager取得
        windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);

        //Viewを作成
        v = layoutInflater.inflate(R.layout.filter, null);
        layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ));
        layout.setBackgroundColor(Color.argb(30, 0, 0,0));


        //Viewにフィルターセット
        v.setBackgroundColor(Color.argb(0,0,0,0));

        //重ね合わせる
        windowManager.addView(v, layoutParams);
        windowManager.addView(layout,layoutParams2);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "onTouch:" + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        //画面タッチ処理
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        layout.setClickable(false);
        layout.setFocusable(false);


        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(TAG, "LongClick");
                return false;
            }
        });

        //通知作成
        Intent testIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, testIntent, 0);
        Notification notification;
        //api16以降はbuild()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notification = new Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("カットインアプリ")
                    .setContentText("動作中")
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        }else{
            notification = new Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("カットインアプリ")
                    .setContentText("動作中")
                    .setSmallIcon(R.mipmap.ic_launcher).getNotification();
        }

        startForeground(startId, notification);
        return START_NOT_STICKY;

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy()");

        //二度実行禁止
        onView = false;

        //View削除
        windowManager.removeView(v);
        windowManager.removeView(layout);
    }
}
