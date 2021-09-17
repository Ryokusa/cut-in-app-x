package com.ryokusasa.cut_in_app.AppDataManager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;

//画像オブジェクト
public class ImageObj extends AnimObj{
    private Drawable drawable;
    private int width, height;  //変形後

    public ImageObj(Drawable drawable, double x, double y, int width, int height){
        super(x, y);

        this.drawable = drawable;
        this.width = width;
        this.height = height;
    }

    public ImageObj(Drawable drawable){
        this( drawable, 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public void draw(Canvas canvas) {
        this.drawable.setBounds((int)this.x, (int)this.y,(int)this.x+this.width, (int)this.y+this.height);
        this.drawable.draw(canvas);
    }
}