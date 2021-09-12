package com.ryokusasa.cut_in_app_2;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ryokusasa.cut_in_app_2.CutIn.CutIn;
import com.ryokusasa.cut_in_app_2.CutIn.CutInHolder;
import com.ryokusasa.cut_in_app_2.Dialog.AppData;

import java.util.ArrayList;

import static com.ryokusasa.cut_in_app_2.PermissionUtils.checkOverlayPermission;

import androidx.constraintlayout.widget.ConstraintLayout;

public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";

    private static UtilCommon sInstance;

    //カットインリスト
    public ArrayList<CutIn> cutInList = new ArrayList<CutIn>();
    //カットイン関連
    public ArrayList<CutInHolder> cutInHolderList = new ArrayList<CutInHolder>();//TODO　後々外部ファイル
    private ConstraintLayout cutInView;
    private LinearLayout layout;
    private WindowManager windowManager;

    //アプリデータ
    public ArrayList<AppData> appDataList = new ArrayList<AppData>();

    //コネクション作成
    private ServiceConnection serviceConnection;
    private CutInService mService;
    public boolean isConnection = false;

    //パーミッション
    private PermissionUtils permissionUtils;


    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;

        cutInView = new ConstraintLayout(this);
    }

    //TODO 再生
    public void play(){
        Log.i(TAG, "play");
    }

    public void play(int id){
        Log.i(TAG, "play" + id);
        setCutIn(cutInList.get(id));
        cutInList.get(id).play();
    }

    //サービス接続
    public void connectService(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Binderからservice取得
                mService = ((CutInService.ServiceBinder)service).getService();
                Log.i(TAG, "onConnected");
                isConnection = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                Log.i("TAG", "Disconnected");
                isConnection = false;
            }
        };
    }

    //カットインサービス開始
    public void startCutInService(Context context){
        if (checkOverlayPermission(context) && !isConnection){
            Intent intent = new Intent(getApplicationContext(), CutInService.class);
            startService(intent);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    //カットインサービス終了
    public void endCutInService(Context context){
        if(checkOverlayPermission(context) && isConnection){
            Intent intent = new Intent(getApplicationContext(), CutInService.class);
            unbindService(serviceConnection);
            stopService(intent);
            isConnection = false;
        }
    }

    //カットイン削除
    public void removeCutIn(CutIn cutIn){
        cutInList.remove(cutIn);

        //同じカットインをホルダーリストから削除
        for (CutInHolder cih : cutInHolderList){
            if(cih.getCutIn().equals(cutIn)){
                cih.setCutIn(cutInList.get(0));
            }
        }
    }

    //カットイン＆タッチウィンドウ設定
    public void windowSetting(){
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
        layoutInflater.inflate(R.layout.filter, cutInView);
        layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ));
        layout.setBackgroundColor(Color.argb(0, 0, 0,0));


        //Viewにフィルターセット
        cutInView.setBackgroundColor(Color.argb(0,0,0,0));

        //重ね合わせる
        windowManager.addView(cutInView, layoutParams);
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
    }

    public void removeWindow(){
        //View削除
        windowManager.removeView(cutInView);
        windowManager.removeView(layout);
    }

    //カットイン適用
    //TODO: 重いのでビュー切り替えなしに
    public void setCutIn(CutIn cutIn){
        cutInView.removeAllViews();
        cutIn.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));
        cutInView.addView(cutIn);
    }

    //コンテキストをどこからでも取得できるように
    public static synchronized UtilCommon getInstance() {
        return sInstance;
    }
}
