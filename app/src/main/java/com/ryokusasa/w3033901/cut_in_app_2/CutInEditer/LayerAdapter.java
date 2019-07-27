package com.ryokusasa.w3033901.cut_in_app_2.CutInEditer;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.ryokusasa.w3033901.cut_in_app_2.AppDataManager.AnimObj;
import com.ryokusasa.w3033901.cut_in_app_2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fripl on 2018/01/28.
 */

public class LayerAdapter extends ArrayAdapter {
    private LayoutInflater layoutInflater;  //レイアウト設定用
    private List<AnimObj> animObjList = new ArrayList<>();
    private List<String> layerName = new ArrayList<>();
    private int selId;  //選択オブジェクトId

    //タグ用ホルダー
    private class ViewHolder{
        ImageView layerImage;
        TextView layerName;
        public ViewHolder(View v, int position){
            layerImage = (ImageView)v.findViewById(R.id.layerImage);
            layerName = (TextView)v.findViewById(R.id.layerName);
        }
    }

    public LayerAdapter(Context context, int resource, List<AnimObj> animObjList, List<String> layerName, int selId){
        super(context, resource, animObjList);
        layoutInflater = LayoutInflater.from(context);
        this.animObjList = animObjList;
        this.layerName = layerName;
        this.selId = selId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            //新しく作成
            convertView = layoutInflater.inflate(R.layout.layer_adapter_layout, null);
            viewHolder = new ViewHolder(convertView, position);
            convertView.setTag(viewHolder); //タグつけ

        }else{
            //再利用
            viewHolder = (ViewHolder) convertView.getTag(); //ゲット
        }

        viewHolder.layerName.setText(layerName.get(position));
        if(animObjList.get(position).getType() == AnimObj.Type.Image) {
            //画像オブジェクトの場合画像表示
            viewHolder.layerImage.setImageDrawable(animObjList.get(position).getImageView().getDrawable());
        }else if (animObjList.get(position).getType() == AnimObj.Type.Text){
            //テキストオブジェクトの場合なにも表示できない
            //TODO テキストオブジェクトのサムネイル追加
            viewHolder.layerImage.setImageDrawable(null);
            viewHolder.layerName.setText(layerName.get(position) + "「" + animObjList.get(position).getTextView().getText() + "」");
        }


        //選択されたレイヤーのみ明るい
        if(position != selId){
            convertView.setBackgroundColor(Color.argb(50,0,0,0));
        }else{
            convertView.setBackgroundColor(Color.argb(0,0,0,0));
        }

        return convertView;
    }

    public void setSelId(int selId) {
        this.selId = selId;
    }
}