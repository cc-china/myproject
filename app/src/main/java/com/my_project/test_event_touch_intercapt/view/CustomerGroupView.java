package com.my_project.test_event_touch_intercapt.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017\12\18 0018.
 */

public class CustomerGroupView extends RelativeLayout {
    public CustomerGroupView(Context context) {
        this(context, null);
    }

    public CustomerGroupView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("tag","viewGroup_dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("tag","viewGroup_onTouchEvent");
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("tag","viewGroup_onInterceptTouchEvent");
//        return super.onInterceptTouchEvent(ev);
        return false;
    }
}
