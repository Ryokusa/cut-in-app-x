package com.ryokusasa.cut_in_app.cut_in;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import android.util.Log;

import com.ryokusasa.cut_in_app.anim_obj.AnimObj;
import com.ryokusasa.cut_in_app.cut_in.cut_in_view.CutInCanvas;
import com.ryokusasa.cut_in_app.image_utils.ImageData;
import com.ryokusasa.cut_in_app.UtilCommon;

import java.util.ArrayList;

//カットインクラス
/*
  アニメーション情報やタイトル情報や動かすビューを持つ
 */
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

    public void setFrameNum(int frameNum){
        for (AnimObj animObj : animObjList){
            animObj.setFrameNum(frameNum);
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
