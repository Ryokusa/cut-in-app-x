package com.ryokusasa.w3033901.cut_in_app_2.Animation;

import android.animation.TimeInterpolator;
import android.graphics.Point;
import android.view.animation.Interpolator;

import java.sql.Time;

//キーフレーム
public class KeyFrame implements Cloneable {
    private int frame;
    private TimeInterpolator interpolator;
    public KeyFrame(int frame, TimeInterpolator interpolator){
        this.frame = frame;
        this.interpolator = interpolator;
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
        private double x, y;
        public MoveKeyFrame(int frame, double x, double y, TimeInterpolator interpolator){
            super(frame, interpolator);
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

    //回転キーフレーム
    public static class RotateKeyFrame extends KeyFrame {
        private double radian;
        public RotateKeyFrame(int frame, double radian, TimeInterpolator interpolator){
            super(frame, interpolator);
            this.radian = radian;
        }

        public double getRadian() {
            return radian;
        }

        public void setRadian(double radian) {
            this.radian = radian;
        }
    }

    //大きさキーフレーム
    public static class ScaleKeyFrame extends KeyFrame {
        private double scaleX, scaleY;
        public ScaleKeyFrame(int frame, double scaleX, double scaleY, TimeInterpolator interpolator){
            super(frame, interpolator);
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }

        public double getScaleX() {
            return scaleX;
        }

        public void setScaleX(double scaleX) {
            this.scaleX = scaleX;
        }

        public double getScaleY() {
            return scaleY;
        }

        public void setScaleY(double scaleY) {
            this.scaleY = scaleY;
        }
    }
}
