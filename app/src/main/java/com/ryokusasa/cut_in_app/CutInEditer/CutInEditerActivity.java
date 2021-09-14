package com.ryokusasa.cut_in_app.CutInEditer;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ryokusasa.cut_in_app.AppDataManager.AnimObj;
import com.ryokusasa.cut_in_app.AppDataManager.ImageObj;
import com.ryokusasa.cut_in_app.AppDataManager.TextObj;
import com.ryokusasa.cut_in_app.CutIn.CutIn;
import com.ryokusasa.cut_in_app.R;
import com.ryokusasa.cut_in_app.UtilCommon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ryokusasa on 2018/01/22.
 *
 * //カットインエディターアクティビティ
 *
 */

public class CutInEditerActivity extends AppCompatActivity {
    private static final String TAG = "CutInEditerActivity";
//    private static final int REQUEST_ADD_CHOOSER = 3149;
//    private static final int REQUEST_IMAGE_CHOOSER = 1242;
//
//    EditerView editerView;  //エディタービュー
//
//    private UtilCommon utilCommon;
//
//    //場面フラグ
//    public static int sceneFlag = 0;
//    public static final int CUTIN_INIT = 0;   //初期値設定画面
//    public static final int LAYER_EDIT = 1;   //レイヤー編集画面
//
//    //レイヤーウィンドウ関連
//    private ObjectAnimator enterAnimator;
//    private ObjectAnimator exitAnimator;
//    private LayerAdapter adapter;
//
//    //オブジェクトウィンドウ関連
//    private final int makeId = -1;    //前回参照したオブジェクト番号
//
//    private CutIn cutIn;    //編集用カットイン
//    private int selCutInId; //編集しているカットイン番号(-1は新規?)
//
//    //タイマー関連
//    Handler handler;
//    Timer timer;
//    EditerTimerTask timerTask;
//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cut_in_editer_layout);
//
//        utilCommon = (UtilCommon)getApplication();
//
//        //ハンドラー取得
//        handler = new Handler();
//
//        //カットイン取得
//        Bundle bundle = getIntent().getExtras();
//        if((selCutInId = bundle.getInt("selCutInId")) != -1) {
//            //カットインをコピー
//            cutIn = utilCommon.cutInList.get(selCutInId).clone();
//        }else{
//            //新規作成
//            cutIn = new CutIn(getBaseContext(), "non title", R.drawable.ic_launcher_background);
//        }
//
//        //エディタービュー作成
//        editerView = new EditerView(this, cutIn.getAnimObjList());
//        final ConstraintLayout layout = findViewById(R.id.editerLayout);
//        layout.addView(editerView, 0);  //エディタービューを最下層に追加
//        editerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //画面遷移
//                changeScene(CUTIN_INIT);
//            }
//        });
//
//        //レイヤーウィンドウ初期位置へ
//        RelativeLayout layerWindow = findViewById(R.id.layerMenu);
//        layerWindow.setTranslationY(50);
//        layerWindow.setTranslationX(editerView.screenWidth);
//
//        //レイヤーウィンドウボタン
//        ImageView layerEditButton = findViewById(R.id.layerEditButton);
//        layerEditButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //遷移
//                changeScene(LAYER_EDIT);
//            }
//        });
//
//        //レイヤーウィンドウ閉じボタン
//        final Button closeButton = findViewById(R.id.closeButton);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeScene(CUTIN_INIT);
//            }
//        });
//
//        //レイヤーリストにImageObject追加
//        ListView layerListView = findViewById(R.id.layerListView);
//        adapter = new LayerAdapter(this, 0, editerView.getAnimObjList(), editerView.getLayerName(), editerView.getSelObjId());
//        layerListView.setAdapter(adapter);
//        layerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //オブジェクト選択
//                if(position == editerView.getSelObjId()) {
//                    //選択オブジェクトが再度選択された場合は選択解除
//                    editerView.setSelObjId(-1);
//                }else{
//                    editerView.setSelObjId(position);
//                }
//
//                //リスト更新
//                adapter.setSelId(editerView.getSelObjId());
//                adapter.notifyDataSetChanged();
//            }
//        });
//
//        //レイヤー追加ボタン
//        ImageView addButton = findViewById(R.id.addButton);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //追加ウィンドウ表示
//                showAddWindow();
//            }
//        });
//
//        //レイヤー削除ボタン
//        ImageView deleteButton = findViewById(R.id.deleteButton);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //削除処理
//                if(editerView.getSelObjId() != -1) {
//                    editerView.deleteAnimObject(editerView.getSelObjId());
//                    editerView.setSelObjId(-1);  //未選択
//                    adapter.setSelId(-1);
//                    adapter.notifyDataSetChanged(); //リスト更新
//                }
//            }
//        });
//
//        //再生ボタン
//        ImageView playButton = findViewById(R.id.playButton);
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //再生処理
//                playCutIn();
//            }
//        });
//
//
//        //タイマー設定
//        timer = new Timer();
//        timerTask = new EditerTimerTask();
//        timer.schedule(timerTask, 0, 17);
    }
