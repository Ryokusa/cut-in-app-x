package com.ryokusasa.cut_in_app;

import android.animation.TimeInterpolator;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ryokusasa.cut_in_app.AnimObj.AnimObj;
import com.ryokusasa.cut_in_app.AnimObj.AnimObjDeserializer;
import com.ryokusasa.cut_in_app.AnimObj.ImageObj;
import com.ryokusasa.cut_in_app.AnimObj.TextObj;
import com.ryokusasa.cut_in_app.CutIn.CutIn;
import com.ryokusasa.cut_in_app.CutIn.CutInHolder;
import com.ryokusasa.cut_in_app.Dialog.AppData;
import com.ryokusasa.cut_in_app.ImageUtils.ImageData;
import com.ryokusasa.cut_in_app.ImageUtils.ImageUtils;

import java.util.ArrayList;

import static com.ryokusasa.cut_in_app.PermissionUtils.checkOverlayPermission;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";

    //保存キー
    private static final String SAVE_KEY = "cutInList";
    private static final String VER_KEY = "cutInVersion";

    private static UtilCommon sInstance;

    //カットインリスト
    public ArrayList<CutIn> cutInList = new ArrayList<>();
    //カットイン関連
    public ArrayList<CutInHolder> cutInHolderList = new ArrayList<>();//TODO　後々外部ファイル
    private ConstraintLayout cutInView;
    private LinearLayout layout;
    private WindowManager windowManager;
    private CutInCanvas cutInCanvas;
    //private final int selCutInId = -1;
    private Gson gson;
    private SharedPreferences sp;

    //アプリデータ
    public ArrayList<AppData> appDataList = new ArrayList<>();

    //コネクション作成
    private ServiceConnection serviceConnection;
    private CutInService mService;
    public boolean isConnection = false;

    //画像管理
    public static ImageUtils imageUtils;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
        imageUtils = new ImageUtils();
        makeGson();

        cutInView = new ConstraintLayout(this);
        cutInCanvas = new CutInCanvas(this);
        cutInView.addView(cutInCanvas, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        /* とりあえずのカットイン */
//        CutIn cutIn1 = new CutIn("None CutIn", new ImageData(R.drawable.ic_launcher_background));
//        AnimObj ao = new ImageObj(new ImageData(R.drawable.foo),400, 0, 300, 300 );
//        ao.addMove(200, 400, 1700, new BounceInterpolator());
//        cutIn1.addAnimObj(ao);
//        cutInList.add(cutIn1);
//        cutInList.add(new CutIn("First CutIn", new ImageData(R.mipmap.ic_launcher)));
//        cutInList.add(new CutIn("Second CutIn", new ImageData(R.mipmap.ic_launcher_round)));


        //保存用
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        loadCutInList();

        cutInHolderList.add(new CutInHolder(EventType.SCREEN_ON, cutInList.get(0)));
        cutInHolderList.add(new CutInHolder(EventType.LOW_BATTERY, cutInList.get(0)));

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

    //TODO: 保存処理
    public void saveCutInList(){
        String json = gson.toJson(cutInList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SAVE_KEY, json);
        editor.putFloat(VER_KEY, CutIn.CUT_IN_VERSION);
        editor.apply();
        Toast.makeText(this, "カットイン保存", Toast.LENGTH_SHORT).show();
    }

    //TODO: 読み込み処理
    public boolean loadCutInList(){
        //カットインバージョン照合
        if(sp.getFloat(VER_KEY, CutIn.CUT_IN_VERSION) != CutIn.CUT_IN_VERSION){
            Toast.makeText(this, "読み込んだカットインのバージョンが違います", Toast.LENGTH_SHORT).show();
            return false;
        }

        //カットイン読み込み
        String json = sp.getString(SAVE_KEY, null);
        if(json == null){
            Toast.makeText(this, "カットインが存在しません", Toast.LENGTH_SHORT).show();
            return false;
        }

        cutInList = (ArrayList<CutIn>) gson.fromJson(json, new TypeToken<ArrayList<CutIn>>(){}.getType());
        Toast.makeText(this, "カットイン読み込み完了", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void makeGson(){
        RuntimeTypeAdapterFactory<AnimObj> animObjAdapterFactory = RuntimeTypeAdapterFactory.of(AnimObj.class)
                .registerSubtype(ImageObj.class)
                .registerSubtype(TextObj.class);
        RuntimeTypeAdapterFactory<TimeInterpolator> timeInterpolatorAdapterFactory = RuntimeTypeAdapterFactory.of(TimeInterpolator.class)
                .registerSubtype(LinearInterpolator.class)
                .registerSubtype(AccelerateDecelerateInterpolator.class)
                .registerSubtype(AccelerateInterpolator.class)
                .registerSubtype(AnticipateInterpolator.class)
                .registerSubtype(AnticipateOvershootInterpolator.class)
                .registerSubtype(BounceInterpolator.class)
                .registerSubtype(CycleInterpolator.class)
                .registerSubtype(DecelerateInterpolator.class)
                .registerSubtype(OvershootInterpolator.class);

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(animObjAdapterFactory)
                .registerTypeAdapterFactory(timeInterpolatorAdapterFactory)
                .create();
    }

    //コンテキストをどこからでも取得できるように
    public static synchronized UtilCommon getInstance() {
        return sInstance;
    }

    public static ImageUtils getImageUtils(){
        return imageUtils;
    }
}
