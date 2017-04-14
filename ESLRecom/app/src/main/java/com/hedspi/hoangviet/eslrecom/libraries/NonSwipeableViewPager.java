package com.hedspi.hoangviet.eslrecom.libraries;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {
    private boolean isSwipableLeft = false;
    private boolean isSwipableRight = false;
    private float initialXValue;

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages

        if(this.IsSwipeAllowed(event))
            return super.onInterceptTouchEvent(event);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages

        if(this.IsSwipeAllowed(event))
            return super.onTouchEvent(event);
        else
            return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                Log.d("diffX",""+diffX);
                if (diffX > 0 && !isSwipableLeft ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 0 && !isSwipableRight ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }


    public boolean isSwipableLeft() {
        return isSwipableLeft;
    }

    public void setSwipableLeft(boolean swipableLeft) {
        isSwipableLeft = swipableLeft;
    }

    public boolean isSwipableRight() {
        return isSwipableRight;
    }

    public void setSwipableRight(boolean swipableRight) {
        isSwipableRight = swipableRight;
    }
}
