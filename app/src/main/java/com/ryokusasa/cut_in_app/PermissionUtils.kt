package com.ryokusasa.cut_in_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

//パーミッションを使うクラス
//もし通知許可リクエストをしたいなら、Activity Result APIの仕様上グローバル変数として定義して使う
class PermissionUtils(private val mActivity: AppCompatActivity) {
    //インスタンス生成時にライフサイクル元(Activityなど)にActivity Resultを登録する
    private val overlayPermissionLauncher : ActivityResultLauncher<Intent> = mActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (!checkOverlayPermission(mActivity.applicationContext)) {
            //オーバレイの権限がない場合
            Toast.makeText(mActivity, "オーバーレイの権限がないと実行できません", Toast.LENGTH_SHORT).show()
        }
    } //オーバーレイ
    private val notificationPermissionLauncher : ActivityResultLauncher<Intent> = mActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (!checkNotificationPermission(mActivity.applicationContext)) {
            //オーバレイの権限がない場合
            Toast.makeText(mActivity, "通知アクセスの権限がありません", Toast.LENGTH_SHORT).show()
        }
    } //通知アクセス

    //オーバーレイリクエスト処理
    fun requestOverlayPermission() {
        //起動
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + mActivity.packageName)
        )
        overlayPermissionLauncher.launch(intent)
    }

    //通知アクセスリクエスト処理
    fun requestNotificationPermission() {
        //起動
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        notificationPermissionLauncher.launch(intent)
    }

    companion object {
        //静的命令
        //オーバーレイ権限チェック
        @JvmStatic
        fun checkOverlayPermission(context: Context?): Boolean {
            return Settings.canDrawOverlays(context) //APILevel23未満は常時ON
        }

        //通知アクセス権限チェック
        fun checkNotificationPermission(context: Context): Boolean {
            for (service in NotificationManagerCompat.getEnabledListenerPackages(context)) {
                if (service == context.packageName) return true
            }
            return false
        }
    }
}