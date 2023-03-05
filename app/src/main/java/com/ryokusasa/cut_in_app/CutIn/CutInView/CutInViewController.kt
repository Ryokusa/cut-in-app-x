package com.ryokusasa.cut_in_app.CutIn.CutInView

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService
import com.ryokusasa.cut_in_app.*
import com.ryokusasa.cut_in_app.CutIn.CutInView.CutInService.ServiceBinder
import com.ryokusasa.cut_in_app.PermissionUtils.Companion.checkOverlayPermission


/** カットインコントローラー */
class CutInViewController(private val context: Context) {
    companion object {
        const val TAG = "CutInController"
    }

    private lateinit var cutInViewLayout: ConstraintLayout
    private lateinit var cutInViewLayoutParams: LayoutParams
    val cutInCanvas = CutInCanvas(context)
    private val windowManager = getSystemService(context, WindowManager::class.java)
    private lateinit var serviceConnection: ServiceConnection
    private var mService: Service? = null
    var isConnected = false


    init{
        makeCutInViewLayout()
        connectService()
    }

    fun startCutInViewService(context: Context) {
        if (checkOverlayPermission(context) && !isConnected) {
            val intent = Intent(context, CutInService::class.java)
            addCutInView()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(context, intent)
            } else {
                context.startService(intent)
            }
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    //カットインサービス終了
    fun endCutInViewService(context: Context) {
        if (checkOverlayPermission(context) && isConnected) {
            val intent = Intent(context, CutInService::class.java)
            context.unbindService(serviceConnection)
            context.stopService(intent)
            isConnected = false
            removeCutInView()
        }
    }

    private fun makeCutInViewLayout(){
        cutInViewLayout = ConstraintLayout(context)

        //レイアウト読み込む用
        val layoutInflater = LayoutInflater.from(context)

        //重ね合わせするViewの設定
        cutInViewLayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )

        //Viewを作成
        layoutInflater.inflate(R.layout.filter, cutInViewLayout)
        val layout = LinearLayout(context)
        layout.layoutParams = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layout.setBackgroundColor(Color.argb(0, 0, 0, 0))

        cutInViewLayout.addView(
            cutInCanvas,
            ConstraintLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun addCutInView() {
        //重ね合わせる
        windowManager?.addView(cutInViewLayout, cutInViewLayoutParams)
    }
    private fun removeCutInView() {
        windowManager?.removeView(cutInViewLayout)
    }

    private fun connectService() {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //Binderからservice取得
                mService = (service as ServiceBinder).service
                Log.i(TAG, "onConnected")
                isConnected = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mService = null
                Log.i("TAG", "Disconnected")
                isConnected = false
            }
        }
    }

    //ディスプレイ情報取得
    fun getDisplayInfo(): DisplayInfo? {
        return DisplayInfo(cutInCanvas.width, cutInCanvas.height)
    }
}

class DisplayInfo(private val width: Int, private val height: Int) {
    private val widthDp: Float = px2dp(width)
    private val heightDp: Float = px2dp(height)
}

fun px2dp(px: Int): Float {
    val metrics = UtilCommon.getInstance().resources.displayMetrics
    return px / metrics.density
}

fun dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        UtilCommon.getInstance().resources.displayMetrics
    ).toInt()
        .toFloat()
}