package com.ryokusasa.cut_in_app.Dialog;

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

import com.ryokusasa.cut_in_app.CutIn.CutInHolder;
import com.ryokusasa.cut_in_app.MainActivity;
import com.ryokusasa.cut_in_app.UtilCommon;

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
    UtilCommon utilCommon;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("アプリ選択\n");
        final MainActivity activity = (MainActivity) getActivity();
        utilCommon = (UtilCommon) activity.getApplication();

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
        ArrayList<AppData> appDataList = utilCommon.appDataList;
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
            List<ApplicationInfo> appInfoList = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo appInfo : appInfoList) {

                //プリインストールアプリは除外（と思ったけどいらないかも）
                /* 仮想機上ではなぜか一部がプリインストール扱いになる */
                if((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    appDataList.add(new AppData(appInfo.toString(), pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(appInfo)));
                    //it's a system app, not interested
                } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    //Discard this one
                    //in this case, it should be a user-installed app
                } else {
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