//
//    //レイヤーウィンドウ表示
//    private void showLayerWindow(){
//        //アニメーション開始
//        RelativeLayout layerWindow = findViewById(R.id.layerMenu);
//        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.setting_enter_animation);
//        animator.setTarget(layerWindow);
//        animator.start();
//
//        //リスト更新
//        adapter.setSelId(editerView.getSelObjId());
//        adapter.notifyDataSetChanged();
//    }
//
//    //レイヤーウィンドウ非表示
//    private void closeLayerWindow(){
//        //アニメーション開始
//        RelativeLayout layerWindow = findViewById(R.id.layerMenu);
//        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.setting_exit_animation);
//        animator.setTarget(layerWindow);
//        animator.start();
//    }
//
//    //追加ウィンドウ表示
//    private void showAddWindow(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("オブジェクト選択");
//        builder.setItems(R.array.object_type, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case 0:
//                        Intent intentGallery;
//                        if (Build.VERSION.SDK_INT < 19) {
//                            intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
//                        } else {
//                            intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                            intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
//                        }
//                        intentGallery.setType("image/*");
//
//                        startActivityForResult(intentGallery, REQUEST_ADD_CHOOSER);
//                        break;
//                    case 1:
//                        //テキストオブジェクト追加
//                        editerView.addAnimObject(new TextObj(CutInEditerActivity.this, "テキスト", 18));
//                        adapter.notifyDataSetChanged();
//                        changeScene(CUTIN_INIT);
//                        break;
//                }
//            }
//        });
//
//        builder.create().show();
//    }
//
//    //画像追加処理
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == REQUEST_ADD_CHOOSER && resultCode == RESULT_OK){
//            //オブジェクト追加処理
//            Uri resultUri = data.getData();
//            Bitmap bitmap;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
//            }catch(IOException e){
//                e.printStackTrace();
//                return;
//            }
//            editerView.addAnimObject(new ImageObj(this, new BitmapDrawable(this.getResources(), bitmap)));
//            editerView.setSelObjId(cutIn.getAnimObjList().size()); //オブジェクト選択
//            adapter.notifyDataSetChanged(); //リスト更新
//
//            //設定ウィンドウ閉じ
//            changeScene(0);
//        }else if(requestCode == REQUEST_IMAGE_CHOOSER && resultCode == RESULT_OK){
//            ImageView layerImage = findViewById(R.id.layerImage);
//
//            //ビットマップ生成
//            Uri resultUri = data.getData();
//            Bitmap bitmap;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
//            }catch(IOException e){
//                e.printStackTrace();
//                return;
//            }
//
//            //ビットマップはタグ付け
//            layerImage.setTag(bitmap);
//
//            //イメージ適用
//            layerImage.setImageDrawable(new BitmapDrawable(this.getResources(), bitmap));
//            layerImage.invalidate();
//        }
//
//    }
//
//    //カットイン生成
//    private void makeCutIn(){
//        //TODO タイトルサムネイル等格納
//        cutIn.setAnimObjList(new ArrayList<AnimObj>(editerView.getAnimObjList()));
//    }
//
//    //カットイン再生
//    private void playCutIn(){
//        makeCutIn();
//        //TODO CutInService.play(cutIn);
//    }
//
//    //タイマークラス
//    private class EditerTimerTask extends TimerTask{
//
//        @Override
//        public void run() {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    //タイマー
//                    //エディタービューを再描画
//                    editerView.invalidate();
//
//                    //選択オブジェクトがある場合はオブジェクト編集ボタン表示
//                    if(editerView.getSelObjId() == -1){
//                        findViewById(R.id.objEditButton).setAlpha(0.5f);
//                    }else{
//                        findViewById(R.id.objEditButton).setAlpha(1.0f);
//                    }
//                }
//            });
//        }
//    }
//
//    //場面変更処理
//    private void changeScene(int flag){
//        if(flag == CUTIN_INIT){ //初期値設定画面へ
//            switch (sceneFlag){ //現在のフラグで動作わけ
//                case LAYER_EDIT:
//                    closeLayerWindow();
//                    break;
//                default:
//                    return;
//            }
//        }else if(flag == LAYER_EDIT){   //レイヤー編集画面へ
//            switch (sceneFlag){
//                case CUTIN_INIT:
//                    showLayerWindow();
//                    break;
//                default:
//                    return;
//            }
//        }
//        sceneFlag = flag;
//    }
//
//    //キーダウン
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode != KeyEvent.KEYCODE_BACK) {
//            return super.onKeyDown(keyCode, event);
//        }else{
//            //たとえばアニメ編集から戻るところとか
//            if(sceneFlag == CUTIN_INIT){
//                //保存確認
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("注意");
//                builder.setMessage("保存しますか？");
//                builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //保存処理
//                        makeCutIn();
//                        if (selCutInId != -1) {
//                            //既成のカットインを編集している場合代入
//                            utilCommon.cutInList.set(selCutInId, cutIn.clone());  //カットイン追加
//                        }else{
//                            //新規カットインの場合追加
//                            utilCommon.cutInList.add(cutIn.clone());
//                        }
//                        //TODO new CutInDataManager(CutInEditerActivity.this).cutInListSave(new ArrayList<CutIn>(CutInService.cutInList));
//                        finish();
//                    }
//                });
//                builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //何もせず終了
//                        finish();
//                    }
//                });
//                builder.create().show();
//            }else if(sceneFlag == LAYER_EDIT){
//                changeScene(CUTIN_INIT);
//            }
//            return false;
//        }
//    }

}