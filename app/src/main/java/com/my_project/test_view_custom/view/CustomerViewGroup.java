package com.my_project.test_view_custom.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017\12\14 0014.
 */

public class CustomerViewGroup extends LinearLayout {

    // 记录手指上次触摸的坐标
    private float mLastPointX;
    private float mLastPointY;

    //用于识别最小的滑动距离
    private int mSlop;

    // 记录被拖拽之前 child 的位置坐标
    private float mDragViewOrigX;
    private float mDragViewOrigY;

    //声明状态，分为空闲和拖拽中
    enum Status {
        IDLE, DRAGGING
    }

    Status mCurrentStatu;

    // 用于标识正在被拖拽的 child，为 null 时表明没有 child 被拖拽
    private View mDragView;

    public CustomerViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public CustomerViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //一个定值，代表过了这个final值用户的滑动手势才被判定是有效的
        mSlop = ViewConfiguration.getWindowTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            //取出对应的children --View
            View view = getChildAt(i);
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) view.getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i) {
                case 0:
                    cl = 100;
                    ct = 100;
                    break;
                case 1:
                    cl = 200;
                    ct = 200;

                    break;
                case 2:
                    cl = 300;
                    ct = 300;
                    break;
            }
            cr = cl + viewWidth;
            cb = viewHeight + ct;
            view.layout(cl, ct, cr, cb);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isPointOnViews(event)) {
                    //标记状态为触摸，并记录上次触摸坐标
                    mCurrentStatu = Status.DRAGGING;
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //View位置移动的偏移量，
                int deltaX = (int) (event.getX() - mLastPointX);
                int deltaY = (int) (event.getY() - mLastPointY);
                if (mCurrentStatu == Status.DRAGGING && mDragView != null
                        && (Math.abs(deltaX) > mSlop || Math.abs(deltaY) > mSlop)) {
                    //如果符合条件则对被拖拽的 child 进行位置移动
                    ViewCompat.offsetLeftAndRight(mDragView, deltaX);
                    ViewCompat.offsetTopAndBottom(mDragView, deltaY);
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                //标记状态为空闲
                if (mCurrentStatu == Status.DRAGGING) {
                    if (mDragView != null) {
                        /**
                         * 设置View回弹的效果
                         * */
//                        ValueAnimator valueAnimatorX = ValueAnimator.ofFloat(mDragView.getX(), mDragViewOrigX);
//                        valueAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                mDragView.setX((float) animation.getAnimatedValue());
//                            }
//                        });
//                        ValueAnimator valueAnimatorY = ValueAnimator.ofFloat(mDragView.getY(), mDragViewOrigY);
//                        valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                mDragView.setY((float) animation.getAnimatedValue());
//                            }
//                        });
//                        AnimatorSet animatorSet = new AnimatorSet();
//                        animatorSet.play(valueAnimatorX).with(valueAnimatorY);
//                        animatorSet.addListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                mDragView = null;
//                            }
//                        });
//                        animatorSet.start();
                    } else {
                        mDragView = null;
                    }
                    mCurrentStatu = Status.IDLE;
                }
                break;
        }
        return true;
    }


    /**
     * 判断触摸的位置有没有落在child身上
     */
    private boolean isPointOnViews(MotionEvent event) {
        boolean result = false;
        Rect rect = new Rect();
        /*
        * 可以看到，基本的拖拽的功能实现了，但是有个细节需要优化，
        * 当 3 个 child 显示重叠时，触摸它的公共区域，总是最底层的 child 被响应，
        * 这有点反人类，正常的操作应该是最上层的最先被响应。那么怎么优化呢？
        * 在上面代码中 mDragView 用来标记可以被拖拽的 child，
        * 我们在 isPointOnViews() 方法中找到最先适配的 child 然后赋值，
        * 但是由于 FrameLayout 的特性，最上面的 child 其实在 ViewGroup 的索引位置最靠后。
        * 因此，我们可以做一小小改动就能修正这个问题，那就是遍历 children 的时候,逆序进行。
        * 这样先从顶层检查找到最适配触摸位置的地方
        * */
//        for (int i = 0; i < getChildCount(); i++) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            rect.set((int) view.getX(),
                     (int) view.getY(),
                    (int) view.getX() + (int) view.getWidth(),
                    (int) view.getY() + view.getHeight());
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                //标记被拖拽的child
                mDragView = view;
                result = true;
                //实时记录拖拽的View位置
                mDragViewOrigX = mDragView.getX();
                mDragViewOrigY = mDragView.getY();
                break;
            }
        }

        return result && mCurrentStatu != Status.DRAGGING;
    }
}
