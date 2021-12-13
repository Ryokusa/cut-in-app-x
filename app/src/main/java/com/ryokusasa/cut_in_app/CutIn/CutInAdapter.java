package com.ryokusasa.cut_in_app.CutIn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryokusasa.cut_in_app.R;
import com.ryokusasa.cut_in_app.UtilCommon;

import java.util.List;

/**
 * Created by fripl on 2018/01/15.
 */

public class CutInAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;  //レイアウト設定用
    private final List<CutIn> cutInList;

    //タグ付け用
    private static class ViewHolder{
        int position;
        TextView textView;
        ImageView imageView;

        ViewHolder(View v, int position){
            this.position = position;
            textView = v.findViewById(R.id.CutInTitle);
            imageView = v.findViewById(R.id.thumbnail);
        }
    }

    //コンストラクタ
    public CutInAdapter(Context context, int resource, List<CutIn> cutInList){
        layoutInflater = LayoutInflater.from(context);
        this.cutInList = cutInList;
    }

    @Override
    public Object getItem(int position){
        return cutInList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getCount(){
        return cutInList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parentView){
        final ViewHolder viewHolder;

        //すでにある場合は再利用
        if(convertView == null){
            //ない場合
            convertView = layoutInflater.inflate(R.layout.cut_in_adapter_layout, null);
            viewHolder = new ViewHolder(convertView, position);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();

            //ポジション格納
            viewHolder.position = position;
        }

        CutIn cutin = cutInList.get(position);

        //カットインサムネイルとタイトル設定予定
        viewHolder.imageView.setImageDrawable(cutin.getThumbnail());
        viewHolder.textView.setText(cutin.title);

        return convertView;

    }

}