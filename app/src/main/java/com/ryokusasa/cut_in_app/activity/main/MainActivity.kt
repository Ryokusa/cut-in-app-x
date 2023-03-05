package com.ryokusasa.cut_in_app.activity.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ryokusasa.cut_in_app.*
import com.ryokusasa.cut_in_app.animation.KeyFrame
import com.ryokusasa.cut_in_app.animation.KeyFrame.MoveKeyFrame
import com.ryokusasa.cut_in_app.animation.KeyFrameAnimation.MoveKeyFrameAnimation
import com.ryokusasa.cut_in_app.cut_in.CutInHolder
import com.ryokusasa.cut_in_app.dialog.AppData
import com.ryokusasa.cut_in_app.dialog.AppDialog

/*
  メインアクティビティ
  by Ryokusasa
 */
class MainActivity : AppCompatActivity() {

    private lateinit var frameList: LinearLayout //レイアウト
    private lateinit var utilCommon: UtilCommon //グローバルクラス
    private lateinit var permissionUtils: PermissionUtils //パーミッション申請用
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        utilCommon = application as UtilCommon
        permissionUtils = PermissionUtils(this)



        initComponent()

        //権限確認
        if (!PermissionUtils.checkOverlayPermission(this)) permissionUtils.requestOverlayPermission()

        /* キーフレームアニメーションテスト */
        val moveKeyFrameAnimation = MoveKeyFrameAnimation(60)
        moveKeyFrameAnimation.addKeyFrame(
            MoveKeyFrame(
                10,
                100.0,
                100.0,
                AccelerateDecelerateInterpolator()
            )
        )
        moveKeyFrameAnimation.addKeyFrame(MoveKeyFrame(40, 200.0, 50.0, AccelerateInterpolator()))
        moveKeyFrameAnimation.makeKeyFrameAnimation()
        var keyFrame: KeyFrame? = moveKeyFrameAnimation.nextFrame()
        var i = 0
        while (keyFrame != null) {
            Log.i(TAG, "" + i + ", " + keyFrame.values["x"] + ", " + keyFrame.values["y"])
            i++
            keyFrame = moveKeyFrameAnimation.nextFrame()
        }
    }

    //部品初期化
    private fun initComponent() {
        setSupportActionBar(findViewById(R.id.cut_in_toolbar))

        //アクションバー
        supportActionBar?.setDisplayShowTitleEnabled(false) //タイトル消去

        //カットインホルダー追加ボタン
        val addCutInHolder = findViewById<ImageView>(R.id.addCutInHolder)
        addCutInHolder.setOnClickListener { onClickAddCutInHolder() }

        //メインレイアウト取得
        frameList = findViewById(R.id.frameList)

        /* カットインホルダー表示 */
        setCutInHolderListDisplayReset()
    }

    //カットインホルダーを表示
    private fun cutInHolderListDisplay() {
        Log.i(TAG, "cutInHolderListDisplay")
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        for (cutInHolder in utilCommon.cutInHolderList) {
            frameList.addView(
                FrameView(
                    this,
                    cutInHolder
                ),
                layoutParams
            )
        }
    }

    //再表示
    private fun setCutInHolderListDisplayReset() {
        frameList.removeAllViews()
        cutInHolderListDisplay()
    }

    //カットインホルダー追加ボタン処理
    private fun onClickAddCutInHolder() {
        //追加できるのはアプリ通知のみ
        //アプリ選択ウィンドウ開く
        val appDialog = AppDialog()
        appDialog.showWithTask(supportFragmentManager, "appDialog", this, null)
    }

    //アプリダイアログのコールバック
    fun appDialogCallBack(appData: AppData, cutInHolder: CutInHolder?) {
        if (cutInHolder != null) {
            //アプリ情報変更後リセット
            cutInHolder.appData.isUsed = false
            cutInHolder.appData = appData
        } else {
            //ホルダー追加処理
            utilCommon.cutInHolderList.add(
                CutInHolder(
                    EventType.APP_NOTIFICATION,
                    utilCommon.initialCutIn,
                    appData
                )
            )
        }
        appData.isUsed = true
        setCutInHolderListDisplayReset()
    }

    //メニュー生成
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        if (!UtilCommon.DEBUG) {
            menu.getItem(3).isVisible = false
            menu.getItem(4).isVisible = false
        }
        return true
    }

    //メニュー選択時処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /* 余談だがswitchにすると構造上再コンパイルが必要になり、重くなるので非推奨とされる */
        /* url:http://tools.android.com/tips/non-constant-fields */
        when (item.itemId) {
            R.id.request_overlay -> {
                permissionUtils.requestOverlayPermission()
                return true
            }
            R.id.request_notification -> {
                permissionUtils.requestNotificationPermission()
                return true
            }
            R.id.cut_in_enable -> {
                if (utilCommon.cutInViewController.isConnected) utilCommon.cutInViewController.endCutInViewService(this)
                else utilCommon.cutInViewController.startCutInViewService(this)
                return true
            }
            R.id.save_menu_icon -> {
                utilCommon.save()
                return true
            }
            R.id.load_menu_item -> {
                utilCommon.load()
                setCutInHolderListDisplayReset()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    //再表示されたとき
    override fun onResume() {
        super.onResume()
        if (!PermissionUtils.checkOverlayPermission(this)) {
            Toast.makeText(this, "オーバーレイの権限がないと実行できません", Toast.LENGTH_SHORT).show()
        }
        if (!PermissionUtils.checkNotificationPermission(this)) {
            Toast.makeText(this, "通知アクセス権限がないと実行できません", Toast.LENGTH_SHORT).show()
        }
        setCutInHolderListDisplayReset()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MainActivity" //Log
    }
}