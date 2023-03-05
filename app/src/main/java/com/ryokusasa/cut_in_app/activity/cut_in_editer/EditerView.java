package com.ryokusasa.cut_in_app.activity.cut_in_editer;

import android.content.Context;
import android.view.View;

import com.ryokusasa.cut_in_app.anim_obj.AnimObj;

import java.util.List;

/**
 * Created by fripl on 2018/01/22.
 * エディター画面のビュー
 */

public class EditerView extends View {
//    private static final String TAG = "EditerView";
//
//    private final List<AnimObj> animObjList = new ArrayList<>();  //オブジェクト
//    private final List<Bitmap> bitmapList = new ArrayList<>();
//    private final List<Matrix> matrixList = new ArrayList<>();
//    private final List<String> layerName = new ArrayList<>(); //レイヤー名
//    private final Paint mPaint = new Paint();
//
//    //アニメ編集関連
//    private AnimObj editAnimObj;    //編集時の一時的AnimObj
//    private Bitmap editAnimObjBitmap;
//    private boolean editAnimObjMove = false;    //移動フラグ
//    private AnimObj preAnimObj; //一個前のAnimObj
//    private Bitmap preAnimObjBitmap;
//    private AnimObj previewAnimObj; //プレビュー時のAnimObj
//    private Bitmap previewAnimObjBitmap;
//    private final long previewMaxFrame = 0;
//    private final long previewFrame = -1;
//    private final long preAnimId = -1;  //以前使ったアニメーションID
//
//    private final ScaleGestureDetector scaleGestureDetector;
//
//    //復数タッチフラグ
//    boolean multiTouch = false;
//
//    //描画用矩形範囲
//    Rect srcRect = new Rect(), destRect = new Rect();
//    Matrix matrix = new Matrix();
//
//    //選択オブジェクト番号
//    private int selObjId = -1;
//    //操作しているアニメーションの番号
//    public int selAnimId = -1;
//
//    //アニメ編集モードフラグ
//    public int animEdit = -1;
//    public static final int TRANSLATION = 0;
//    public static final int SCALE = 1;
//    public static final int ALPHA = 2;
//    public static final int ROTATE = 3;
//    public static final int TEXTSIZE = 4;
//    public static boolean animPreview = true;
//
//    //スクリーン情報
//    private final Matrix screenMatrix = new Matrix();
//    public int screenWidth, screenHeight;
//
//    //移動用
//    private int preFocusX, preFocusY;
//
//    //シークバーデータ
//    public int seekBarValue1;
//    public int seekBarValue2;
//
    public EditerView(Context context, List<AnimObj> animObjList){
        super(context);
//
//        //AnimObjがある場合は代入
//        if(animObjList != null) {
//            for (AnimObj ao : animObjList) {
//                addAnimObject(ao);
//            }
//        }
//
//
//        //スケールジェスチャーリスナー登録
//        //スケールジェスチャーリスナー
//        //なにも選択されていないときもしくはアニメーション編集時
//        //スクリーンスケール処理
//        //オブジェクトスケール処理(拡大基準座標は中心)
//        //初期値設定画面のみ操作可能
//        //画像オブジェクトの場合
//        //falseの場合Endするらしい
//        ScaleGestureDetector.SimpleOnScaleGestureListener onScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//
//                if (selObjId == -1) {    //なにも選択されていないときもしくはアニメーション編集時
//                    //スクリーンスケール処理
//                    screenMatrix.postScale(detector.getScaleFactor(), detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
//                } else if (selObjId != -1) {
//                    //オブジェクトスケール処理(拡大基準座標は中心)
//                    //初期値設定画面のみ操作可能
//                    if (CutInEditerActivity.sceneFlag == CutInEditerActivity.CUTIN_INIT) {
//                        AnimObj ao;
//                        if ((ao = animObjList.get(selObjId)).getType() == AnimObj.Type.Image) {
//                            //画像オブジェクトの場合
//                            matrixList.get(selObjId).postScale(detector.getScaleFactor(), detector.getScaleFactor(), (float) ao.getInitX() + ao.getInitWidth() / 2, (float) ao.getInitY() + ao.getInitHeight() / 2);
//                            RectF rectF = new RectF();
//                            rectF.set(0, 0, ao.getInitWidth(), ao.getInitHeight());
//                            rectF.offset((float) ao.getInitX(), (float) ao.getInitY());
//                            matrixList.get(selObjId).mapRect(rectF);
//                            ao.initMove(rectF.left, rectF.top);
//                            ao.setInitHeight((int) rectF.height());
//                            ao.setInitWidth((int) (ao.getImageRatio() * ao.getInitHeight()));
//                            matrixList.set(selObjId, new Matrix());
//                        }
//                    }
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onScaleBegin(ScaleGestureDetector detector) {
//
//                //falseの場合Endするらしい
//                return super.onScaleBegin(detector);
//            }
//
//            @Override
//            public void onScaleEnd(ScaleGestureDetector detector) {
//
//                super.onScaleEnd(detector);
//            }
//        };
//        scaleGestureDetector = new ScaleGestureDetector(context, onScaleGestureListener);
//        //画面サイズ取得
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
//        screenWidth = displayMetrics.widthPixels;
//        screenHeight = displayMetrics.heightPixels;
//
    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        //タイマーイベントで一定周期で呼び出される
//
//        //各種描画
//        canvas.concat(screenMatrix);
//
//        //スクリーン枠描画
//        mPaint.setStyle(Paint.Style.STROKE);    //線
//        mPaint.setARGB(255, 0,0,0);
//        canvas.drawRect(0, 0, screenWidth, screenHeight, mPaint);
//
//        //編集モードでない場合
//        //全オブジェクト描画
//        int i = 0;
//        for(AnimObj ao : animObjList){
//            //非選択オブジェクトは半透明
//            if(i != selObjId){
//                mPaint.setAlpha(127);
//            }else{
//                mPaint.setAlpha(255);
//            }
//
//            //TODO 回転描画 マトリックスで行えそう
//
//            i++;
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //スケールジェスチャー検知
//        scaleGestureDetector.onTouchEvent(event);
//
//        //単体タッチ
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                performClick();
//                if(!multiTouch) {
//                    if (animEdit == TRANSLATION) {
//                        RectF rectF = new RectF();
//                        if(editAnimObj.getType() == AnimObj.Type.Image) {
//                            //画像オブジェクト
//                            rectF.set(0, 0, editAnimObj.getInitWidth() * editAnimObj.getImageView().getScaleX(),  editAnimObj.getInitHeight() * editAnimObj.getImageView().getScaleY());
//                            rectF.offset(editAnimObj.getImageView().getTranslationX(), editAnimObj.getImageView().getTranslationY());
//                            screenMatrix.mapRect(rectF);
//                        }else if (editAnimObj.getType() == AnimObj.Type.Text){
//                            //テキストオブジェクト
//                            Rect rect = new Rect();
//                            Paint paint = new Paint();
//                            paint.setTextSize(editAnimObj.getInitTextSize() * getResources().getDisplayMetrics().scaledDensity);
//                            paint.getTextBounds(editAnimObj.getTextView().getText().toString(), 0, editAnimObj.getTextView().getText().length(), rect);
//                            rect.offset((int)editAnimObj.getInitX(), (int)editAnimObj.getInitY());
//                            rectF = new RectF(rect);
//                            screenMatrix.mapRect(rectF);
//                        }
//                        if(rectF.contains((int)event.getX(), (int)event.getY())) {   //タッチした場所がオブジェクトなら
//                            //移動編集時タッチで移動処理
//                            preFocusX = (int) (event.getX());
//                            preFocusY = (int) (event.getY());
//                            editAnimObjMove = true; //移動フラグ
//                        }else {
//                            editAnimObjMove = false;
//                        }
//                    }
//                }
//                break;
//
//            case MotionEvent.ACTION_UP:
//                Log.i(TAG, "ACTION_UP");
//
//                if(!multiTouch){
//                    //オブジェクト切り替え処理
//                    //初期値設定画面以外は無効
//                    if(CutInEditerActivity.sceneFlag == CutInEditerActivity.CUTIN_INIT) {
//                        AnimObj animObj;
//                        RectF rectF = new RectF();
//                        int i;
//                        for (i = animObjList.size() - 1; i >= 0; i--) {
//                            animObj = animObjList.get(i);
//                            if (animObj.getType() == AnimObj.Type.Image) {
//                                //画像オブジェクトの場合
//                                rectF.set(0, 0, animObj.getInitWidth(), animObj.getInitHeight());
//                                rectF.offset((int)animObj.getInitX(), (int)animObj.getInitY());
//                                screenMatrix.mapRect(rectF);    //クリック範囲マトリックス変換
//                                if (rectF.contains((int) event.getX(), (int) event.getY())) {
//                                    //範囲内なら
//                                    selObjId = i;
//                                    break;
//                                }
//                            } else if (animObj.getType() == AnimObj.Type.Text) {
//                                //テキストオブジェクトの場合
//                                Rect rect = new Rect();
//                                Paint paint = new Paint();
//                                paint.setTextSize(animObj.getInitTextSize() * getResources().getDisplayMetrics().scaledDensity);
//                                paint.getTextBounds(animObj.getTextView().getText().toString(), 0, animObj.getTextView().getText().length(), rect);
//                                rect.offset((int)animObj.getInitX(), (int)animObj.getInitY());
//                                rectF = new RectF(rect);
//                                screenMatrix.mapRect(rectF);
//                                if (rectF.contains((int) event.getX(), (int) event.getY())) {
//                                    //範囲内なら
//                                    selObjId = i;
//                                    break;
//                                }
//                            }
//                        }
//                        if (i < 0) selObjId = -1;  //見つからなかった場合未選択
//                    }
//                }else{
//                    //初期化ホルダーセット
//                    if(selObjId != -1) {
//                        //TODO animObjList.get(selObjId).setInitHolder();
//                    }
//                    multiTouch = false;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:{
//                if(!multiTouch){
//                    if(animEdit == TRANSLATION && editAnimObjMove){
//                        //移動編集時タッチで移動処理
//                        int focusX = (int)event.getX();
//                        int focusY = (int)event.getY();
//                        int dx = focusX - preFocusX;
//                        int dy = focusY - preFocusY;
//                        AnimObj animObj = editAnimObj;
//                        Matrix matrix = new Matrix(screenMatrix);
//                        float[] vecs = {dx, dy};
//                        //逆変換
//                        matrix.invert(matrix);
//                        matrix.mapVectors(vecs);
//                        dx = (int) vecs[0];
//                        dy = (int) vecs[1];
//                        View v = animObj.getObjView();
//                        v.setTranslationX(v.getTranslationX() + dx);
//                        v.setTranslationY(v.getTranslationY() + dy);
//                        //現在の座標格納
//                        preFocusX = focusX;
//                        preFocusY = focusY;
//
//                        Log.i(TAG, "" + v.getTranslationX());
//                    }
//                }
//            }
//        }
//
//        //復数タッチ
//        switch (event.getActionMasked()){
//            case MotionEvent.ACTION_POINTER_DOWN:
//                //タッチされたとき
//                Log.i(TAG, "POINTER_DOWN");
//
//
//                //タッチ移動開始座標格納(マルチタッチ時)
//                if(event.getPointerCount() == 2) {
//                    preFocusX = (int) (event.getX(1) + (event.getX(0) - event.getX(1)) / 2);
//                    preFocusY = (int) (event.getY(1) + (event.getY(0) - event.getY(1)) / 2);
//                }
//
//                multiTouch = true;  //復数タッチフラグ
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //タッチ場所が移動したとき
//
//                //タッチ移動処理
//                if(event.getPointerCount() == 2){
//                    int focusX = (int) (event.getX(1) + (event.getX(0) - event.getX(1)) / 2);
//                    int focusY = (int) (event.getY(1) + (event.getY(0) - event.getY(1)) / 2);
//                    int dx = focusX - preFocusX;
//                    int dy = focusY - preFocusY;
//                    if(selObjId == -1) {
//                        screenMatrix.postTranslate(dx, dy);
//                    }else if (selObjId != -1){
//                        AnimObj animObj = animObjList.get(selObjId);
//                        Matrix matrix = new Matrix(screenMatrix);
//                        float[] vecs = {dx, dy};
//                        //逆変換
//                        matrix.invert(matrix);
//                        matrix.mapVectors(vecs);
//                        dx = (int) vecs[0];
//                        dy = (int) vecs[1];
//
//                        animObjList.get(selObjId).initMove(animObj.getInitX() + dx,animObj.getInitY() + dy );
//                    }
//
//                    //現在の座標格納
//                    preFocusX = focusX;
//                    preFocusY = focusY;
//                }
//                break;
//            default:
//                break;
//
//        }
//
//        return true;
//    }
//
//    public int getSelObjId() {
//        return selObjId;
//    }
//
//    public void setSelObjId(int selObjId) {
//        this.selObjId = selObjId;
//    }
//
//    //オブジェクト追加
//    public void addAnimObject(AnimObj animObj){
//        animObjList.add(animObj);
//        if(animObj.getType() == AnimObj.Type.Image) {
//            //画像オブジェクト
//            bitmapList.add(drawableToBitmap(animObj.getImageView().getDrawable()));
//            matrixList.add(new Matrix());
//            layerName.add("" + layerName.size());
//        }else if (animObj.getType() == AnimObj.Type.Text){
//            //TODO テキストオブジェクトの場合のbitmap,matrix追加処理
//            //TODO テキストオブジェクトのサムネイル
//            bitmapList.add(drawableToBitmap(getResources().getDrawable(R.drawable.ic_launcher_foreground)));
//            matrixList.add(new Matrix());   //ダミー
//            layerName.add("" + layerName.size());
//        }
//    }
//
//    //オブジェクト削除
//    public void deleteAnimObject(int index){
//        animObjList.remove(index);
//        bitmapList.remove(index);
//        matrixList.remove(index);
//        layerName.remove(index);
//    }
//
//    public List<AnimObj> getAnimObjList() {
//        return animObjList;
//    }
//
//    public List<String> getLayerName() {
//        return layerName;
//    }
//
//    public List<Bitmap> getBitmapList() {
//        return bitmapList;
//    }
//
//    //Drawable→Bitmap
//    private Bitmap drawableToBitmap (Drawable drawable) {
//
//        //BitmapDrawableならBitmapを取得可能
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable)drawable).getBitmap();
//        }
//
//        //画像サイズのビットマップ作成
//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//
//        //bitmapのキャンバス取得して画像描画
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//
//        return bitmap;
//    }
//
//    //選択されたオブジェクトの指定アニメーションの終了時の状態を取得
//    public void getAnimStart(int frame){
//        editAnimObj = animObjList.get(selObjId);
//        editAnimObj.setFrame(frame);
//        editAnimObj.playFrame();
//    }
//
//    public AnimObj getEditAnimObj() {
//        return editAnimObj;
//    }
//
//    public void drawAnimObj(Canvas canvas, AnimObj ao, Bitmap bitmap, int alpha){
//        mPaint.setAlpha(alpha);
//        //TODO 回転描画 マトリックスで行えそう
//        if(ao.getType() == AnimObj.Type.Image){
//            //画像オブジェクトの場合
//            srcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
//            destRect.set(0,0, (int)(ao.getInitWidth()*ao.getImageView().getScaleX()), (int)(ao.getInitHeight()*ao.getImageView().getScaleY()));
//            destRect.offset((int)ao.getImageView().getTranslationX(), (int)ao.getImageView().getTranslationY());
//            matrix = new Matrix();
//            matrix.postRotate(ao.getImageView().getRotation(), bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
//            matrix.postScale(ao.getImageView().getScaleX(), ao.getImageView().getScaleY(), bitmap.getWidth()/2.0f, bitmap.getHeight()/2.0f);
//            matrix.postScale((float)ao.getInitWidth() / bitmap.getWidth()
//                    , (float)ao.getInitHeight() / bitmap.getHeight());
//            matrix.postTranslate(ao.getImageView().getTranslationX(), ao.getImageView().getTranslationY());
//            canvas.drawBitmap(bitmap, matrix, mPaint);
//        }else if (ao.getType() == AnimObj.Type.Text){
//            //テキストオブジェクトの場合
//            mPaint.setTextSize(ao.getTextView().getTextSize() / (ao.getTextView().getText().length()-1) *  getContext().getResources().getDisplayMetrics().density);  //サイズ取得
//            canvas.drawText(ao.getTextView().getText().toString(), ao.getTextView().getTranslationX(), ao.getTextView().getTranslationY(), mPaint);
//            Log.i(TAG, "" + ao.getTextView().getTranslationY());
//        }
//    }
//
//    //編集AnimObjのパラメータ入手
//    public int getEditAnimObjTranslationX(){
//        if(animObjList.get(selObjId).getType() == AnimObj.Type.Image){
//            return (int)editAnimObj.getImageView().getTranslationX();
//        }else{
//            return (int)editAnimObj.getTextView().getTranslationX();
//        }
//    }
//
//    public int getEditAnimObjTranslationY(){
//        if(animObjList.get(selObjId).getType() == AnimObj.Type.Image){
//            return (int)editAnimObj.getImageView().getTranslationY();
//        }else{
//            return (int)editAnimObj.getTextView().getTranslationY();
//        }
//    }
//
//    //プレビュー描画
//    public void drawPreview(Canvas canvas){
//        //TODO プレビュー
//    }
//
//    //TODO 他アニメーションパラメータ
//    /*
//    public int getEditAnimObjAlpha(){
//        if(animObjList.get(selObjId).getType() == AnimObj.Type.Image){
//            return (int)editAnimObj.imageView.getTranslationX();
//        }else{
//            return (int)editAnimObj.textView.getTranslationX();
//        }
//    }
//    */
}