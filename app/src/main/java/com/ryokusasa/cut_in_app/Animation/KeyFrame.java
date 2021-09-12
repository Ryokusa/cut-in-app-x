package com.ryokusasa.cut_in_app.Animation;

import android.animation.TimeInterpolator;

import java.util.HashMap;

//キーフレーム
public class KeyFrame implements Cloneable {
    private int frame;
    private TimeInterpolator interpolator;
    public HashMap<String, Double> values; //値のマップ

    public KeyFrame(int frame, TimeInterpolator interpolator){
        this.frame = frame;
        this.interpolator = interpolator;
        this.values = new HashMap<>();
    }
    public KeyFrame(int frame, HashMap<String, Double> values, TimeInterpolator interpolator){
        this.frame = frame;
        this.interpolator = interpolator;
        this.values = values;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    @Override
    public KeyFrame clone() throws CloneNotSupportedException {
        KeyFrame rKeyFrame;
        try{
            rKeyFrame = (KeyFrame) super.clone();
            rKeyFrame = new KeyFrame(frame, interpolator);
            return rKeyFrame;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //移動キーフレーム
    public static class MoveKeyFrame extends KeyFrame {
        public MoveKeyFrame(int frame, double x, double y, TimeInterpolator interpolator){
            super(frame, interpolator);
            this.values.put("x", x);
            this.values.put("y", y);
        }
    }

    //回転キーフレーム
    public static class RotateKeyFrame extends KeyFrame {
        public RotateKeyFrame(int frame, double radian, TimeInterpolator interpolator){
            super(frame, interpolator);
            this.values.put("radian", radian);
        }
    }

    //大きさキーフレーム
    public static class ScaleKeyFrame extends KeyFrame {
        public ScaleKeyFrame(int frame, double scaleX, double scaleY, TimeInterpolator interpolator){
            super(frame, interpolator);
            this.values.put("scaleX", scaleX);
            this.values.put("scaleY", scaleY);
        }
    }
}
