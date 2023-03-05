package com.ryokusasa.cut_in_app;

import android.animation.TimeInterpolator;
import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ryokusasa.cut_in_app.anim_obj.AnimObj;
import com.ryokusasa.cut_in_app.anim_obj.ImageObj;
import com.ryokusasa.cut_in_app.anim_obj.TextObj;
import com.ryokusasa.cut_in_app.cut_in.CutIn;
import com.ryokusasa.cut_in_app.cut_in.cut_in_view.CutInViewController;
import com.ryokusasa.cut_in_app.cut_in.CutInHolder;
import com.ryokusasa.cut_in_app.dialog.AppData;
import com.ryokusasa.cut_in_app.image_utils.ImageData;
import com.ryokusasa.cut_in_app.image_utils.ImageUtils;
import com.ryokusasa.cut_in_app.image_utils.UriTypeHierarchyAdapter;

import java.util.ArrayList;

//TODO: カットインタイトル変更機能(タイトルは唯一にならないと行けないのでここで一括管理)
//TODO: カットインホルダーにアプリ名表示
//TODO: カットインホルダー削除機能
//TODO: 重複したイベントの追加防止
public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";

    //保存キー
    private static final String SAVE_KEY = "cutInList";
    private static final String SAVE_HOLDER_KEY = "cutInHolderList";
    private static final String VER_KEY = "cutInVersion";

    //デバッグフラグ
    public static boolean DEBUG = true;

    private static UtilCommon sInstance;

    //カットインリスト
    public ArrayList<CutIn> cutInList = new ArrayList<>();
    //カットイン関連
    public ArrayList<CutInHolder> cutInHolderList = new ArrayList<>();


    public CutIn initialCutIn = new CutIn("Initial CutIn", new ImageData(R.drawable.ic_launcher_background));
    public CutInViewController cutInViewController;

    //保存関連
    private Gson gson;
    private SharedPreferences sp;

    //アプリデータ
    public ArrayList<AppData> appDataList = new ArrayList<>();

    //画像管理
    public static ImageUtils imageUtils;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
        imageUtils = new ImageUtils();
        makeGson();

        cutInViewController = new CutInViewController(this);

        //保存用
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        /* とりあえずのカットイン */
        CutIn cutIn1 = new CutIn("None CutIn", new ImageData(R.drawable.ic_launcher_background));
        AnimObj ao = new ImageObj(new ImageData(R.drawable.foo),400, 0, 300, 300 );
        ao.addMove(200, 400, 1700, new BounceInterpolator());
        ao.addRotate(200, 2*Math.PI, new DecelerateInterpolator());
        cutIn1.addAnimObj(ao);
        cutIn1.setFrameNum(250);
        cutInList.add(cutIn1);
        cutInList.add(new CutIn("First CutIn", new ImageData(R.mipmap.ic_launcher)));
        cutInList.add(new CutIn("Second CutIn", new ImageData(R.mipmap.ic_launcher_round)));

        cutInHolderList.add(new CutInHolder(EventType.SCREEN_ON, initialCutIn));
        cutInHolderList.add(new CutInHolder(EventType.LOW_BATTERY, initialCutIn));

    }

    public void play(int id){
        Log.i(TAG, "play" + id);
        cutInList.get(id).play(cutInViewController.getCutInCanvas());
    }

    //パッケージネームで再生
    public void play(String packageName){
        for(CutInHolder cutInHolder : cutInHolderList){
            if (cutInHolder.getEventType() == EventType.APP_NOTIFICATION) {
                if (cutInHolder.getAppData().getPackageName().equals(packageName)) {
                    cutInHolder.getCutIn().play(cutInViewController.getCutInCanvas());
                }
            }
        }
    }


    //カットイン削除
    public void removeCutIn(CutIn cutIn){
        cutInList.remove(cutIn);

        //同じカットインをホルダーリストから削除
        for (CutInHolder cih : cutInHolderList){
            if(cih.getCutIn().equals(cutIn)){
                cih.setCutIn(initialCutIn);
            }
        }
    }

    public void saveCutInHolder(){
        String json = gson.toJson(cutInHolderList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SAVE_HOLDER_KEY, json);
        editor.apply();
        Toast.makeText(this, "カットインホルダー保存", Toast.LENGTH_SHORT).show();
    }

    public boolean loadCutInHolder() {
        String json = sp.getString(SAVE_HOLDER_KEY, null);
        if(json == null){
            Toast.makeText(this, "カットインホルダーが存在しません", Toast.LENGTH_SHORT).show();
            return false;
        }

        cutInHolderList = (ArrayList<CutInHolder>) gson.fromJson(json, new TypeToken<ArrayList<CutInHolder>>(){}.getType());
        Toast.makeText(this, "カットインホルダー読み込み完了", Toast.LENGTH_SHORT).show();

        for (CutInHolder cutInHolder : cutInHolderList){
            if (!cutInHolder.loadComponent()) {
                Toast.makeText(this, "カットインホルダー読み込み失敗", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void save(){
        save(true);
    }

    public void save(boolean f){
        saveCutInList(f);
        saveCutInHolder();
    }

    public void load(){
        if (!loadCutInList() | !loadCutInHolder()){
            Log.i(TAG, "読み込めませんでした");
        }
    }

    public void saveCutInList(boolean f){
        if(f) {
            String json = gson.toJson(cutInList);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(SAVE_KEY, json);
            editor.putFloat(VER_KEY, CutIn.CUT_IN_VERSION);
            editor.apply();
            Toast.makeText(this, "カットイン保存", Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG, "保存しません");
        }
    }

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


        /*
        * AnimObj
        * TimeInterpolator
        * Uri
        */
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(animObjAdapterFactory)
                .registerTypeAdapterFactory(timeInterpolatorAdapterFactory)
                .registerTypeHierarchyAdapter(Uri.class, new UriTypeHierarchyAdapter())
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
