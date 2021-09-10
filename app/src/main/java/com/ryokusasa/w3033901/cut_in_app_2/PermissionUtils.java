package com.ryokusasa.w3033901.cut_in_app_2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionUtils {

    //オーバーレイ権限チェック
    public static Boolean checkOverlayPermission(Context context){
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(context); //APILevel23未満は常時ON
    }

    //オーバーレイリクエスト処理
    public static void requestOverlayPermission(AppCompatActivity activity, int requestCode) {
        //なんかワーニング出てるけどcheckOverlayPermissionで23以下は排除済み
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, requestCode);
    }

    //通知アクセス権限チェック
    public static boolean checkNotificationPermission(Context context){
        if (Build.VERSION.SDK_INT >= 18) {
            for (String service : NotificationManagerCompat.getEnabledListenerPackages(context)) {
                if (service.equals(context.getPackageName()))
                    return true;
            }
            return false;
        }
        return true;
    }

    //通知アクセスリクエスト処理
    public static void requestNotificationPermission(AppCompatActivity activity, int requestCode){
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        activity.startActivityForResult(intent, requestCode);
    }
}
