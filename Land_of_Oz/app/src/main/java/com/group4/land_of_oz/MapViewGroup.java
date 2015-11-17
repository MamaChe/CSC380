package com.group4.land_of_oz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.group4.land_of_oz.domain.Floor;
import com.group4.land_of_oz.domain.GraphNode;
import com.group4.land_of_oz.domain.Label;
import com.group4.land_of_oz.domain.Location;
import com.group4.land_of_oz.persistence.FloorDAO;
import com.group4.land_of_oz.persistence.LabelDAO;
import com.group4.land_of_oz.persistence.LocationDAO;

import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;

/**
 * Created by ace on 10/15/15.
 */
public class MapViewGroup extends RelativeLayout implements Gui, ScaleGestureDetector.OnScaleGestureListener {
    float downX, downY, trueDownY, spaceForEach = 0;
    boolean rotated = false, zoomedOut = true, isClick = false, isScalingMotion = false;
    int activeLayer;
    long timeDown;
    ScaleGestureDetector scaleGestureDetector;
    private AnimatorSet rotate, undoScroll;

    /*
    Constructors
     */
    public MapViewGroup(Context context){
        super(context);
        activeLayer = 0;
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }
    public MapViewGroup(Context context, AttributeSet attrs){
        super(context, attrs);
        activeLayer = 0;
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    //called by view shift button to toggle between single or multi-floor view
    public void rotate(){
        initializeAnimatorSets();
        if(rotated){
            correctedUnrotate();
            rotated = false;
        }else{
            if(spaceForEach == (float)0.0){
                spaceForEach = (float) Math.cos((double) MapView.rotationAngle) * MapView.rotationYScale * getHeight();
            }
            rotate.start();
            rotated = true;
            updateLayerinGui();
            invalidate();
        }
    }
    private void correctedUnrotate(){
        updateLayerByScroll();
        unrotate();
    }
    public void updateLayerByScroll(){
        float spaceForEach = (float)(Math.cos((double)MapView.rotationAngle) * MapView.rotationYScale * getHeight());
        int layerShift = Math.round(getScrollY()  / spaceForEach);
        activeLayer = layerShift;
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

    public void updateLayerinGui(){
        Resources res = getResources();
        ((TextView) getRootView().findViewById(R.id.floorTextView)).setText(res.getStringArray(R.array.floors_array)[getChildCount() - activeLayer - 1]);
        if(rotated) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                float relativeScale = Math.min((float) 1.08, Math.max(1, (float) (1.13 - 2 * Math.abs((getScrollY() - (getChildCount() - i - 1) * spaceForEach) / (spaceForEach * getChildCount())))));
                view.setScaleX(relativeScale * MapView.rotationXScale);
                view.setScaleY(relativeScale * MapView.rotationYScale);
            }
            //System.out.println(getScrollY());
        }
    }

