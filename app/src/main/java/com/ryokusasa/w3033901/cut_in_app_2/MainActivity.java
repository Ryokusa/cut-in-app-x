package com.ryokusasa.w3033901.cut_in_app_2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ryokusasa.w3033901.cut_in_app_2.Animation.KeyFrame;
import com.ryokusasa.w3033901.cut_in_app_2.Animation.KeyFrameAnimation;
import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutIn;
import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutInHolder;
import com.ryokusasa.w3033901.cut_in_app_2.Dialog.AppData;
import com.ryokusasa.w3033901.cut_in_app_2.Dialog.AppDialog;

import java.util.ArrayList;

import static com.ryokusasa.w3033901.cut_in_app_2.PermissionUtils.*;

/*
  メインアクティビティ
  by Ryokusasa
 */


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";   //Log

    /* サービス関連 */
    private boolean isConnection = false;   //サービスにつながってるか
    private CutInService mService;             //サービス

    //コネクション作成
    private ServiceConnection serviceConnection;

    /* パーミッション関連 */
    private final int OVERLAY_PERMISSION_REQUEST_CODE = 893;    //リクエストコード
    private final int NOTIFICATION_PERMISSION_REQUEST_CODE = 810;

    //レイアウト
    private LinearLayout frameList;

    //カットイン関連
    private static ArrayList<CutIn> cutInList = new ArrayList<CutIn>();
    private static ArrayList<CutInHolder> cutInHolderList = new ArrayList<CutInHolder>();//TODO　後々外部ファイル

    //アプリデータ
    private static ArrayList<AppData> appDataList = new ArrayList<AppData>();

    //TODO: グローバル変数をまとめる(Application)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.cut_in_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //タイトル消去

        ImageView addCutInHolder = (ImageView)findViewById(R.id.addCutInHolder);
        addCutInHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddCutInHolder();
            }
        });

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                    //Binderからservice取得
                    mService = ((CutInService.ServiceBinder)service).getService();
                    Log.i("MainActivity", "onConnected");
                    isConnection = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                Log.i("MainActivity", "Disnnected");
                isConnection = false;
            }
        };

        //権限確認
        if (!checkOverlayPermission(this))
            requestOverlayPermission(this, OVERLAY_PERMISSION_REQUEST_CODE);

        //メインレイアウト取得
        frameList = (LinearLayout)findViewById(R.id.frameList);

        /* とりあえずのカットイン */
        cutInList.add(new CutIn(this, "None CutIn", R.drawable.ic_launcher_background));
        cutInList.add(new CutIn(this, "First CutIn", R.mipmap.ic_launcher));
        cutInList.add(new CutIn(this, "Second CutIn", R.mipmap.ic_launcher_round));
        cutInHolderList.add(new CutInHolder(EventType.SCREEN_ON, cutInList.get(0)));
        cutInHolderList.add(new CutInHolder(EventType.LOW_BATTERY, cutInList.get(0)));

        /* カットインホルダー表示 */
        setCutInHolderListDisplayReset();

        //TODO: キーフレームアニメーションの動作確認
        /* キーフレームアニメーションテスト */
        KeyFrameAnimation.MoveKeyFrameAnimation moveKeyFrameAnimation = new KeyFrameAnimation.MoveKeyFrameAnimation(60);
        moveKeyFrameAnimation.addKeyFrame(new KeyFrame.MoveKeyFrame(10, 100, 100, new AccelerateDecelerateInterpolator()));
        moveKeyFrameAnimation.addKeyFrame(new KeyFrame.MoveKeyFrame(40, 200, 50, new AccelerateInterpolator()));
        moveKeyFrameAnimation.makeKeyFrameAnimation();
        KeyFrame keyFrame;
        while((keyFrame = moveKeyFrameAnimation.nextFrame()) != null){
            Log.i(TAG, "" + ((KeyFrame.MoveKeyFrame)keyFrame).getX() + ", " + ((KeyFrame.MoveKeyFrame)keyFrame).getY());
        }
    }

    //カットインホルダーを表示
    private void cutInHolderListDisplay(){
        Log.i(TAG, "cutInHolderListDisplay");
        for (CutInHolder cih : cutInHolderList){
            frameList.addView(new FrameView(this, cih)
                    .setCutInName(cih.getCutIn().getTitle())
                    .setEventType(cih.getEventType())
                    .setThumbnail(cih.getCutIn().getThumbnail())
                    .setAppIcon(cih.getAppIcon()),
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                  ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    //再表示
    private void setCutInHolderListDisplayReset(){

        frameList.removeAllViews();
        cutInHolderListDisplay();
    }

    //カットインホルダー追加ボタン処理
    private void onClickAddCutInHolder(){
        //追加できるのはアプリ通知のみ
        //アプリ選択ウィンドウ開く
        AppDialog appDialog = new AppDialog();
        appDialog.showWithTask(getFragmentManager(), "appDialog", this, null);
    }

    //アプリダイアログのコールバック
    public void appDialogCallBack(AppData appData, CutInHolder cutInHolder){
        if(cutInHolder != null) {
            //アプリ情報変更後リセット
            cutInHolder.getAppData().setUsed(false);
            cutInHolder.setAppData(appData);
        }else{
            //ホルダー追加処理
            cutInHolderList.add(new CutInHolder(EventType.APP_NOTIFICATION, cutInList.get(0), appData));
        }
        appData.setUsed(true);
        setCutInHolderListDisplayReset();
    }
    //カットインサービス開始
    public void startCutInService(Context context){
        if (checkOverlayPermission(context) && !isConnection){
            Intent intent = new Intent(MainActivity.this, CutInService.class);
            startService(intent);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    //カットインサービス終了
    public void endCutInService(Context context){
        if(checkOverlayPermission(context) && isConnection){
            Intent intent = new Intent(MainActivity.this, CutInService.class);
            unbindService(serviceConnection);
            stopService(intent);
            isConnection = false;
        }
    }

    public static ArrayList<CutInHolder> getCutInHolderList() {
        return cutInHolderList;
    }

    public static ArrayList<AppData> getAppDataList() {
        return appDataList;
    }

    public static ArrayList<CutIn> getCutInList() {
        return cutInList;
    }

    //カットイン削除
    public static void removeCutIn(CutIn cutIn){
        cutInList.remove(cutIn);

        //同じカットインをホルダーリストから削除
        for (CutInHolder cih : cutInHolderList){
            if(cih.getCutIn().equals(cutIn)){
                cih.setCutIn(cutInList.get(0));
            }
        }
    }

    //メニュー生成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //メニュー選択時処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.request_overlay:
                requestOverlayPermission(this, OVERLAY_PERMISSION_REQUEST_CODE);
                return true;
            case R.id.request_notification:
                requestNotificationPermission(this, NOTIFICATION_PERMISSION_REQUEST_CODE);
                return true;
            case R.id.cut_in_enable:
                if(isConnection) endCutInService(this);
                else             startCutInService(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //再表示されたとき
    @Override
    protected void onResume() {
        super.onResume();
        if (!checkOverlayPermission(this)){
            Toast.makeText(this, "オーバーレイの権限がないと実行できません", Toast.LENGTH_SHORT).show();
        }
        if (!checkNotificationPermission(this)){
            Toast.makeText(this, "通知アクセス権限がないと実行できません", Toast.LENGTH_SHORT).show();
        }

        setCutInHolderListDisplayReset();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE){
            if(!checkOverlayPermission(this)){
                //オーバレイの権限がない場合
                Toast.makeText(this, "オーバーレイの権限がないと実行できません", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE)
            if(!checkNotificationPermission(this)){
                Toast.makeText(this, "通知アクセス権限がないと実行できません", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
