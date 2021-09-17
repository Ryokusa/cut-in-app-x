package com.ryokusasa.cut_in_app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;

import com.ryokusasa.cut_in_app.AppDataManager.AnimObj;
import com.ryokusasa.cut_in_app.AppDataManager.ImageObj;
import com.ryokusasa.cut_in_app.CutIn.CutIn;
import com.ryokusasa.cut_in_app.CutIn.CutInHolder;
import com.ryokusasa.cut_in_app.Dialog.AppData;
import com.ryokusasa.cut_in_app.R;

import java.util.ArrayList;

import static com.ryokusasa.cut_in_app.PermissionUtils.checkOverlayPermission;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";

    private static UtilCommon sInstance;

    //カットインリスト
    public ArrayList<CutIn> cutInList = new ArrayList<>();
    //カットイン関連
    public ArrayList<CutInHolder> cutInHolderList = new ArrayList<>();//TODO　後々外部ファイル
    private ConstraintLayout cutInView;
    private LinearLayout layout;
    private WindowManager windowManager;
    private CutInCanvas cutInCanvas;
    private final int selCutInId = -1;

    //アプリデータ
    public ArrayList<AppData> appDataList = new ArrayList<>();

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
        cutInCanvas = new CutInCanvas(this);
        cutInView.addView(cutInCanvas, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        /* とりあえずのカットイン */
        CutIn cutIn1 = new CutIn(this, "None CutIn", R.drawable.ic_launcher_background);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.foo, null);
        AnimObj ao = new ImageObj(ResourcesCompat.getDrawable(getResources(), R.drawable.foo, null),400, 0, 300, 300 );
        ao.addMove(200, 400, 1700, new BounceInterpolator());
        cutIn1.addAnimObj(ao);
        cutInList.add(cutIn1);
        cutInList.add(new CutIn(this, "First CutIn", R.mipmap.ic_launcher));
        cutInList.add(new CutIn(this, "Second CutIn", R.mipmap.ic_launcher_round));
        cutInHolderList.add(new CutInHolder(EventType.SCREEN_ON, cutInList.get(0)));
        cutInHolderList.add(new CutInHolder(EventType.LOW_BATTERY, cutInList.get(0)));
    }

    //TODO 再生
    public void play(){
        Log.i(TAG, "play");
    }

    public void play(int id){
        Log.i(TAG, "play" + id);
        cutInList.get(id).play(cutInCanvas);
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

        layout.setOnTouchListener((v, event) -> {
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
        });
        layout.setClickable(false);
        layout.setFocusable(false);


        layout.setOnLongClickListener(v -> {
            Log.i(TAG, "LongClick");
            return false;
        });
    }

    public void removeWindow() {
        //View削除
        windowManager.removeView(cutInView);
        windowManager.removeView(layout);
    }

    //コンテキストをどこからでも取得できるように
    public static synchronized UtilCommon getInstance() {
        return sInstance;
    }
}
