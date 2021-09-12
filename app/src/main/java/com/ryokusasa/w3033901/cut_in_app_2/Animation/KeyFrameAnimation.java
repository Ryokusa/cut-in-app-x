package com.ryokusasa.w3033901.cut_in_app_2.Animation;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//TODO: 欲をいうとKeyFrameにすべてのフレーム情報が入り(フォルダー機能)、getですべてのフレームをゲット

//キーフレーム集合体
public class KeyFrameAnimation implements Cloneable {
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
                return (t0.getFrame() > t1.getFrame()) ? 1 : -1;
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
        //もしかぶっているなら上書き
        for (int i = 0; i < keyFrameList.size(); i++){
            if(keyFrameList.get(i).getFrame() == keyFrame.getFrame()){
                keyFrameList.set(i, keyFrame);
                return;
            }
        }
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

    //フレーム
    public KeyFrame playFrame(int frame){
        return fullKeyFrameList.get(frame);
    }

    //フレームセット
    public void setFrame(int frame){
        culFrame = frame;
    }

    //リセット
    public void resetFrame(){
        culFrame = 0;
    }

    @Override
    public KeyFrameAnimation clone() throws CloneNotSupportedException {
        KeyFrameAnimation keyFrameAnimation;
        try{
            keyFrameAnimation = (KeyFrameAnimation) super.clone();
            keyFrameAnimation.keyFrameList = new ArrayList<KeyFrame>(this.keyFrameList);
            keyFrameAnimation.fullKeyFrameList = new ArrayList<KeyFrame>(this.fullKeyFrameList);
            return keyFrameAnimation;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }

    //移動キーフレームアニメーション
    public static class MoveKeyFrameAnimation extends KeyFrameAnimation implements Cloneable {

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
                for(; i < keyFrame.getFrame(); i++){
                    //イージング
                    interpolated = keyFrame.getInterpolator().getInterpolation((float)((i-pKeyFrame.getFrame())) / df);
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

        @Override
        public MoveKeyFrameAnimation clone() throws CloneNotSupportedException {
            return (MoveKeyFrameAnimation) super.clone();
        }
    }

    //回転キーフレームアニメーション
    public static class RotateKeyFrameAnimation extends KeyFrameAnimation implements Cloneable {

        public RotateKeyFrameAnimation(int frameNum){
            super(frameNum);
        }

        //計算して該当フレーム
        @Override
        public void makeKeyFrameAnimation() {
            super.makeKeyFrameAnimation();

            int i = 0;
            int df;  //フレーム差分
            double dr;  //回転差分
            float interpolated; //インターポレーター値
            KeyFrame.RotateKeyFrame pKeyFrame = new KeyFrame.RotateKeyFrame(0, 0, null);    //前のキーフレーム
            for (KeyFrame keyFrame : keyFrameList){
                df = keyFrame.getFrame() - pKeyFrame.getFrame();
                for(; i < keyFrame.getFrame(); i++){
                    //イージング
                    interpolated = keyFrame.getInterpolator().getInterpolation((float)((i-pKeyFrame.getFrame())) / df);
                    dr = ((KeyFrame.RotateKeyFrame)keyFrame).getRadian() - pKeyFrame.getRadian();
                    fullKeyFrameList.add(new KeyFrame.RotateKeyFrame(i,
                            pKeyFrame.getRadian() + (dr * interpolated),
                            null));
                }
                pKeyFrame = (KeyFrame.RotateKeyFrame) keyFrame;
            }

            //最後埋める
            for (; i < frameNum; i++){
                fullKeyFrameList.add(pKeyFrame);
            }
        }

        @Override
       public RotateKeyFrameAnimation clone() throws CloneNotSupportedException {
            return (RotateKeyFrameAnimation) super.clone();
        }
    }

    //拡大キーフレームアニメーション
    public static class ScaleKeyFrameAnimation extends KeyFrameAnimation implements Cloneable {

        public ScaleKeyFrameAnimation(int frameNum){
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
            KeyFrame.ScaleKeyFrame pKeyFrame = new KeyFrame.ScaleKeyFrame(0, 0, 0, null);    //前のキーフレーム
            for (KeyFrame keyFrame : keyFrameList){
                df = keyFrame.getFrame() - pKeyFrame.getFrame();
                for(; i < keyFrame.getFrame(); i++){
                    //イージング
                    interpolated = keyFrame.getInterpolator().getInterpolation((float)((i-pKeyFrame.getFrame())) / df);
                    dx = ((KeyFrame.ScaleKeyFrame)keyFrame).getScaleX() - pKeyFrame.getScaleX();
                    dy = ((KeyFrame.ScaleKeyFrame)keyFrame).getScaleY() - pKeyFrame.getScaleY();
                    fullKeyFrameList.add(new KeyFrame.ScaleKeyFrame(i,
                            pKeyFrame.getScaleX() + (dx * interpolated),
                            pKeyFrame.getScaleY() + (dy * interpolated),
                            null));
                }
                pKeyFrame = (KeyFrame.ScaleKeyFrame) keyFrame;
            }

            //最後埋める
            for (; i < frameNum; i++){
                fullKeyFrameList.add(pKeyFrame);
            }
        }

        @Override
        public ScaleKeyFrameAnimation clone() throws CloneNotSupportedException {
            return (ScaleKeyFrameAnimation) super.clone();
        }
    }
}
