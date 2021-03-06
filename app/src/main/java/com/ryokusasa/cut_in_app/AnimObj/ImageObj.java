package com.ryokusasa.cut_in_app.AnimObj;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.ryokusasa.cut_in_app.Animation.KeyFrameAnimation;
import com.ryokusasa.cut_in_app.ImageUtils.ImageData;
import com.ryokusasa.cut_in_app.ImageUtils.ImageUtils;
import com.ryokusasa.cut_in_app.UtilCommon;

//画像オブジェクト
public class ImageObj extends AnimObj{
    private final String TAG = "ImageObj";

    private final ImageData imageData;
    private final int width;
    private final int height;  //変形後

    public ImageObj(double x, double y, ImageData imageData, int width, int height) {
        super(x, y);
        this.imageData = imageData;
        this.width = width;
        this.height = height;
    }

    public ImageObj(ImageData imageData, double x, double y, int width, int height){
        super(x, y);

        this.imageData = imageData;
        this.width = width;
        this.height = height;
    }

    //イメージデータのみ
    public ImageObj(ImageData imageData){
        this( imageData, 0, 0,
                UtilCommon.getImageUtils().getDrawable(imageData).getIntrinsicWidth(),
                UtilCommon.getImageUtils().getDrawable(imageData).getIntrinsicHeight());
    }

    //描画
    public void draw(Canvas canvas) {
        Drawable drawable = UtilCommon.getImageUtils().getDrawable(imageData);
        if (drawable != null) {
            int dp_x = (int)UtilCommon.px2dp((int)this.x);
            int dp_y = (int)UtilCommon.px2dp((int)this.y);
            int dp_w= (int)UtilCommon.px2dp((int)this.width);
            int dp_h = (int)UtilCommon.px2dp((int)this.height);
            drawable.setBounds((int) this.x, (int) this.y, (int) this.x + this.width, (int) this.y + this.height);
            drawable.draw(canvas);
        }
    }
}