package com.ryokusasa.cut_in_app.anim_obj;

import static com.ryokusasa.cut_in_app.cut_in.cut_in_view.CutInViewControllerKt.px2dp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import com.ryokusasa.cut_in_app.image_utils.ImageData;
import com.ryokusasa.cut_in_app.UtilCommon;

//画像オブジェクト
public class ImageObj extends AnimObj{
    private final String TAG = "ImageObj";

    private final ImageData imageData;
    private final int width;
    private final int halfWidth;
    private final int height;  //変形後
    private final int halfHeight;

    private final Drawable drawable;
    private final Bitmap bitmap;

    private final Matrix matrix = new Matrix();

    public ImageObj(ImageData imageData, double x, double y, int width, int height){
        super(x, y);

        this.imageData = imageData;
        this.width = width;
        this.height = height;
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;

        drawable = UtilCommon.getImageUtils().getDrawable(imageData);
        bitmap = UtilCommon.getImageUtils().getBitmap(imageData);
    }


    //イメージデータのみ
    public ImageObj(ImageData imageData){
        this( imageData, 0, 0,
                UtilCommon.getImageUtils().getDrawable(imageData).getIntrinsicWidth(),
                UtilCommon.getImageUtils().getDrawable(imageData).getIntrinsicHeight());
    }

    //描画
    public void draw(Canvas canvas) {
        if (drawable != null) {
            int dp_x = (int)px2dp((int)this.x);
            int dp_y = (int)px2dp((int)this.y);
            int dp_w= (int)px2dp((int)this.width);
            int dp_h = (int)px2dp((int)this.height);

            matrix.reset();
            float sx = (float)this.width / bitmap.getWidth();
            float sy = (float)this.height / bitmap.getHeight();
            matrix.postScale(sx, sy);
            matrix.postRotate((float)(this.radian * 180 / Math.PI), (float)halfWidth, (float)halfHeight);
            matrix.postTranslate((int)this.x, (int)this.y);

//            drawable.setBounds((int) this.x, (int) this.y, (int) this.x + this.width, (int) this.y + this.height);
            canvas.drawBitmap(bitmap, matrix, null);
            drawable.draw(canvas);
        }
    }
}