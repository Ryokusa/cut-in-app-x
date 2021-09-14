package com.ryokusasa.cut_in_app.CutIn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ryokusasa.cut_in_app.AppDataManager.AnimObj;
import com.ryokusasa.cut_in_app.CutInCanvas;

import java.io.Serializable;
import java.util.ArrayList;

//カットインクラス
/*
  アニメーション情報やタイトル情報や動かすビューを持つ
 */

/* TODO:終了時の消去処理 */
public class CutIn extends ConstraintLayout implements Cloneable, Serializable {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "CutIn";

    private Context context;
    private Drawable thumbnail;
    private String title;
    private ArrayList<AnimObj> animObjList;

    private boolean playing = false;

    //ワーニング避け
    public CutIn(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public CutIn(Context context, String title, int resource){
        super(context);
        //コンテキスト取得
        this.context = context;

        //サムネ設定
        this.thumbnail = ResourcesCompat.getDrawable(getResources(), resource, null);

        //タイトル設定
        this.title = title;

        //初期化
        setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundColor(Color.argb(0,0,0,0));
        setVisibility(View.INVISIBLE);

        //アニメオブジェリスト
        animObjList = new ArrayList<AnimObj>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getThumbnail() {
        return thumbnail;
    }

    public void play(CutInCanvas cutInCanvas){
        initAnim();
        stop();
        this.setVisibility(View.VISIBLE);
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
        this.setVisibility(View.INVISIBLE);
    }

    //アニメーション初期化
    public void initAnim(){
        for(AnimObj animObj : animObjList) {
            animObj.setFrame(0);
        }
    }

    //animObjを追加
    //TODO:animObjをViewとして使うか、画像を描画するかきめる
    public void addAnimObj(AnimObj animObj){
        animObjList.add(animObj);
//        this.addView(animObj.getObjView());
    }

    public ArrayList<AnimObj> getAnimObjList() {
        return animObjList;
    }

    public void setAnimObjList(ArrayList<AnimObj> animObjList) {
        this.animObjList = animObjList;
    }

    @Override
    public CutIn clone() {
        CutIn cutIn = null;

        /*ObjectクラスのcloneメソッドはCloneNotSupportedExceptionを投げる可能性があるので、try-catch文で記述(呼び出し元に投げても良い)*/
        try {
            cutIn=(CutIn)super.clone();
            cutIn.thumbnail = this.thumbnail.getConstantState().newDrawable();
            cutIn.animObjList = new ArrayList<>(this.animObjList);
            cutIn.title = this.title;
            cutIn.context = null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return cutIn;
    }
}
