package com.ryokusasa.cut_in_app.AppDataManager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

//画像オブジェクト
public class TextObj extends AnimObj{
    public String text;
    private int textSize;  //変形後
    private Paint mPaint;

    public TextObj(Context context, double x, double y, String text, int textSize){
        super(context, x, y);

        this.text = text;
        this.textSize = textSize;

        mPaint = new Paint();
    }

    public TextObj(Context context, String text, int textSize){
        this(context, 0, 0, text, textSize);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(textSize);
        canvas.drawText(text, (float)x, (float)y ,mPaint);
    }
}