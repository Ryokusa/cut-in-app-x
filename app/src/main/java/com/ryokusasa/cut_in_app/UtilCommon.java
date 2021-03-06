package com.ryokusasa.cut_in_app;

import android.animation.TimeInterpolator;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowMetrics;
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
import com.ryokusasa.cut_in_app.ImageUtils.UriTypeHierarchyAdapter;

import java.util.ArrayList;

import static com.ryokusasa.cut_in_app.PermissionUtils.checkOverlayPermission;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

//TODO: ???????????????????????????????????????(??????????????????????????????????????????????????????????????????????????????)
//TODO: ????????????????????????????????????????????????
//TODO: ???????????????????????????????????????
//TODO: ???????????????????????????????????????
public class UtilCommon extends Application {
    private static final String TAG = "UtilCommon";

    //????????????
    private static final String SAVE_KEY = "cutInList";
    private static final String SAVE_HOLDER_KEY = "cutInHolderList";
    private static final String VER_KEY = "cutInVersion";

    //?????????????????????
    public static boolean DEBUG = true;

    private static UtilCommon sInstance;

    //????????????????????????
    public ArrayList<CutIn> cutInList = new ArrayList<>();
    //?????????????????????
    public ArrayList<CutInHolder> cutInHolderList = new ArrayList<>();
    private ConstraintLayout cutInView;
    private LinearLayout layout;
    private WindowManager windowManager;
    private CutInCanvas cutInCanvas;
    public CutIn initialCutIn = new CutIn("Initial CutIn", new ImageData(R.drawable.ic_launcher_background));

    //????????????
    private Gson gson;
    private SharedPreferences sp;

    //??????????????????
    public ArrayList<AppData> appDataList = new ArrayList<>();

    //????????????????????????
    private ServiceConnection serviceConnection;
    private CutInService mService;
    public boolean isConnection = false;

    //????????????
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
        getDisplayInfo();

        //?????????
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        /* ????????????????????????????????? */
        CutIn cutIn1 = new CutIn("None CutIn", new ImageData(R.drawable.ic_launcher_background));
        AnimObj ao = new ImageObj(new ImageData(R.drawable.foo),400, 0, 300, 300 );
        ao.addMove(200, 400, 1700, new BounceInterpolator());
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
        cutInList.get(id).play(cutInCanvas);
    }

    //?????????????????????????????????
    public void play(String packageName){
        for(CutInHolder cutInHolder : cutInHolderList){
            if (cutInHolder.getEventType() == EventType.APP_NOTIFICATION) {
                if (cutInHolder.getAppData().getPackageName().equals(packageName)) {
                    cutInHolder.getCutIn().play(cutInCanvas);
                }
            }
        }
    }

    //??????????????????
    public void connectService(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Binder??????service??????
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

    //?????????????????????????????????
    public void startCutInService(Context context){
        if (checkOverlayPermission(context) && !isConnection){
            Intent intent = new Intent(getApplicationContext(), CutInService.class);
            startService(intent);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    //?????????????????????????????????
    public void endCutInService(Context context){
        if(checkOverlayPermission(context) && isConnection){
            Intent intent = new Intent(getApplicationContext(), CutInService.class);
            unbindService(serviceConnection);
            stopService(intent);
            isConnection = false;
        }
    }

    //?????????????????????
    public void removeCutIn(CutIn cutIn){
        cutInList.remove(cutIn);

        //?????????????????????????????????????????????????????????
        for (CutInHolder cih : cutInHolderList){
            if(cih.getCutIn().equals(cutIn)){
                cih.setCutIn(initialCutIn);
            }
        }
    }

    //????????????????????????????????????????????????
    public void windowSetting(){
        //??????????????????????????????
        final LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

        //?????????????????????View?????????
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );

        //????????????
        final WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        //WindowManager??????
        windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);

        //View?????????
        layoutInflater.inflate(R.layout.filter, cutInView);
        layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ));
        layout.setBackgroundColor(Color.argb(0, 0, 0,0));


        //View???????????????????????????
        cutInView.setBackgroundColor(Color.argb(0,0,0,0));

        //??????????????????
        windowManager.addView(cutInView, layoutParams);
        windowManager.addView(layout,layoutParams2);

        layout.setOnTouchListener((v, event) -> {
            Log.i(TAG, "onTouch:" + event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    //?????????????????????
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
        //View??????
        windowManager.removeView(cutInView);
        windowManager.removeView(layout);
    }

    public void saveCutInHolder(){
        String json = gson.toJson(cutInHolderList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SAVE_HOLDER_KEY, json);
        editor.apply();
        Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
    }

    public boolean loadCutInHolder() {
        String json = sp.getString(SAVE_HOLDER_KEY, null);
        if(json == null){
            Toast.makeText(this, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        }

        cutInHolderList = (ArrayList<CutInHolder>) gson.fromJson(json, new TypeToken<ArrayList<CutInHolder>>(){}.getType());
        Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();

        for (CutInHolder cutInHolder : cutInHolderList){
            if (!cutInHolder.loadComponent()) {
                Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
            Log.i(TAG, "??????????????????????????????");
        }
    }

    public void saveCutInList(boolean f){
        if(f) {
            String json = gson.toJson(cutInList);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(SAVE_KEY, json);
            editor.putFloat(VER_KEY, CutIn.CUT_IN_VERSION);
            editor.apply();
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG, "??????????????????");
        }
    }

    public boolean loadCutInList(){
        //????????????????????????????????????
        if(sp.getFloat(VER_KEY, CutIn.CUT_IN_VERSION) != CutIn.CUT_IN_VERSION){
            Toast.makeText(this, "???????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        }

        //???????????????????????????
        String json = sp.getString(SAVE_KEY, null);
        if(json == null){
            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        }

        cutInList = (ArrayList<CutIn>) gson.fromJson(json, new TypeToken<ArrayList<CutIn>>(){}.getType());
        Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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

    //??????????????????????????????
    public DisplayInfo getDisplayInfo(){
        return new DisplayInfo(cutInCanvas.getWidth(), cutInCanvas.getHeight());
    }

    //???????????????????????????????????????????????????????????????
    public static synchronized UtilCommon getInstance() {
        return sInstance;
    }

    public static ImageUtils getImageUtils(){
        return imageUtils;
    }

    class DisplayInfo{
        private int width;
        private int height;
        private float widthDp;
        private float heightDp;

        public DisplayInfo(int width, int height){
            this.width = width;
            this.height = height;

            widthDp = UtilCommon.px2dp(width);
            heightDp = UtilCommon.px2dp(height);
        }
    }

    public static float px2dp(int px){
        DisplayMetrics metrics = UtilCommon.getInstance().getResources().getDisplayMetrics();
        return px / metrics.density;
    }

    public static float dp2px(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,UtilCommon.getInstance().getResources().getDisplayMetrics());
    }
}
