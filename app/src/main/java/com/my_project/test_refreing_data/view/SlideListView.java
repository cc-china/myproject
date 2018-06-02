package com.my_project.test_refreing_data.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.iflytek.cloud.resource.Resource;
import com.my_project.R;

import java.util.zip.Inflater;

/**
 * Created by com_c on 2018/1/31.
 */

public class SlideListView extends LinearLayout {

    private static final int TAN = 2;
    private int mHolderWidth = 90;
    private float mLastX = 0;
    private float mLastY = 0;
    private Scroller mScroller;

    public SlideListView(Context context) {
        super(context);
    }

    public SlideListView(Context ctx, View view) {
        super(ctx);
        initView(ctx, view);
    }

    private void initView(Context ctx, View view) {
        mScroller = new Scroller(ctx);
        View view1 = LayoutInflater.from(ctx).inflate(R.layout.view_slide, this);
        LinearLayout linearLayout = (LinearLayout) view1.findViewById(R.id.view_content);
        if (view != null) {
            linearLayout.addView(view);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float startX = 0;
        float startY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 第一个判断是从右往左滑，
                 * 第二个判断是用水平移动的距离和垂直移动的距离做对比
                 * 当垂直移动的距离大于水平移动的距离时，说明不是侧滑，用户是在上下滑动
                 * */
                float x = event.getX();
                float y = event.getY();
                float deltaX = x - mLastX;
                float deltaY = y - mLastY;
                mLastX = x;
                mLastY = y;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }
                if (deltaX != 0) {
                    float newScrollX = getScrollX() - deltaX;
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mHolderWidth) {
                        newScrollX = mHolderWidth;
                    }
                    Log.e("x",x+"");
                    Log.e("y",y+"");
                    Log.e("deltaX",deltaX+"");
                    Log.e("deltaY",deltaY+"");
                    Log.e("mLastX",mLastX+"");
                    Log.e("mLastY",mLastY+"");
                    Log.e("getScrollX()",getScrollX()+"");
                    this.scrollTo((int)newScrollX,0);
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
