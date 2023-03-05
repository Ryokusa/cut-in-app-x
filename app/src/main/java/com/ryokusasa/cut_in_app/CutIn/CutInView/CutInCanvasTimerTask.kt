package com.ryokusasa.cut_in_app.CutIn.CutInView

import android.os.Handler
import android.os.Looper
import android.view.View
import java.util.TimerTask

class CutInCanvasTimerTask(private val v: View) : TimerTask() {
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun run() {
        handler.post {
            v.postInvalidate()
        }
    }
}