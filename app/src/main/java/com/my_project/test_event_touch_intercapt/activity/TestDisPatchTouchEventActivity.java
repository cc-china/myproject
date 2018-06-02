package com.my_project.test_event_touch_intercapt.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.my_project.R;
import com.my_project.test_event_touch_intercapt.view.CustomerButton;
import com.my_project.test_event_touch_intercapt.view.CustomerGroupView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\12\18 0018.
 */

public class TestDisPatchTouchEventActivity extends Activity {
    @Bind(R.id.linearlayout)
    FrameLayout linearlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dispatch_touch_event);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        CustomerGroupView a = new CustomerGroupView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = 300;
        layoutParams.height = 300;
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        a.setLayoutParams(layoutParams);
        a.setBackgroundColor(getResources().getColor(R.color.colorAccent));


        CustomerButton button = new CustomerButton(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setText("点击");

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        button.setLayoutParams(params);
        button.setBackgroundColor(Color.YELLOW);
        button.setWidth(200);
        button.setHeight(120);
        a.addView(button);
        linearlayout.addView(a);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("tag","activity_onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("tag","activity_dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
}

