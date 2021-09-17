package com.ryokusasa.cut_in_app.CutIn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ryokusasa.cut_in_app.AnimObj.AnimObj;
import com.ryokusasa.cut_in_app.CutInCanvas;
import com.ryokusasa.cut_in_app.ImageUtils.ImageData;
import com.ryokusasa.cut_in_app.UtilCommon;

import java.io.Serializable;
import java.util.ArrayList;

//カットインクラス
/*
  アニメーション情報やタイトル情報や動かすビューを持つ
 */

/* TODO:終了時の消去処理 */
/* TODO: 保存のために画像をファイルパスで指定*/
public class CutIn{
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = "CutIn";

    //カットインのバージョン(カットイン読み込む際、整合性を保つため)
    public static final int CUT_IN_VERSION = 1;

    public ImageData imageData;
    public String title;
    private ArrayList<AnimObj> animObjList;

    private boolean playing = false;

    public CutIn(String title, ImageData imageData){

        //サムネ設定
        this.imageData = imageData;

        //タイトル設定
        this.title = title;

        //アニメオブジェリスト
        animObjList = new ArrayList<AnimObj>();
    }

    public Drawable getThumbnail() {
        return  UtilCommon.getImageUtils().getDrawable(imageData);
    }

    public void play(CutInCanvas cutInCanvas){
        initAnim();
        stop();
        playing = true;
        cutInCanvas.setDrawListener(new CutInCanvas.DrawListener() {
            @Override
            public void onDraw(Canvas canvas) {
                if(playing) _play(canvas);
            }
        });

        Log.i(TAG, "play");
    }

    //繰り返し
    private void _play(Canvas canvas){
        Log.i(TAG,"_play");
        boolean f = true;  //終了フラグ
        for(AnimObj animObj : animObjList) {
            if(!animObj.playFrame(canvas)) f = false; //再生中が一つでもあるなら継続
        }
        if(f) stop();
    }

    //停止
    public void stop(){
        playing = false;
    }

    //アニメーション初期化
    public void initAnim(){
        for(AnimObj animObj : animObjList) {
            animObj.setFrame(0);
        }
    }

    //animObjを追加
    public void addAnimObj(AnimObj animObj){
        animObjList.add(animObj);
    }

    public ArrayList<AnimObj> getAnimObjList() {
        return animObjList;
    }

    public void setAnimObjList(ArrayList<AnimObj> animObjList) {
        this.animObjList = animObjList;
    }
}
