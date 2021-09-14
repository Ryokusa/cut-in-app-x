package com.ryokusasa.cut_in_app.Animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

//TODO: 欲をいうとKeyFrameにすべてのフレーム情報が入り(フォルダー機能)、getですべてのフレームをゲット
/* 考えてみたがいらない可能性あり。それよりanimationをまとめて初期化等必要かも */

//キーフレーム集合体
public class KeyFrameAnimation implements Cloneable {
    ArrayList<KeyFrame> keyFrameList;
    ArrayList<KeyFrame> fullKeyFrameList;   //完全体
    int frameNum;   //フレーム数
    int culFrame = 0;   //現在フレーム
    String[] keys;

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

        //各フレーム計算
        int i = 0, j;
        int df;  //フレーム差分
        ArrayList<Double> d = new ArrayList<>();  //座標差分
        float interpolated; //インターポレーター値
        HashMap<String, Double> v;  //プロパティ
        KeyFrame pKeyFrame = getKeyFrameList().get(0);    //前のキーフレーム

        for (KeyFrame keyFrame : keyFrameList){
            //差分
            df = keyFrame.getFrame() - pKeyFrame.getFrame();
            d.clear();
            for (String key : keys) {
                d.add(keyFrame.values.get(key) - pKeyFrame.values.get(key));
            }
            for(; i < keyFrame.getFrame(); i++){
                if(df == 0){
                    //最初のキーフレーム(dfが0)までは同じ値で埋めるため
                    interpolated = 1;
                }else {
                    //イージング
                    interpolated = keyFrame.getInterpolator().getInterpolation((float) ((i - pKeyFrame.getFrame())) / df);
                }

                //values作成
                v = new HashMap<>();
                for (j=0; j < keys.length; j++){
                    v.put(keys[j], pKeyFrame.values.get(keys[j]) + (d.get(j) * interpolated));
                }
                fullKeyFrameList.add(new KeyFrame(i, v, null));
            }
            pKeyFrame = keyFrame;
        }

        //最後埋める
        for (; i < frameNum; i++){
            fullKeyFrameList.add(pKeyFrame);
        }
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
    public KeyFrame getFrame(int frame){
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
            keys = new String[]{"x", "y"};
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
            keys = new String[]{"radian"};
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
            keys = new String[]{"scaleX", "scaleY"};
        }

        @Override
        public ScaleKeyFrameAnimation clone() throws CloneNotSupportedException {
            return (ScaleKeyFrameAnimation) super.clone();
        }
    }
}
