package com.ryokusasa.w3033901.cut_in_app_2.CutIn;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ryokusasa.w3033901.cut_in_app_2.AppDataManager.AnimObj;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

//カットインクラス
/*
  アニメーション情報やタイトル情報や動かすビューを持つ
 */
public class CutIn extends ConstraintLayout implements Cloneable, Serializable {
    private Context context;
    private Drawable thumbnail;
    private String title;
    private ArrayList<AnimObj> animObjList;

    //ワーニング避け
    public CutIn(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public CutIn(Context context, String title, int resource){
        super(context);
        //コンテキスト取得
        this.context = context;

        //サムネ設定
        this.thumbnail = getResources().getDrawable(resource);

        //タイトル設定
        this.title = title;

        //初期化
        setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundColor(Color.argb(0,0,0,0));
        setVisibility(View.INVISIBLE);

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

    public void play(){
        //TODO 再生処理

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
