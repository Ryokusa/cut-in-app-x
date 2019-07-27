package com.ryokusasa.w3033901.cut_in_app_2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

//カットインクラス
/*
  アニメーション情報やタイトル情報や動かすビューを持つ
 */
public class CutIn extends ConstraintLayout implements Serializable {
    private Context context;
    private Drawable thumbnail;
    private String title;
    //TODO AnimObj追加

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
}
