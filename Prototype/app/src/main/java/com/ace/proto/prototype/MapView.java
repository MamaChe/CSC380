package com.ace.proto.prototype;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
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
    public AnimatorSet rotate;
    public static final float rotationAngle = 26, rotationYScale = (float).5;
    public MapView(Context context) {
        super(context);
        init(context);
    }
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    @Override
    public void onClick(View view){
        ((MapViewGroup)findViewById(R.id.MapViewGroup)).setActiveLayer(view);
    }

    private void init(Context context){
        setPivotY(getHeight());
        rotate = new AnimatorSet();
        float rotationXScale = (float).95;
        rotate.play(ObjectAnimator.ofFloat(this, "rotationX", rotationAngle)).with(ObjectAnimator.ofFloat(this, "scaleY", rotationYScale)).with(ObjectAnimator.ofFloat(this, "scaleX", rotationXScale));
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

}