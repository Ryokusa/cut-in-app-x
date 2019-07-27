package com.ryokusasa.w3033901.cut_in_app_2.Animation;

import android.animation.TimeInterpolator;
import android.graphics.Point;
import android.view.animation.Interpolator;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KeyFrameAnimation {
    ArrayList<KeyFrame> keyFrameList;
    ArrayList<KeyFrame> fullKeyFrameList;   //完全体
    int frameNum;   //フレーム数
    int culFrame = 0;   //現在フレーム

    public KeyFrameAnimation(int frameNum) {
        keyFrameList = new ArrayList<KeyFrame>();
        fullKeyFrameList = new ArrayList<KeyFrame>();
        this.frameNum = frameNum;
    }

    //フレーム順にソートしてまとめる
    public void makeKeyFrameAnimation(){
        Collections.sort(keyFrameList, new Comparator<KeyFrame>() {
            @Override
            public int compare(KeyFrame t0, KeyFrame t1) {
                if(t0 == null || t1 == null)
                    return 0;
                return (t0.getFrame() > t1.getFrame()) ? -1 : 1;
            }
        });

        fullKeyFrameList.clear();
    }

    //キーフレーム取得
    public ArrayList<KeyFrame> getKeyFrameList() {
        return keyFrameList;
    }

    //キーフレーム追加
    public void addKeyFrame(KeyFrame keyFrame){
        keyFrameList.add(keyFrame);
    }

    //次のフレーム
    public KeyFrame nextFrame(){
        try{
            return fullKeyFrameList.get(culFrame++);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    //リセット
    public void resetFrame(){
        culFrame = 0;
    }

    //移動キーフレームアニメーション
    public static class MoveKeyFrameAnimation extends KeyFrameAnimation {

        public MoveKeyFrameAnimation(int frameNum){
            super(frameNum);
        }

        //計算して該当フレーム
        @Override
        public void makeKeyFrameAnimation() {
            super.makeKeyFrameAnimation();

            int i = 0;
            int df;  //フレーム差分
            double dx, dy;  //座標差分
            float interpolated; //インターポレーター値
            KeyFrame.MoveKeyFrame pKeyFrame = new KeyFrame.MoveKeyFrame(0, 0, 0, null);    //前のキーフレーム
            for (KeyFrame keyFrame : keyFrameList){
                df = keyFrame.getFrame() - pKeyFrame.getFrame();
                for(; i <= keyFrame.getFrame(); i++){
                    //イージング
                    interpolated = keyFrame.getInterpolator().getInterpolation((float)(pKeyFrame.getFrame()+i) / df);
                    dx = ((KeyFrame.MoveKeyFrame)keyFrame).getX() - pKeyFrame.getX();
                    dy = ((KeyFrame.MoveKeyFrame)keyFrame).getY() - pKeyFrame.getY();
                    fullKeyFrameList.add(new KeyFrame.MoveKeyFrame(i,
                            pKeyFrame.getX() + (dx * interpolated),
                            pKeyFrame.getY() + (dy * interpolated),
                            null));
                }
                pKeyFrame = (KeyFrame.MoveKeyFrame) keyFrame;
            }

            //最後埋める
            for (; i < frameNum; i++){
                fullKeyFrameList.add(pKeyFrame);
            }
        }
    }

    //回転キーフレームアニメーション
}
