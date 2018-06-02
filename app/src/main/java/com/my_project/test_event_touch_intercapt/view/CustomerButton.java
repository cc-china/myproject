package com.my_project.test_event_touch_intercapt.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Administrator on 2017\12\18 0018.
 */

@SuppressLint("AppCompatCustomView")
public class CustomerButton extends Button {
    public CustomerButton(Context context) {
        this(context,null);
    }

    public CustomerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("tag","view_onTouchEvent");
//        return super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("tag","view_dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
//        return true;
    }
}
