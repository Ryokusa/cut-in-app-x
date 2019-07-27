package com.ryokusasa.w3033901.cut_in_app_2.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.ryokusasa.w3033901.cut_in_app_2.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by Ryokusasa on 2017/12/18.
 * アプリ選択ダイアログDialogFragment
 */

public class AppDialog extends DialogFragment{
    public static List<AppData> appDataList = new ArrayList<AppData>();
    public static boolean apploaded = false;   //アプリ情報を読み込んだか

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("アプリ選択");

        //アダプター作成
        AppDataAdapter adapter = new AppDataAdapter(builder.getContext(), 0, appDataList);

        //オンクリックイベント
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity activity = (MainActivity)getActivity();

                //コールバック
                activity.appDialogCallBack(appDataList.get(which), getArguments());
            }
        });

        return builder.create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(apploaded) {
            super.show(manager, tag);
        }else{
            LoadAppInfoTask loadAppInfoTask = new LoadAppInfoTask(getActivity());
            loadAppInfoTask.execute(0);
        }
    }
}


//appInfoを読み込むタスク
//progressと連動
class LoadAppInfoTask extends AsyncTask<Integer, Integer, Integer>{

    private Activity activity;
    private DialogFragment progressDialog = new ProgressDialog();

    public LoadAppInfoTask(Activity activity){
        //activity確保
        this.activity = activity;
    }

    @Override
    protected void onPreExecute(){
        //読み込む前にプログレスダイアログ表示
        progressDialog = new ProgressDialog();
        progressDialog.show(activity.getFragmentManager(), "progress");
    }

    @Override
    protected Integer doInBackground(Integer... args){
        //アプリ情報取得
        if(!AppDialog.apploaded) {    //読み込み済みの場合は無視
            PackageManager pm = activity.getPackageManager();
            List<ApplicationInfo> appInfoList = pm.getInstalledApplications(0);

            for (ApplicationInfo appInfo : appInfoList) {
                //プリインストールアプリは除外
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    AppDialog.appDataList.add(new AppData(appInfo.toString(), pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(appInfo)));
                }
            }
            AppDialog.apploaded = true;
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result){
        //プログレスダイアログ削除
        progressDialog.dismiss();

        //アプリ選択ダイアログ表示
        DialogFragment appDialog = new AppDialog();
        appDialog.show(activity.getFragmentManager(), "appDialog");
    }
}