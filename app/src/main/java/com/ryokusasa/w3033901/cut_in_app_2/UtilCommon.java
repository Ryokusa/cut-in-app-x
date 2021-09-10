package com.ryokusasa.w3033901.cut_in_app_2;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutIn;

import java.util.ArrayList;

import static com.ryokusasa.w3033901.cut_in_app_2.PermissionUtils.checkOverlayPermission;

public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";

    private static UtilCommon sInstance;

    //カットインリスト
    public ArrayList<CutIn> cutInList = new ArrayList<CutIn>();

    //コネクション作成
    private ServiceConnection serviceConnection;
    private CutInService mService;
    public boolean isConnection = false;


    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }

    //TODO 再生
    public void play(){
        Log.i(TAG, "play");
    }

    public void play(int id){
        Log.i(TAG, "play" + id);
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

    //コンテキストをどこからでも取得できるように
    public static synchronized UtilCommon getInstance() {
        return sInstance;
    }
}