    //Floor Change methods
    public void incrementActiveLayer(){
        if(activeLayer < getChildCount() -1 && !rotated) {
            initializeAnimatorSets();
            activeLayer++;
            updateLayerinGui();
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
            updateLayerinGui();
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
            rotationAnimations[2 * i] = new ObjectAnimator().ofFloat(getChildAt(getChildCount() - i - 1), "translationY", (float) (i * spaceForEach - (getHeight() - spaceForEach)/2  ));//the "getChildCount() - i - 1" bit reverses the ordering of the layers. Good candidate for refactoring
            rotationAnimations[2 * i +1] = ((MapView)getChildAt(getChildCount() - i - 1)).rotate;
        }
        for(int i = 0; i < rotationAnimations.length - 1; i ++){
            rotate.play(rotationAnimations[i]).with(rotationAnimations[i+1]);
        }
        rotate.play(rotationAnimations[0]).with(new ObjectAnimator().ofInt(this, "scrollY", (int) spaceForEach * activeLayer));

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
        if(event.getPointerCount()<=1){
            float currentY = event.getY();
            float currentX = event.getX();
            int scrollByY, scrollByX;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = trueDownY = event.getY();
                    downX = event.getX();
                    timeDown = System.nanoTime();
                    isClick = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (rotated) {
                        float spaceForEach = (float) (Math.cos((double) MapView.rotationAngle) * MapView.rotationYScale * getHeight());
                        scrollByY = (int) (downY - currentY);
                        scrollByX = (int) (downX - currentX);
                        if ((scrollByY < 0 && getScrollY() > spaceForEach/3)//scrolling up
                                || (scrollByY > 0 && getScrollY() < (getChildCount() - 1) * spaceForEach)) //scrolling down
                        {
                            scrollBy(0, scrollByY);
                        }
                        if (scrollByX > 1 || scrollByY > 1) {
                            isClick = false;
                        }
                        updateLayerByScroll();
                        updateLayerinGui();
                    } else if (!zoomedOut) {
                        scrollByX = (int) (downX - currentX);
                        scrollByY = (int) (downY - currentY);
                        for (int i = 0; i < getChildCount(); i++) {
                            getChildAt(i).scrollBy(scrollByX, scrollByY);
                        }
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
                    if(isClick && rotated && System.nanoTime() - timeDown < 150000000){
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
        ((MapView)getChildAt(source.getFloor().getLevel())).setPathStart(source);
        for(int i = 1; i < locations.size();i ++){
            sink = locations.get(i);
            if(source.getFloor().getLevel() == sink.getFloor().getLevel()){
                ((MapView)getChildAt(source.getFloor().getLevel())).drawPath(sink);
            }else{
                ((MapView)getChildAt(sink.getFloor().getLevel())).setPathStart(sink);
            }
            source = sink;
        }
    }

    public void illustrateEdge(GraphNode node, GraphNode neighbor) throws IOException {
        Location nodeLocation, neighborLocation;
        LocationDAO locationDAO= new LocationDAO(getContext());
        nodeLocation = locationDAO.findById(node.getId());
        neighborLocation= locationDAO.findById(neighbor.getId());
        if(nodeLocation == null){
            throw new IOException("Location null in the illustrateNode, node/location id:"+node.getId());
        }
        if(neighborLocation == null) {
            throw new IOException("Location null in the illustrateNode, node/location id:"+neighbor.getId());
        }
        if(nodeLocation.getFloor() == null || neighborLocation.getFloor() == null){
            throw new IOException("Floor null in the location, node/location id:"+nodeLocation.getId());
        }
        long nodeLevel = nodeLocation.getFloor().getId();
        long neighborLevel = neighborLocation.getFloor().getId();
        if(nodeLocation.getFloor().getLevel() == neighborLocation.getFloor().getLevel()) {
            MapView view;
            switch((int)nodeLevel){
                case(0):
                    view = (MapView) getChildAt(0);
                    break;
                case(1):
                    view = (MapView) getChildAt(0);
                    break;
                case(2):
                    view = (MapView) getChildAt(1);
                    break;
                case(3):
                    view = (MapView) getChildAt(1);
                    break;
                case(4):
                    view = (MapView) getChildAt(2);
                    break;
                case(5):
                    view = (MapView) getChildAt(3);
                    break;
                case(6):
                    view = (MapView) getChildAt(4);
                    break;
                case(7):
                    view = (MapView) getChildAt(5);
                    break;
                case(8):
                    view = (MapView) getChildAt(6);
                    break;
                case(9):
                    view = (MapView) getChildAt(7);
                    break;
                default:
                    view = (MapView) getChildAt((int)nodeLevel);
            }
            if(view == null){
                System.out.println(nodeLevel + "drawing edges" + getChildCount());
            }else {
                view.setPathStart(nodeLocation);
                view.drawPath(neighborLocation);
            }
        }
    }
    public void illustrateNode(GraphNode node) throws IOException {
        Location nodeLocation;
        LocationDAO locationDAO= new LocationDAO(getContext());
        nodeLocation = locationDAO.findById(node.getId());
        if (nodeLocation==null) throw new IOException("Location null in the illustrateNode, node/location id:"+node.getId());
        long nodeLevel = nodeLocation.getFloor().getId();
        MapView view;
        switch((int)nodeLevel){
            case(0):
                view = (MapView) getChildAt(0);
                break;
            case(1):
                view = (MapView) getChildAt(1);
                break;
            case(2):
                view = (MapView) getChildAt(2);
                break;
            case(3):
                view = (MapView) getChildAt(3);
                break;
            case(4):
                view = (MapView) getChildAt(4);
                break;
            case(5):
                view = (MapView) getChildAt(5);
                break;
            default:
                view = (MapView) getChildAt((int)nodeLevel);
        }
        if(view == null){
            System.out.println(nodeLevel + " error " + getChildCount());
        }else {
            view.illustrateNode(nodeLocation, Long.toString(node.getId()));
        }
    }
}
