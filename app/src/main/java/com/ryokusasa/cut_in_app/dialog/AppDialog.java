package com.ryokusasa.cut_in_app.dialog;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ryokusasa.cut_in_app.cut_in.CutInHolder;
import com.ryokusasa.cut_in_app.activity.main.MainActivity;
import com.ryokusasa.cut_in_app.UtilCommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Ryokusasa on 2017/12/18.
 * アプリ選択ダイアログDialogFragment
 *
 * Bundle渡してあげればいろんなやつ渡せる
 */

public class AppDialog extends DialogFragment{

    CutInHolder cutInHolder;
    UtilCommon utilCommon;

    public AppDialog(){
        utilCommon = UtilCommon.getInstance();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("アプリ選択\n");
        final MainActivity activity = (MainActivity) getActivity();

        //アダプター作成
        AppDataAdapter adapter = new AppDataAdapter(builder.getContext(), 0, utilCommon.appDataList);

        //オンクリックイベント
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //コールバック
                activity.appDialogCallBack(utilCommon.appDataList.get(which), cutInHolder);
            }
        });

        return builder.create();
    }

    //アクティビティとアプリデータリストを作成確認しながら表示
    public void showWithTask(FragmentManager manager, String tag, AppCompatActivity activity, CutInHolder cutInHolder) {
        this.cutInHolder = cutInHolder;

        if(!UtilCommon.getInstance().appDataList.isEmpty()) {
            super.show(manager, tag);
        }else{
            LoadAppInfoTask loadAppInfoTask = new LoadAppInfoTask(activity, this, utilCommon.appDataList);
            loadAppInfoTask.execute(0);
        }
    }
}


//appInfoを読み込むタスク
//progressと連動
//Android11から非推奨だけど現状使えるのでこのまま
class LoadAppInfoTask extends AsyncTask<Integer, Integer, Integer>{

    private final AppCompatActivity activity;
    private DialogFragment progressDialog = new ProgressDialog();
    private final AppDialog appDialog;
    private final ArrayList<AppData> appDataList;

    public LoadAppInfoTask(AppCompatActivity activity, AppDialog appDialog, ArrayList<AppData> appDataList){
        //activity確保
        this.activity = activity;
        this.appDialog = appDialog;
        this.appDataList = appDataList;
    }

    @Override
    protected void onPreExecute(){
        //読み込む前にプログレスダイアログ表示
        progressDialog = new ProgressDialog();
        progressDialog.show(activity.getSupportFragmentManager(), "progress");
    }

    @Override
    protected Integer doInBackground(Integer... args){
        //アプリ情報取得
        if(appDataList.isEmpty()) {    //読み込み済みの場合は無視
            PackageManager pm = activity.getPackageManager();
            List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.PERMISSION_GRANTED);

            for (PackageInfo packageInfo : packageInfoList){
                if (packageInfo.requestedPermissions != null) {
                    if ( Arrays.asList(packageInfo.requestedPermissions).contains("android.permission.POST_NOTIFICATIONS")) {
                        ApplicationInfo appInfo = packageInfo.applicationInfo;
                        appDataList.add(new AppData(appInfo.packageName, appInfo.loadLabel(pm).toString(), pm.getApplicationIcon(appInfo)));
                    }
                }
            }
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result){
        //プログレスダイアログ削除
        progressDialog.dismiss();

        //アプリ選択ダイアログ表示
        appDialog.show(activity.getSupportFragmentManager(), "appDialog");
    }
}