package com.my_project.utils;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017\11\15 0015.
 */

public class MyViewPage extends ViewPager {
    private boolean noScroll = false;

    public MyViewPage(Context context) {
        super(context);
    }

    public MyViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNoScroll(boolean flag) {
        this.noScroll = flag;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (noScroll) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noScroll){
            return false;
        }else {
        return super.onTouchEvent(ev);
        }
    }
}
