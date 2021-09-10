package com.ryokusasa.w3033901.cut_in_app_2.Dialog;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutInHolder;
import com.ryokusasa.w3033901.cut_in_app_2.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryokusasa on 2017/12/18.
 * アプリ選択ダイアログDialogFragment
 *
 * Bundle渡してあげればいろんなやつ渡せる
 */

public class AppDialog extends DialogFragment{

    CutInHolder cutInHolder;

    //TODO: カットインリストをutilから参照できるように
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("アプリ選択\n");
        final MainActivity activity = (MainActivity) getActivity();

        //アダプター作成
        AppDataAdapter adapter = new AppDataAdapter(builder.getContext(), 0, activity.getAppDataList());

        //オンクリックイベント
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //コールバック
                activity.appDialogCallBack(activity.getAppDataList().get(which), cutInHolder);
            }
        });

        return builder.create();
    }

    //アクティビティとアプリデータリストを作成確認しながら表示
    public void showWithTask(FragmentManager manager, String tag, AppCompatActivity activity, CutInHolder cutInHolder) {
        ArrayList<AppData> appDataList = ((MainActivity)activity).getAppDataList();
        this.cutInHolder = cutInHolder;

        if(!appDataList.isEmpty()) {
            super.show(manager, tag);
        }else{
            LoadAppInfoTask loadAppInfoTask = new LoadAppInfoTask(activity, this, appDataList);
            loadAppInfoTask.execute(0);
        }
    }
}


//appInfoを読み込むタスク
//progressと連動
class LoadAppInfoTask extends AsyncTask<Integer, Integer, Integer>{

    private AppCompatActivity activity;
    private DialogFragment progressDialog = new ProgressDialog();
    private AppDialog appDialog;
    private ArrayList<AppData> appDataList;

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
            List<ApplicationInfo> appInfoList = pm.getInstalledApplications(0);

            for (ApplicationInfo appInfo : appInfoList) {
                //プリインストールアプリは除外
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appDataList.add(new AppData(appInfo.toString(), pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(appInfo)));
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