package com.ryokusasa.cut_in_app.ImageUtils;

import android.graphics.drawable.Drawable;
import android.media.Image;

import androidx.core.content.res.ResourcesCompat;

import com.ryokusasa.cut_in_app.UtilCommon;

//画像の保存先データ
public class ImageData {
    public final static int INTERNAL_STORAGE = 0;
    public final static int EXTERNAL_STORAGE = 1;
    public final static int ASSET = 2;
    public final static int RESOURCE = 3;

    public final String path;    //ファイルパス
    public final int flag;  //ファイルの場所
    public int resource_id;   //リソースID

    public ImageData(String path, int flag){
        this.path = path;
        this.flag = flag;
    }

    //リソースから
    public ImageData(int id){
        this("", RESOURCE);
        this.resource_id = id;
    }

    //Drawable取得関数
    public Drawable getDrawable(){
        if(flag == INTERNAL_STORAGE){
            //TODO: 内部ストレージ
        }else if(flag == EXTERNAL_STORAGE){
            //TODO: 外部ストレージ
        }else if(flag == ASSET){
            //TODO: アセットフォルダ
        }else if(flag == RESOURCE){
            //リソースフォルダ
            return ResourcesCompat.getDrawable(UtilCommon.getInstance().getResources(), resource_id, null);
        }
        return null;
    }
}
