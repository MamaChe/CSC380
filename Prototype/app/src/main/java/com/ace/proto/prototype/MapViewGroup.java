package com.ace.proto.prototype;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by ace on 10/15/15.
 */
public class MapViewGroup extends RelativeLayout implements Gui, ScaleGestureDetector.OnScaleGestureListener {
    float downX, downY, trueDownY;
    boolean rotated = false, zoomedOut = true, isClick = false, isPan = false;
    int activeLayer;
    ScaleGestureDetector scaleGestureDetector;
    private AnimatorSet rotate, undoScroll;

    /*
    Constructors
     */
    public MapViewGroup(Context context){
        super(context);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }
    public MapViewGroup(Context context, AttributeSet attrs){
        super(context, attrs);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    //called by view shift button to toggle between single or multi-floor view
    public void rotate(){
        initializeAnimatorSets();
        if(rotated){
            correctedUnrotate();
            rotated = false;
        }else{
            rotate.start();
            rotated = true;
        }
    }
    private void correctedUnrotate(){
        updateLayerByScroll();
        unrotate();
    }
    public void updateLayerByScroll(){
        float spaceForEach = (float)(Math.cos((double)MapView.rotationAngle) * MapView.rotationYScale * getHeight());
        int layerShift =Math.round(getScrollY()  / spaceForEach);
        activeLayer += layerShift;
    }
    public void unrotate(){
        initializeAnimatorSets();
        undoScroll.start();
        int childCount = getChildCount();
        Animator[] anims = new Animator[childCount];
        for(int i  = 0; i < childCount; i++) {
            if(i < activeLayer){
                anims[i]=((MapView)getChildAt(childCount - i - 1)).unrotateTop();
            }else if(i == activeLayer){
                anims[i]=((MapView)getChildAt(childCount - i - 1)).unrotateActive();
            }else{
                anims[i]=((MapView)getChildAt(childCount - i - 1)).unrotateBottom();
            }
        }
        AnimatorSet temp = new AnimatorSet();
        for(int i = 0; i < childCount - 1; i ++){
            temp.play(anims[i]).with(anims[i+1]);
        }
        temp.start();
    }


    //Floor Change methods
    public void incrementActiveLayer(){
        if(activeLayer < getChildCount() -1 && !rotated) {
            initializeAnimatorSets();
            activeLayer++;
            unrotate();
        }else{
            float spaceForEach = (float) Math.cos((double) MapView.rotationAngle) * MapView.rotationYScale * getHeight();
            if(rotated && activeLayer + getScrollY()/ spaceForEach + .5 < getChildCount()-1){
                new ObjectAnimator().ofInt(this, "scrollY", getScrollY() + Math.round(spaceForEach)).start();
            }
        }
    }
    public void decrementActiveLayer(){
        if(activeLayer > 0 && !rotated) {
            initializeAnimatorSets();
            activeLayer--;
            unrotate();
        }else{
            float spaceForEach = (float) Math.cos((double) MapView.rotationAngle) * MapView.rotationYScale * getHeight();
            if(rotated && activeLayer + getScrollY()/ spaceForEach - .5 > 0){
                new ObjectAnimator().ofInt(this, "scrollY", getScrollY() - Math.round(spaceForEach)).start();
            }
        }
    }

    //Define Animations
    public void initializeAnimatorSets() {
        rotate = new AnimatorSet();
        undoScroll = new AnimatorSet();
        Animator[] rotationAnimations = new Animator[getChildCount() * 2];

        //set rotate
        float spaceForEach = (float)(Math.cos((double)MapView.rotationAngle) * MapView.rotationYScale * getHeight());
        for(int i = 0; i < getChildCount(); i++){
            ((MapView)getChildAt(i)).initPivots();
            rotationAnimations[2 * i] = new ObjectAnimator().ofFloat(getChildAt(getChildCount() - i - 1), "translationY", (float) ((i - activeLayer) * spaceForEach - (getHeight() - spaceForEach)/2  ));//the "getChildCount() - i - 1" bit reverses the ordering of the layers. Good candidate for refactoring
            rotationAnimations[2 * i +1] = ((MapView)getChildAt(getChildCount() - i - 1)).rotate;
        }
        for(int i = 0; i < rotationAnimations.length - 1; i ++){
            rotate.play(rotationAnimations[i]).with(rotationAnimations[i+1]);
        }

        //set undoScroll
        undoScroll.play(ObjectAnimator.ofInt(this, "scrollY", 0));
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isClick = true;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (isClick) {
//                    rotate();
//                    return false;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                isClick = false;
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
    @Override
     public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        if(isPan){
            isPan = false;
        }else {
            float currentY = event.getY();
            float currentX = event.getX();
            int scrollByY, scrollByX;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = trueDownY = event.getY();
                    downX = event.getX();
                    isClick = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (rotated) {
                        float spaceForEach = (float) (Math.cos((double) MapView.rotationAngle) * MapView.rotationYScale * getHeight());
                        scrollByY = (int) (downY - currentY);
                        scrollByX = (int) (downX - currentX);
                        if ((scrollByY > 0 && scrollByY + getScrollY() < (getChildCount() - 1 - activeLayer) * spaceForEach)//scrolling up
                                || (scrollByY < 0 && scrollByY + getScrollY() > -1 * activeLayer * spaceForEach)) //scrolling down
                        {
                            scrollBy(0, scrollByY);
                        }
                        if (scrollByX > 1 || scrollByY > 1) {
                            isClick = false;
                        }

                    } else if (!zoomedOut) {
                        /*scrollByX = (int) (downX - currentX);
                        scrollByY = (int) (downY - currentY);
                        for (int i = 0; i < getChildCount(); i++) {
                            getChildAt(i).scrollBy(scrollByX, scrollByY);
                        }*/
                    } else {
                        //this part handles the draging of unrotated mapviews
                        if (trueDownY - currentY <= 0 && activeLayer > 0) {
                            View child = getChildAt(getChildCount() - activeLayer);
                            child.setTranslationY(currentY - trueDownY - getHeight());
                        } else if (trueDownY - currentY >= 0 && activeLayer < getChildCount() - 1) {
                            View child = getChildAt(getChildCount() - activeLayer - 1);
                            child.setTranslationY(currentY - trueDownY);
                        } else {
                            unrotate();
                        }
                    }
                    downY = currentY;
                    downX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!rotated && zoomedOut) {
                        double threshold = getHeight() * .2;
                        if (trueDownY - currentY > threshold) {
                            incrementActiveLayer();
                        } else if (currentY - trueDownY > threshold) {
                            decrementActiveLayer();
                        } else {
                            unrotate();
                        }
                    } else if (false) {
                        rotate();
                        isClick = false;
                    }
            }
        }
        return true;
    }
    public boolean onScaleBegin(ScaleGestureDetector detector){
        return true;
    }
    public boolean onScale(ScaleGestureDetector detector){
        if(!rotated) {
            if (detector.getScaleFactor() > 1.2 || detector.getScaleFactor() < .8) {
                isPan = true;
            }
            float scale = getChildAt(0).getScaleX() * detector.getScaleFactor();
            if (scale > 1) {
                zoomedOut = false;
                setChildrenScale(scale);
            } else if (scale == 1) {
                zoomedOut = true;
                setChildrenScale(scale);
            } else {
                zoomedOut = true;
            }
        }
        return true;
    }
    public void onScaleEnd(ScaleGestureDetector detector){
        unrotate();
    }
    private void setChildrenScale(float scale){
        for(int i = 0;  i < getChildCount(); i++){
            getChildAt(i).setScaleX(scale);
            getChildAt(i).setScaleY(scale);
            //setScaleX(scale);
            //setScaleY(scale); 
        }
    }
    public void setActiveLayer(View view){
//        int layer = getChildCount() - indexOfChild(view) - 1;
//        if(rotated && layer >= 0 && layer < getChildCount()){
//            activeLayer = layer;
//            undoScroll();
//            rotated = false;
//        }
        rotate();
    }

    @Override
    public void drawPath(List<Location> locations) {
        Location source = locations.get(0), sink;
        for(int i = 0; i < getChildCount(); i++){
            ((MapView)getChildAt(i)).resetPath();
        }
        ((MapView)getChildAt(source.getFloor())).setPathStart(source);
        for(int i = 1; i < locations.size();i ++){
            sink = locations.get(i);
            if(source.getFloor() == sink.getFloor()){
                ((MapView)getChildAt(source.getFloor())).drawPath(sink);
            }else{

                ((MapView)getChildAt(sink.getFloor())).setPathStart(sink);
            }
            source = sink;
        }
    }


}
