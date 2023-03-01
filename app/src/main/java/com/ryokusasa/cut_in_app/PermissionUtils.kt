package com.ryokusasa.cut_in_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;

//パーミッションを使うクラス
//もし通知許可リクエストをしたいなら、Activity Result APIの仕様上グローバル変数として定義して使う
public class PermissionUtils {
    private final ActivityResultLauncher<Intent> OPLauncher;  //オーバーレイ
    private final ActivityResultLauncher<Intent> NPLauncher;  //通知アクセス
    private final AppCompatActivity mActivity;

    public PermissionUtils(AppCompatActivity activity){
        mActivity = activity;
        //インスタンス生成時にライフサイクル元(Activityなど)にActivity Resultを登録する
        OPLauncher = mActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(!checkOverlayPermission(activity.getApplicationContext())){
                //オーバレイの権限がない場合
                Toast.makeText(mActivity, "オーバーレイの権限がないと実行できません", Toast.LENGTH_SHORT).show();
            }
        });
        NPLauncher = mActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(!checkNotificationPermission(activity.getApplicationContext())){
                //オーバレイの権限がない場合
                Toast.makeText(mActivity, "通知アクセスの権限がありません", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //オーバーレイリクエスト処理
    public void requestOverlayPermission() {
        //起動
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mActivity.getPackageName()));
            OPLauncher.launch(intent);
        }
    }

    //通知アクセスリクエスト処理
    public void requestNotificationPermission(){
        //起動
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            NPLauncher.launch(intent);
        }
    }


    //静的命令
    //オーバーレイ権限チェック
    public static boolean checkOverlayPermission(Context context){
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(context); //APILevel23未満は常時ON
    }

    //通知アクセス権限チェック
    public static boolean checkNotificationPermission(Context context){
        for (String service : NotificationManagerCompat.getEnabledListenerPackages(context)) {
            if (service.equals(context.getPackageName()))
                return true;
        }
        return false;
    }

}
