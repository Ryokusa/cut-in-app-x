package com.ryokusasa.w3033901.cut_in_app_2.Dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryokusasa.w3033901.cut_in_app_2.CutinService;
import com.ryokusasa.w3033901.cut_in_app_2.MainActivity;
import com.ryokusasa.w3033901.cut_in_app_2.R;

import java.util.List;

/**
 * Created by Ryokusasa on 2017/12/17.
 */

public class AppDataAdapter extends ArrayAdapter {
    private LayoutInflater layoutInflater;

    //タグつけ用
    private static class ViewFolder {   //独立したクラスとしてstatic
        TextView textView;
        ImageView imageView;
        TextView haveCutIn;

        ViewFolder(View v)
        {
            //オブジェクト取得
            textView = (TextView)v.findViewById(R.id.textView);
            imageView = (ImageView)v.findViewById(R.id.cutInThumbnail);
            haveCutIn = (TextView)v.findViewById(R.id.haveCutIn);
        }
    }


    public AppDataAdapter(Context context, int resource, List<AppData> appDataList)
    {
        super(context, resource, appDataList);

        //layoutInflater生成
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        ViewFolder viewFolder;

        //findViewByIdやlayoutInflaterを多く使用するとメモリ消費が激しいのですべて再利用する
        if(convertView == null){    //リサイクルできない場合
            //Viewを設定する(一行分のリストViewのレイアウトから)
            convertView = layoutInflater.inflate(R.layout.app_data_adapter_layout, null);

            //ViewFolderをnewし、convertViewのオブジェクトを取得
            viewFolder = new ViewFolder(convertView);

            //リサイクルするためにViewにタグ付け
            convertView.setTag(viewFolder);
        }else{  //リサイクル出来る場合

            //タグ付けられたオブジェクトを取得
            viewFolder = (ViewFolder)convertView.getTag();
        }

        //positionに格納されたアプリデータを取得
        AppData appData = (AppData)getItem(position);
        boolean haveCutIn = false;
        if(appData.isUsed()){
            haveCutIn = true;
        }

        //セット
        viewFolder.imageView.setImageDrawable(appData.getIconDrawable());
        viewFolder.textView.setText(appData.getAppName());
        if(haveCutIn){
            viewFolder.haveCutIn.setText("設定済み");
        }else{
            viewFolder.haveCutIn.setText("");
        }

        return convertView;
    }
}