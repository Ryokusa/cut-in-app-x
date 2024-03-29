package com.ryokusasa.cut_in_app.image_utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.ryokusasa.cut_in_app.UtilCommon;

import java.io.FileNotFoundException;
import java.io.InputStream;

//画像の保存先データ
public class ImageData {
    private final static String TAG = "ImageData";

    public final static int INTERNAL_STORAGE = 0;
    public final static int EXTERNAL_STORAGE = 1;
    public final static int ASSET = 2;
    public final static int RESOURCE = 3;

    public final Uri uri;    //ファイルパス
    public final int flag;  //ファイルの場所
    public int resource_id;   //リソースID

    public ImageData(Uri uri, int flag){
        this.uri = uri;
        this.flag = flag;
    }

    //リソースから
    public ImageData(int id){
        this(null, RESOURCE);
        this.resource_id = id;
    }

    //Drawable取得関数
    public Drawable getDrawable(){
        if(flag == INTERNAL_STORAGE || flag == EXTERNAL_STORAGE){
            //内部固有ストレージは使わないかも
            InputStream inputStream;
            try{
                inputStream = UtilCommon.getInstance().getContentResolver().openInputStream(uri);
            }catch (FileNotFoundException e){
                Log.i(TAG, "ファイルが存在しません\n" + uri);
                return null;
            }
            return Drawable.createFromStream(inputStream, null);
        }else if(flag == ASSET){
            //TODO: アセットフォルダ
        }else if(flag == RESOURCE){
            //リソースフォルダ
            return ResourcesCompat.getDrawable(UtilCommon.getInstance().getResources(), resource_id, null);
        }
        return null;
    }
}
