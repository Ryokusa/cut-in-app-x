package com.ryokusasa.cut_in_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;

//カットインを描画するView
public class CutInCanvas extends View {
    private static final String TAG = "CutInCanvas";

    private Handler handler;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
            handler.postDelayed(this, 13);
        }
    };
    private DrawListener drawListener = (c) -> {};

    public CutInCanvas(Context context){
        super(context);
        handler = new Handler();
        handler.post(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawListener.onDraw(canvas);
        //Log.i(TAG, "onDraw");
    }

    public void setDrawListener(DrawListener drawListener){
        this.drawListener = drawListener;
    }

    public interface DrawListener{
        void onDraw(Canvas canvas);
    }
}
