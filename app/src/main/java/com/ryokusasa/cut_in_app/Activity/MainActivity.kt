package com.ryokusasa.cut_in_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ryokusasa.cut_in_app.Animation.KeyFrame;
import com.ryokusasa.cut_in_app.Animation.KeyFrameAnimation;
import com.ryokusasa.cut_in_app.CutIn.CutIn;
import com.ryokusasa.cut_in_app.CutIn.CutInHolder;
import com.ryokusasa.cut_in_app.Dialog.AppData;
import com.ryokusasa.cut_in_app.Dialog.AppDialog;
import com.ryokusasa.cut_in_app.EventType;
import com.ryokusasa.cut_in_app.FrameView;
import com.ryokusasa.cut_in_app.PermissionUtils;
import com.ryokusasa.cut_in_app.R;
import com.ryokusasa.cut_in_app.UtilCommon;

import java.util.ArrayList;
import java.util.Objects;

import static com.ryokusasa.cut_in_app.PermissionUtils.*;

/*
  メインアクティビティ
  by Ryokusasa
 */


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";   //Log

    //レイアウト
    private LinearLayout frameList;

    //グローバルクラス
    private UtilCommon utilCommon;

    //パーミッション申請用
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.cut_in_toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);    //タイトル消去

        utilCommon = (UtilCommon)getApplication();
        permissionUtils = new PermissionUtils(this);

        //カットインホルダー追加ボタン
        ImageView addCutInHolder = findViewById(R.id.addCutInHolder);
        addCutInHolder.setOnClickListener(view -> onClickAddCutInHolder());

        //サービス接続
        utilCommon.connectService();

        //権限確認
        if (!checkOverlayPermission(this))
            permissionUtils.requestOverlayPermission();

        //メインレイアウト取得
        frameList = findViewById(R.id.frameList);

        /* カットインホルダー表示 */
        setCutInHolderListDisplayReset();

        /* キーフレームアニメーションテスト */
        KeyFrameAnimation.MoveKeyFrameAnimation moveKeyFrameAnimation = new KeyFrameAnimation.MoveKeyFrameAnimation(60);
        moveKeyFrameAnimation.addKeyFrame(new KeyFrame.MoveKeyFrame(10, 100, 100, new AccelerateDecelerateInterpolator()));
        moveKeyFrameAnimation.addKeyFrame(new KeyFrame.MoveKeyFrame(40, 200, 50, new AccelerateInterpolator()));
        moveKeyFrameAnimation.makeKeyFrameAnimation();
        KeyFrame keyFrame;
        int i = 0;
        while((keyFrame = moveKeyFrameAnimation.nextFrame()) != null){
            Log.i(TAG, "" + i + ", " + keyFrame.values.get("x") + ", " + keyFrame.values.get("y"));
            i++;
        }

        //テスト
        String[] files = this.fileList();
        for (String file : files)Log.i(TAG, file);

    }

    //カットインホルダーを表示
    private void cutInHolderListDisplay(){
        Log.i(TAG, "cutInHolderListDisplay");
        for (CutInHolder cutInHolder : utilCommon.cutInHolderList){
            frameList.addView(
                    new FrameView(this, cutInHolder),
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            );
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
        appDialog.showWithTask(getSupportFragmentManager(), "appDialog", this, null);
    }

    //アプリダイアログのコールバック
    public void appDialogCallBack(AppData appData, CutInHolder cutInHolder) {
        if (cutInHolder != null) {
            //アプリ情報変更後リセット
            cutInHolder.getAppData().setUsed(false);
            cutInHolder.setAppData(appData);
        } else {
            //ホルダー追加処理
            utilCommon.cutInHolderList.add(new CutInHolder(EventType.APP_NOTIFICATION, utilCommon.initialCutIn, appData));
        }
        appData.setUsed(true);
        setCutInHolderListDisplayReset();
    }

    //メニュー生成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        if (!UtilCommon.DEBUG) {
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(false);
        }
        return true;
    }

    //メニュー選択時処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* 余談だがswitchにすると構造上再コンパイルが必要になり、重くなるので非推奨とされる */
        /* url:http://tools.android.com/tips/non-constant-fields */
        int itemId = item.getItemId();
        if (itemId == R.id.request_overlay) {
            permissionUtils.requestOverlayPermission();
            return true;
        } else if (itemId == R.id.request_notification) {
            permissionUtils.requestNotificationPermission();
            return true;
        } else if (itemId == R.id.cut_in_enable) {
            if (utilCommon.isConnection) utilCommon.endCutInService(this);
            else utilCommon.startCutInService(this);
            return true;
        }else if (itemId == R.id.save_menu_icon){
            utilCommon.save();
            return true;
        }else if (itemId == R.id.load_menu_item){
            utilCommon.load();
            setCutInHolderListDisplayReset();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
