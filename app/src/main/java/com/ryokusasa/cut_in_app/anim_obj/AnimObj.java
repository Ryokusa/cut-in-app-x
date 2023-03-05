package com.ryokusasa.cut_in_app.anim_obj;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import com.ryokusasa.cut_in_app.animation.KeyFrame;
import com.ryokusasa.cut_in_app.animation.KeyFrameAnimation;

/**
 * アニメーションオブジェクト
 * ImageView TextViewいずれかを所持する
 * アニメーション情報も持つ
 */

//TODO: 座標形式を複数に(中心・右上など)
//TODO: 画面の右端などの定数を設定
abstract public class AnimObj {


    //キーフレームアニメーション
    private final KeyFrameAnimation.MoveKeyFrameAnimation moveKeyFrameAnimation;
    private final KeyFrameAnimation.RotateKeyFrameAnimation rotateKeyFrameAnimation;
    private final KeyFrameAnimation.ScaleKeyFrameAnimation scaleKeyFrameAnimation;

    //フレーム関連
    private int frameNum;
    private static final int MAX_FRAME_NUM = 1000;
    private int currentFrame;

    //プロパティ
    public double x;
    public double y;
    public double radian;
    public double scaleX;
    public double scaleY;

    //画像オブジェクト作成
    //w,hは伸縮
    public AnimObj(double x, double y){

        frameNum = MAX_FRAME_NUM;    //アニメーションの長さ
        currentFrame = 0;   //現在のフレーム

        //初期化
        moveKeyFrameAnimation = new KeyFrameAnimation.MoveKeyFrameAnimation(frameNum);
        rotateKeyFrameAnimation = new KeyFrameAnimation.RotateKeyFrameAnimation(frameNum);
        scaleKeyFrameAnimation = new KeyFrameAnimation.ScaleKeyFrameAnimation(frameNum);
        initMove(x, y);
        initRotate(0);
        initScale(1, 1);
        makeAnimation(frameNum);
    }

    /* アニメーション作成関連 */
    public void initMove(double x, double y){
        this.x = x;
        this.y = y;
        moveKeyFrameAnimation.addKeyFrame(new KeyFrame.MoveKeyFrame(0, x, y, new LinearInterpolator()));
    }

    public void initRotate(double radian){
        this.radian = radian;
        rotateKeyFrameAnimation.addKeyFrame(new KeyFrame.RotateKeyFrame(0, radian, new LinearInterpolator()));
    }

    public void initScale(double scaleX, double scaleY){
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        scaleKeyFrameAnimation.addKeyFrame(new KeyFrame.ScaleKeyFrame(0, scaleX, scaleY, new LinearInterpolator()));
    }

    /* アニメーション編集関連の関数 */
    public void addMove(int frame, double x, double y, TimeInterpolator interpolator){
        moveKeyFrameAnimation.addKeyFrame(new KeyFrame.MoveKeyFrame(frame, x, y, interpolator));
        moveKeyFrameAnimation.makeKeyFrameAnimation();
    }

    public void addRotate(int frame, double radian, TimeInterpolator interpolator){
        rotateKeyFrameAnimation.addKeyFrame(new KeyFrame.RotateKeyFrame(frame, radian, interpolator));
        rotateKeyFrameAnimation.makeKeyFrameAnimation();
    }

    public void addScale(int frame, double scaleX, double scaleY, TimeInterpolator interpolator){
        scaleKeyFrameAnimation.addKeyFrame(new KeyFrame.ScaleKeyFrame(frame, scaleX, scaleY, interpolator));
        scaleKeyFrameAnimation.makeKeyFrameAnimation();
    }

    //アニメーション作成
    public void makeAnimation(int frameNum){
        moveKeyFrameAnimation.makeKeyFrameAnimation();
        rotateKeyFrameAnimation.makeKeyFrameAnimation();
        scaleKeyFrameAnimation.makeKeyFrameAnimation();
    }

    //フレーム数設定
    public void setFrameNum(int frameNum){
        if(frameNum >= 0 && frameNum <= MAX_FRAME_NUM){
            this.frameNum = frameNum;
        }else{
            this.frameNum = MAX_FRAME_NUM;
        }
        setFrame(0);    //初期化
    }

    //描画
    abstract void draw(Canvas canvas);

    //指定フレームへ
    public void setFrame(int frame){
        currentFrame = frame;
        moveKeyFrameAnimation.setFrame(frame);
        rotateKeyFrameAnimation.setFrame(frame);
        scaleKeyFrameAnimation.setFrame(frame);
    }

    //次のフレームを再生
    //終了したかどうかを返す
    public boolean playFrame(Canvas canvas){
        KeyFrame moveKeyFrame = moveKeyFrameAnimation.nextFrame();
        KeyFrame rotateKeyFrame = rotateKeyFrameAnimation.nextFrame();
        KeyFrame scaleKeyFrame = scaleKeyFrameAnimation.nextFrame();
        if(currentFrame < frameNum) {   //フレーム数から外れたら再生しない
            Log.i("AnimObj", "frame:" + currentFrame + " x:" + moveKeyFrame.values.get("x"));
            this.x = moveKeyFrame.values.get("x");
            this.y = moveKeyFrame.values.get("y");
            this.radian = rotateKeyFrame.values.get("radian");
            this.scaleX = scaleKeyFrame.values.get("scaleX");
            this.scaleY = scaleKeyFrame.values.get("scaleY");
            currentFrame++;
            draw(canvas);
            return false;
        }else {
            //終了処理
            return true;
        }
    }

    public double getInitX(){
        return moveKeyFrameAnimation.getFrame(0).values.get("x");
    }

    public double getInitY(){
        return moveKeyFrameAnimation.getFrame(0).values.get("y");
    }
}