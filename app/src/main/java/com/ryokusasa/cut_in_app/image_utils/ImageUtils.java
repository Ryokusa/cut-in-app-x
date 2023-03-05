package com.ryokusasa.cut_in_app.image_utils;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

//画像管理クラス
//内部・外部の画像管理
//すべての画像はここから取り出す
public class ImageUtils {
    public Map<ImageData, Drawable> imageList = new HashMap<>();

    //Drawableオブジェクトを取得
    public Drawable getDrawable(ImageData imageData){
        Drawable drawable = imageList.get(imageData);
        if(drawable == null){   //存在しない場合
            imageList.put(imageData, imageData.getDrawable());
            return imageData.getDrawable();
        }else{
            return drawable;
        }
    }
}
