package com.ryokusasa.w3033901.cut_in_app_2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;

//カットインクラス
/*
  アニメーション情報やタイトル情報や動かすビューを持つ
 */
public class CutIn extends ConstraintLayout implements Cloneable{
    private Context context;
    private Drawable thumbnail;
    private String title;
    //TODO AnimObj追加


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

    public void play(){
        //TODO 再生処理
    }
}
