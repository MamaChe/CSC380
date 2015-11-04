package com.ace.proto.prototype;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ace on 10/15/15.
 */
public class MapView extends ImageView implements View.OnClickListener {
    private boolean rotated = false;
    private static final Paint paint = new Paint();
    private Path path = new Path();
    public AnimatorSet rotate;
    public static final float rotationAngle = 26, rotationYScale = (float).5;
    public float userScale = 1;
    public MapView(Context context) {
        super(context);
        init();
    }
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    @Override
    public void onClick(View view){
        ((MapViewGroup)findViewById(R.id.MapViewGroup)).setActiveLayer(view);
    }

    private void init(){
        paint.setStrokeWidth(7);
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        setPivotY(getHeight());
        rotate = new AnimatorSet();
        float rotationXScale = (float).95;
        rotate.play(ObjectAnimator.ofFloat(this, "rotationX", rotationAngle)).with(ObjectAnimator.ofFloat(this, "scaleY", rotationYScale)).with(ObjectAnimator.ofFloat(this, "scaleX", rotationXScale));
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        //Log.d("drawing", "fs");
    }
    public void resetPath(){
        path = new Path();
        invalidate();
    }
    public void setPathStart(Location start) {
        path.moveTo(start.getLongitude(), start.getLatitude());
    }
    public void rotate(){
        setPivotY(getHeight());
        setPivotX(getWidth() / 2);
        rotate.start();
        rotated = true;
    }
    public Animator unrotateTop(){
        rotated = false;
        AnimatorSet unrotateTop = new AnimatorSet();
        unrotateTop.play(ObjectAnimator.ofFloat(this, "rotationX", 0)).with(ObjectAnimator.ofFloat(this, "scaleY", 1)).with(ObjectAnimator.ofFloat(this, "scaleX", 1)).with(ObjectAnimator.ofFloat(this, "translationY", (-1 * getHeight())));
        return unrotateTop;
    }
    public Animator unrotateBottom(){
        rotated = false;
        AnimatorSet unrotateBottom = new AnimatorSet();
        unrotateBottom.play(ObjectAnimator.ofFloat(this, "rotationX", 0)).with(ObjectAnimator.ofFloat(this, "scaleY", (float).95)).with(ObjectAnimator.ofFloat(this, "scaleX", (float).95)).with(ObjectAnimator.ofFloat(this, "translationY", 0));
        return unrotateBottom;
    }
    public Animator unrotateActive(){
        rotated = false;
        AnimatorSet unrotateActive = new AnimatorSet();
        unrotateActive.play(ObjectAnimator.ofFloat(this, "rotationX", 0)).with(ObjectAnimator.ofFloat(this, "scaleY", 1)).with(ObjectAnimator.ofFloat(this, "scaleX", 1)).with(ObjectAnimator.ofFloat(this, "translationY", 0));
        return unrotateActive;
    }

    public void initPivots(){
        setPivotY(getHeight());
        setPivotX(getWidth() / 2);
    }
    public void drawPath(Location sink){
        path.lineTo(sink.getLongitude(), sink.getLatitude());
        invalidate();
    }
}