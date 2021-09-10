package com.ryokusasa.w3033901.cut_in_app_2;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutIn;
import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutInHolder;
import com.ryokusasa.w3033901.cut_in_app_2.Dialog.AppData;

import java.util.ArrayList;

import static com.ryokusasa.w3033901.cut_in_app_2.PermissionUtils.checkOverlayPermission;

public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";

    private static UtilCommon sInstance;

    //カットインリスト
    public ArrayList<CutIn> cutInList = new ArrayList<CutIn>();
    //カットイン関連
    public ArrayList<CutInHolder> cutInHolderList = new ArrayList<CutInHolder>();//TODO　後々外部ファイル

    //アプリデータ
    public ArrayList<AppData> appDataList = new ArrayList<AppData>();

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

    //コンテキストをどこからでも取得できるように
    public static synchronized UtilCommon getInstance() {
        return sInstance;
    }
}
