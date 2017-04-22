package com.example.atom.gamylife;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {


    public CustomViewPager(Context context) {

        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (getCurrentItem() != 2) {    //item 2 us the CalendarFragment
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getCurrentItem() != 2) {    //item 2 us the CalendarFragment
            return super.onTouchEvent(event);
        }

        return false;
    }
}