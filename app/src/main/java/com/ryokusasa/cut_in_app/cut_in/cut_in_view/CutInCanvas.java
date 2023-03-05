package com.ryokusasa.cut_in_app.cut_in.cut_in_view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import java.util.Timer;
import java.util.TimerTask;

//カットインを描画するView
public class CutInCanvas extends View {
    private static final String TAG = "CutInCanvas";

    private DrawListener drawListener = (c) -> {};

    public CutInCanvas(Context context){
        super(context);

        Timer timer = new Timer();
        TimerTask cutInTimerTask = new CutInCanvasTimerTask(this);
        timer.scheduleAtFixedRate(cutInTimerTask, 0, 13);
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
