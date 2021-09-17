package com.ryokusasa.cut_in_app.AnimObj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

//画像オブジェクト
public class TextObj extends AnimObj{
    private final String TAG = "TextObj";

    public String text;
    private final int textSize;  //変形後
    private final Paint mPaint;


    public TextObj(double x, double y, String text, int textSize){
        super(x, y);

        this.text = text;
        this.textSize = textSize;

        mPaint = new Paint();
    }

    public TextObj(Context context, String text, int textSize){
        this( 0, 0, text, textSize);
    }

    public void draw(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(textSize);
        canvas.drawText(text, (float)x, (float)y ,mPaint);
    }
}