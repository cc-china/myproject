package com.my_project.test_custom_date_picker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.util.Measure;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.R;
import com.my_project.test_custom_date_picker.utils.LinearGradintUtils;

import java.text.Format;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by com_c on 2018/4/8.
 * 滚动循环的日期选择器 --父类
 */
@SuppressWarnings({"FieldCanBeLocal", "unused", "SameParameterValue"})//去除感叹号警告的意思
public class WheelPicker<T> extends View {
    private final Context mContext;
    /**
     * Item的Text的颜色和字号大小
     */
    private int mTextColor;
    private int mTextSize;
    /**
     * 字体渐变，开启后越靠近边缘，字体越模糊
     */
    private boolean mIsTextGradual;
    /**
     * 是否循环读取
     */
    private boolean mIsCyclic;
    /**
     * Item显示的一半数量（以中心Item为中轴线，上下两边分别相同的数量）
     * 总显示的数量为 mHalfVisibleItemCount * 2 + 1
     */
    private int mHalfVisibleItemCount;
    /**
     * 输入的一段文字，可以用来测量 mTextMaxWidth
     */
    private String mItemMaximumWidthText;
    /**
     * 选中的Item的Text颜色和字号大小
     */
    private int mSelectedItemTextColor;
    private int mSelectedItemTextSize;
    /**
     * 当前Item的位置
     */
    private int mCurrentItemPosition;
    /**
     * 不同Item之间高度和宽度的间隔
     */
    private int mItemWidthSpace, mItemHeightSpace;
    /**
     * 是否将选中的item放大
     */
    private boolean mIsZoomInSelectedItem;
    /**
     * 是否显示幕布，中央的Item会遮盖一个颜色
     */
    private boolean mIsShowCurtain;
    /**
     * 幕布颜色
     */
    private int mCurtainColor;
    /**
     * 是否显示幕布边框
     */
    private boolean mIsShowCurtainBorder;
    /**
     * 幕布边框的颜色
     */
    private int mCurtainBorderColor;
    /**
     * 指示器文字
     * 会在中心文字后边多绘制一个文字
     */
    private String mIndicatorText;
    /**
     * 指示器文字颜色
     */
    private int mIndicatorTextColor;
    /**
     * 指示器文字大小
     */
    private int mIndicatorTextSize;
    private Paint mPaint;
    /**
     * 颜色线性渐变的工具类
     */
    private LinearGradintUtils mLinearGradint;
    /**
     * 整个控件的可绘制面积
     */
    private Rect mDrawnRect;
    /**
     * 选中item的绘制矩形面积
     */
    private Rect mSelectedItemRect;
    /**
     * mScroller.getCurrX() //获取mScroller当前水平滚动的位置
     * 2 mScroller.getCurrY() //获取mScroller当前竖直滚动的位置
     * 3 mScroller.getFinalX() //获取mScroller最终停止的水平位置
     * 4 mScroller.getFinalY() //获取mScroller最终停止的竖直位置
     * 5 mScroller.setFinalX(int newX) //设置mScroller最终停留的水平位置，没有动画效果，直接跳到目标位置
     * 6 mScroller.setFinalY(int newY) //设置mScroller最终停留的竖直位置，没有动画效果，直接跳到目标位置
     * 滚动，startX, startY为开始滚动的位置，dx,dy为滚动的偏移量, duration为完成滚动的时间
     * 9 mScroller.startScroll(int startX, int startY, int dx, int dy) //使用默认完成时间250ms
     * 10 mScroller.startScroll(int startX, int startY, int dx, int dy, int duration)
     * 返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。
     * mScroller.computeScrollOffset()
     */
    private Scroller mScroller;
    /**
     * 这是一个距离值，从configuration这个类的getScaledTouchSlop()方法中获取的，
     * 从源码看它就是一个固定的位置 --8dp
     */
    private int mTouchSlop;
    /**
     * 数据集合
     */
    private List<T> mDataList;
    /**
     * 最大的一个Item文本的宽高
     */
    private int mTextMaxHeight, mTextMaxWidth;
    /**
     * 整个控件中每个Item矩形的高度
     */
    private int mItemHeight;
    /**
     * 用mDrawRect.getCenterX获取到矩形中点X的位置
     */
    private int mFirstItemDrawX;
    private int mFirstItemDrawY;
    /**
     * 中心的Item绘制的text文字Y轴坐标
     */
    private int mCenterItemDrawnY;
    /**
     * Y轴 scroll滚动的位移
     */
    private int mScrollerOffsetY;
    /**
     * 输入的一段文字,用来测量mTextMaxWidth
     */
    private String mItemMaxMumWidth;
    /**
     * View类中用来获取touch事件时手势的速度
     */
    VelocityTracker mTracker = null;
    /**
     * 是否手动停止滚动
     */
    private boolean mIsAbortScroller;

    private int mTouchDownY;
    /**
     * 最后手指Down事件的Y轴坐标，用于计算拖动距离
     */
    private int mLastDownY;
    /**
     * 此标记的作用是，令mTouchSlop仅在一个滑动过程中生效一次
     */
    private boolean mTouchSlopFlag;

    /**
     * 滚轮滑动时的最小/最大滑动速度
     */
    private int mMaximumVelocity = 12000, mMinimumVelocity = 50;
    /**
     * 最大可以Fling的距离
     */
    private int mMinFlingY;
    private int mMaxFlingY;
    private Format mDataFormat;


    private android.os.Handler mHandler = new android.os.Handler();
    private Runnable mScrollerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                mScrollerOffsetY = mScroller.getCurrY();
                invalidate();
                mHandler.postDelayed(this, 16);
            }
            if (mScroller.isFinished()) {
                if (onWheelChangeListener == null) {
                    return;
                }
                if (mItemHeight == 0) {
                    return;
                }
                //计算位置
                int position = -mScrollerOffsetY / mItemHeight;
                position = fixItemPosition(position);//如果是循环的话修正position值
                Log.d("11111OffsetY", -mScrollerOffsetY + "");
                Log.d("111mItemHeight", mItemHeight + "");
                Log.d("11111mmPosition", mCurrentItemPosition + "");
                Log.d("11111position", position + "");
                if (mCurrentItemPosition != position) {
                    mCurrentItemPosition = position;
                    onWheelChangeListener.onWheelSelected(mDataList.get(position), position);
                }
            }
        }
    };
    private OnWheelChangeListener<T> onWheelChangeListener;

    public WheelPicker(Context context) {
        this(context, null);
    }

    public WheelPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(context, attrs);
        initpaint();//初始化画笔
        mLinearGradint = new LinearGradintUtils(mTextColor, mSelectedItemTextColor);
        mDrawnRect = new Rect();
        mSelectedItemRect = new Rect();
        mScroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public int measureSize(int specMode, int specSize, int size) {
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            return Math.min(specSize, size);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidthMeasureSpec = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec);
        int modeHeightMeasureSpec = MeasureSpec.getMode(heightMeasureSpec);
        int sizeheightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
        int width = mTextMaxWidth + mItemWidthSpace;
        int height = (mTextMaxHeight + mItemHeightSpace) * getVisibilityItemCount();
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureSize(modeWidthMeasureSpec, sizeWidthMeasureSpec, width)
                , measureSize(modeHeightMeasureSpec, sizeheightMeasureSpec, height));
    }

    /**
     * 这里的mMinFlingY和mMaxFlingY 是指日期选择器会不会循环的滚动，
     */
    private void computeFlingLimitY() {
        mMinFlingY = mIsCyclic ? Integer.MIN_VALUE : -(mDataList.size() - 1) * mItemHeight;
        mMaxFlingY = mIsCyclic ? Integer.MAX_VALUE : 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /**
         * 设置整个控件的绘制矩形，如何确定这个控件的宽高，画矩形是传递 l,t,r,b四个参数
         *        经过测试我发现我们在布局文件中设置的margin值不会对自定义view产生任何影响，
         *        想要的间距它不管你是不是自定义的，都可以完美展示，而当我们设定Padding值的时候，
         *        如果我们设置Rect.set(0,0,getWidth(),getHeight())view会显示的特别丑，所以在绘制矩形的时候
         *        特意减去padding值，这样的话下列的值就是：
         * l = getPaddingLeft()
         * t = getPaddingTop()
         * r = getWidth() - getPaddingRight()
         * b = getHeight() - getPaddingBottom()
         * */
        mDrawnRect.set(getPaddingLeft(), getPaddingTop()
                , getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        /**
         * 我们要精确的知道每一个Item高度是多少，这个参数在后边用的非常多，那么应该怎么来计算？
         * 很简单，刚刚我们画出了自定义的View矩形控件，那么它必然是有一个确定的高度的，view.Heigth()
         * 然后，我们这个矩形控件在屏幕可见区域准备要展示多少条Item
         * 例如，屏幕可见区域总共10cm，要显示5条数据
         * 显然，每一个Item高度 = 10/5 = 2cm
         * 这里也是一样的，屏幕可见区域矩形的高度 view.Height我们拿到了
         * 下面来计算要显示几条数据，在res-attrs文件夹中，我们声明了自定义属性，其中有一条halfVisibleItemCount，
         * 翻译过来就是可见Item的一半，很显然，总数就是 halfVisibleItemCount * 2 + 1 ，为什么要加 1 ？
         * 因为这个halfVisibleItemCount说的是可见屏幕从中间砍一半的条数，中间正好中刀的那一条也要加上的
         * */
        mItemHeight = mDrawnRect.height() / getVisibilityItemCount();//整个控件中每个Item矩形的高度
        //这俩应该是 mDrawnRect 矩形的 对角线中心点
        // mPaint.ascent()文字top方向边缘   mPaint.descent()文字bottom方向边缘
        mFirstItemDrawX = mDrawnRect.centerX();
        mFirstItemDrawY = (int) ((mItemHeight - (mPaint.ascent() + mPaint.descent())) / 2);
        /**
         * 这是选中的矩形区域，也是传入  l,t,r,b四个参数
         * l 和 r 与整体的矩形一样的逻辑
         * 需要注意的就是默认选中的矩形 t 和 b 这个参数传什么？
         * 很简单，我们这里说的默认选中Item就是mDrawnRect矩形中从可见屏幕中间砍一半正好砍中
         * 的屏幕最中间的Item，那么，这个矩形距离最上边  top 是多少，就是halfVisibleItemCount * mItemHeight的值
         * so：t = halfVisibleItemCount * mItemHeight的值，
         * b 的值是多少？ 它就是比 t 多了一个Item
         * 所以 b = （halfVisibleItemCount + 1） * mItemHeight
         * */
        mSelectedItemRect.set(getPaddingLeft(), mItemHeight * mHalfVisibleItemCount
                , getWidth() - getPaddingRight(), mItemHeight * (mHalfVisibleItemCount + 1));
        //这个没什么 如果设定循环滚动，就重置一下Item的position
        computeFlingLimitY();
        mCenterItemDrawnY = mFirstItemDrawY + mItemHeight * mHalfVisibleItemCount;
        //滑动了多少Y轴的距离
        mScrollerOffsetY = -mItemHeight * mCurrentItemPosition;
    }

    /**
     * 修正坐标值，让其回到dateList的范围内
     *
     * @param position 修正前的值
     * @return 修正后的值
     */
    private int fixItemPosition(int position) {
        //将数据集 position 限定在0 ~ mDataList.size()-1之间
        if (position < 0) {
            position = mDataList.size() + position % (mDataList.size());
        }
        if (position > mDataList.size() - 1) {
            position = position % mDataList.size();
        }
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式
        if (mIsShowCurtain) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mCurtainColor);
            canvas.drawRect(mSelectedItemRect, mPaint);
        }
        if (mIsShowCurtainBorder) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mCurtainBorderColor);
            canvas.drawRect(mSelectedItemRect, mPaint);
            canvas.drawRect(mDrawnRect, mPaint);
        }
        //当前滑动了几个矩形控件
        int drawnSelectedPos = -mScrollerOffsetY / mItemHeight;
        mPaint.setStyle(Paint.Style.FILL);
        //在最大和最小之间留一个缓冲，多绘制一个矩形控件
        for (int drawDataPos = drawnSelectedPos - mHalfVisibleItemCount - 1;
             drawDataPos <= drawnSelectedPos + mHalfVisibleItemCount + 1; drawDataPos++) {
            int position = drawDataPos;
            if (mIsCyclic) {
                position = fixItemPosition(position);
            } else {
                if (position < 0 || position > mDataList.size() - 1) {
                    continue;
                }
            }

            //在中间位置的Item作为被选中的条目
            if (drawnSelectedPos == drawDataPos) {
                mPaint.setColor(mSelectedItemTextColor);
            } else {
                mPaint.setColor(mTextColor);
            }
            T data = mDataList.get(position);
            //当前Item距离坐标原点的distance
            int itemDrawY = mFirstItemDrawY + (drawDataPos + mHalfVisibleItemCount) * mItemHeight + mScrollerOffsetY;
            //当前Item中心点距离坐标原点的distance
            int distanceY = Math.abs(mCenterItemDrawnY - itemDrawY);
            if (mIsTextGradual) {
                //计算文字颜色渐变
                //文字颜色渐变要设置在透明度上边，否则会被覆盖
                if (distanceY < mItemHeight) {
                    float colorRatio = 1 - (distanceY / (float) mItemHeight);
                    mPaint.setColor(mLinearGradint.getColor(colorRatio));
                } else {
                    mPaint.setColor(mTextColor);
                }

                //透明度
                float alphaRatio;
                if (itemDrawY > mCenterItemDrawnY) {
                    alphaRatio = (mDrawnRect.height() - itemDrawY) /
                            (float) (mDrawnRect.height() - mCenterItemDrawnY);
                } else {
                    alphaRatio = itemDrawY / (float) mCenterItemDrawnY;
                }
                alphaRatio = alphaRatio < 0 ? 0 : alphaRatio;
                mPaint.setAlpha((int) (alphaRatio * 255));
            } else {
                mPaint.setAlpha(255);
                mPaint.setColor(mSelectedItemTextColor);
            }
            //开启此选项，会将越靠近中心的Item字体放大
            if (mIsZoomInSelectedItem) {
                if (distanceY < mItemHeight) {
                    float addedSize = (mItemHeight - distanceY) / (float) (mItemHeight * (mSelectedItemTextSize - mTextSize));
                    mPaint.setTextSize(addedSize + mTextSize);
                } else {
                    mPaint.setTextSize(mTextSize);
                }

            } else {
                mPaint.setTextSize(mTextSize);
            }
            if (mDataFormat != null) {
                canvas.drawText(mDataFormat.format(data), mFirstItemDrawX, itemDrawY, mPaint);
            } else {
                canvas.drawText(data.toString(), mFirstItemDrawX, itemDrawY, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 按下这个touch事件，我们需要分析当前的状态
                 * 1、当前view正在高速滑动中，
                 * 2、当前view在静止状态
                 * */
                if (!mScroller.isFinished()) {
                    //当前view正在高速滑动中，
                    mScroller.abortAnimation();
                    mIsAbortScroller = true;
                } else {
                    //当前view在静止状态，点击了一下Item
                    mIsAbortScroller = false;
                }
                mTracker.clear();
                mTouchDownY = mLastDownY = (int) event.getY();
                mTouchSlopFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 在第一次move，首次按下的时候，滑动的距离要大于View规定的常量才算是滑动，
                 * 但是这样的判断会出现一个问题，我们会来回的拖动view，上下反复的拖动，
                 * 如果只使用距离作为唯一的判断依据，这种情况下就无法展示出来，
                 * 所以我们加一个flag，当我们在move的时候保证逻辑程序不会走进这个判断（即使滑动距离小于view常量）
                 * */
                if (mTouchSlopFlag && Math.abs(mTouchDownY - event.getY()) < mTouchSlop) {
                    break;
                }
                mTouchSlopFlag = false;
                //移动的距离，终点 - 起点   Y轴坐标
                float move = event.getY() - mLastDownY;
                //实时更新Y轴滑动的距离
                mScrollerOffsetY += move;
                mLastDownY = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 判断view是在高速滑动还是静止状态，并且对比收拖动的最后距离与Down时间中的滑动位置
                 * 以此来判断当前view滚动的状态，是滚动还是静止时拖动了几个单位的Item距离
                 * */
                if (!mIsAbortScroller && mTouchDownY == mLastDownY) {
                    //当前view是静止状态，用户点击了Item来选择数据
                    //模拟手指点击view控件
                    performClick();
                    //判断点击的Item是在默认选中Item的上方还是下方
                    if (event.getY() > mSelectedItemRect.bottom) {
                        //点击是默认选中Item的下方，这时view的状态应该是向上滑
                        //判断点击的Item距离默认Item的Y轴值，所以在sctoller.startScroll方法中传的参数dy
                        //是负数
                        int scrollItem = (int) ((event.getY() - mSelectedItemRect.bottom) / mItemHeight + 1);
                        //因为是向上滑的状态，也就是说view的Y轴是从大变小的状态，
                        mScroller.startScroll(0, mScrollerOffsetY, 0, -scrollItem * mItemHeight);
                    } else if (event.getY() < mSelectedItemRect.top) {
                        //在这个判断的范围内，点击的是默认选中Item的上方，这时view的状态应当是向下滑，
                        //也就是说这时候view的Y轴是从小变大的过程，所以在sctoller.startScroll方法中传的参数dy
                        //是正数
                        int scrollItem = (int) ((mSelectedItemRect.top - event.getY()) / mItemHeight + 1);
                        mScroller.startScroll(0, mScrollerOffsetY, 0, scrollItem * mItemHeight);
                    }
                } else {
                    /**
                     * 当前view是高速滑动状态，我们要去判断，
                     * 当前滑动的速度(使用VelocityTracker获取到的真实世界手指滑屏物理速度)有没有我们设定的最小速度值低
                     *     如果比手动设定最小值还低，说明这不是滑动，是用户在点击某一些Item来切换数据源
                     *     如果比它大，说明是高速滑动，走下一步：
                     * */
                    mTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int yVelocity = (int) mTracker.getYVelocity();
                    if (Math.abs(yVelocity) > mMinimumVelocity) {
                        /**
                         * 如果比它大，说明是高速滑动，走下一步：
                         * 在这个时候如果不做任何处理，在用力滑动Item的时候，
                         * 数据并不会随着手指的快速滑动而飞速切换，而是如同界面卡死一样一点一点的移动，
                         * 这样的用户体验非常差，在这里我们使用velocityTracker类拿到物理屏幕上滑动的速度，
                         * 传入scroller类中的fling方法，数据就可以飞速的滑动，
                         * */
                        mScroller.fling(0, mScrollerOffsetY, 0, yVelocity,
                                0, 0, mMinFlingY, mMaxFlingY);
                        //光使用scroller.fling方法  可以正常滑动view实现功能，
                        //但是我们很可能将中间默认选中的item位置滑动成俩个数据源的中间，例如年份1908和1909的中间，
                        //不属于任何一个年份，所以我们要去判断，出现上述问题的时候通过计算返回对应的view滑动distance
                        //所以这里应当使用Scroller.setFinalY方法，将最终位置定死在对应的数据Item上
                        mScroller.setFinalY(mScroller.getFinalY() + computeDistanceToEndPoint(mScroller.getFinalY() % mItemHeight));
                    } else {
                        /**
                         * 如果比手动设定最小值还低，说明这不是滑动，是用户在点击某一些Item来切换数据源
                         * 这样就很简单了，Scroller.startScroll方法滑动就好,在传入dy参数的时候，同样参考Scroll滑动的规则
                         * 中间默认选中的item位置滑动成俩个数据源的中间，例如年份1908和1909的中间，
                         * 不属于任何一个年份，所以我们要去判断，出现上述问题的时候通过计算返回对应的view滑动distance
                         * */
                        mScroller.startScroll(0, mScrollerOffsetY, 0, computeDistanceToEndPoint(mScrollerOffsetY % mItemHeight));
                    }

                }
                mHandler.post(mScrollerRunnable);
                mTracker.recycle();
                mTracker = null;
                break;
        }

        return true;
    }

    private int computeDistanceToEndPoint(int remainder) {
        /**
         * 这里的remainder是滑动的距离对每一个Item宽度取余，
         * ①：它等于0，说明正好是滑动了Item宽度的整数个单位
         * ②：它绝对值大于Item宽度的一半说明我们想要滑动到下一个Item数据源上
         *   这时候需要判断我们是向上滑还是向下滑
         *      2.1:向上滑mScrollerOffsetY就是小于0
         *      2.2:向下滑mScrollerOffsetY就是大于0
         * ③：如果绝对值小于Item一半，说明我们想要滑动到当前的Item数据源上
         *      3.1
         * */
        if (Math.abs(remainder) > mItemHeight / 2) {
            //绝对值大于Item宽度的一半，应当将当前Item移向下一个数据源，也就是说,
            // 当向上滑的时候，在当前Item的基础上再向上滑动一个Item位置，再减去一个Item的宽度
            //当向下滑的时候，在当前Item的基础上再向下滑动一个Item位置，再加上一个Item的宽度
            if (mScrollerOffsetY < 0) {
                //向上滑
                return -mItemHeight - remainder;
            } else {
                //向下滑
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }

    private void initpaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelPicker);
        mTextSize = a.getDimensionPixelSize(R.styleable.WheelPicker_itemTextSize
                , getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        mTextColor = a.getColor(R.styleable.WheelPicker_itemTextColor, Color.BLACK);
        mIsTextGradual = a.getBoolean(R.styleable.WheelPicker_textGradual, true);
        mIsCyclic = a.getBoolean(R.styleable.WheelPicker_wheelCyclic, false);
        mHalfVisibleItemCount = a.getInteger(R.styleable.WheelPicker_halfVisibleItemCount, 2);
        mItemMaximumWidthText = a.getString(R.styleable.WheelPicker_itemMaximumWidthText);
        mSelectedItemTextColor = a.getColor(R.styleable.WheelPicker_selectedTextColor, Color.parseColor("#33aaff"));
        mSelectedItemTextSize = a.getDimensionPixelSize(R.styleable.WheelPicker_selectedTextSize,
                getResources().getDimensionPixelSize(R.dimen.WheelSelectedItemTextSize));
        mCurrentItemPosition = a.getInteger(R.styleable.WheelPicker_currentItemPosition, 0);
        mItemWidthSpace = a.getDimensionPixelSize(R.styleable.WheelPicker_itemWidthSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemWidthSpace));
        mItemHeightSpace = a.getDimensionPixelSize(R.styleable.WheelPicker_itemHeightSpace,
                getResources().getDimensionPixelOffset(R.dimen.WheelItemHeightSpace));
        mIsZoomInSelectedItem = a.getBoolean(R.styleable.WheelPicker_zoomInSelectedItem, true);
        mIsShowCurtain = a.getBoolean(R.styleable.WheelPicker_wheelCurtain, true);
        mCurtainColor = a.getColor(R.styleable.WheelPicker_wheelCurtainColor,
                Color.parseColor("#303d3d3d"));
        mIsShowCurtainBorder = a.getBoolean(R.styleable.WheelPicker_wheelCurtainBorder, true);
        mCurtainBorderColor = a.getColor(R.styleable.WheelPicker_wheelCurtainBorderColor, Color.BLACK);
        mIndicatorText = a.getString(R.styleable.WheelPicker_indicatorText);
        mIndicatorTextColor = a.getColor(R.styleable.WheelPicker_indicatorTextColor, mSelectedItemTextColor);
        mIndicatorTextSize = a.getDimensionPixelSize(R.styleable.WheelPicker_indicatorTextSize, mTextSize);
        a.recycle();
    }


    /**
     * 设置当前选中的列表
     *
     * @Param currentPosition 设置当前位置
     * @Param smoothScroll 是否平滑滚动
     */
    protected void setCurrentPosition(int currentPosition, boolean smoothScroll) {
        if (mCurrentItemPosition == currentPosition) {
            return;
        }
        if (currentPosition > mDataList.size()) {
            currentPosition = mDataList.size() - 1;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        if (smoothScroll) {
            mScroller.startScroll(0, mScrollerOffsetY, 0,
                    (mCurrentItemPosition - currentPosition) * mItemHeight);
            mScroller.setFinalY(mScroller.getFinalY() +
                    computeDistanceToEndPoint(mScroller.getFinalY() % mItemHeight));
            mHandler.post(mScrollerRunnable);
        } else {
            mCurrentItemPosition = currentPosition;
            mScrollerOffsetY = -mItemHeight * mCurrentItemPosition;
            postInvalidate();
            if (onWheelChangeListener != null) {
                onWheelChangeListener.onWheelSelected(mDataList.get(mCurrentItemPosition), mCurrentItemPosition);
            }
        }
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
        if (dataList.size() == 0) {
            return;
        }
        computeTextSize();
        requestLayout();//重绘onMeasure和onLayout方法
        postInvalidate();//重绘onDraw方法
    }

    private void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (mDataList.size() == 0) {
            return;
        }
        //这里使用最大的,防止文字大小超过布局大小。
        mPaint.setTextSize(mSelectedItemTextSize > mTextSize ? mSelectedItemTextSize : mTextSize);
        if (!TextUtils.isEmpty(mItemMaximumWidthText)) {
            mTextMaxWidth = (int) mPaint.measureText(mItemMaximumWidthText);
        } else {
            mTextMaxWidth = (int) mPaint.measureText(mDataList.get(0).toString());
        }
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextMaxHeight = (int) (fontMetrics.bottom - fontMetrics.top);
    }

    private int getVisibilityItemCount() {
        return mHalfVisibleItemCount * 2 + 1;
    }

    /**
     * 设置输入的一段文字,用来测量mTextMaxWidth
     */
    public void setmItemMaximumWidthText(String ItemMaxMumWidth) {
        this.mItemMaxMumWidth = ItemMaxMumWidth;
        requestLayout();
        postInvalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 设置数据集格式
     *
     * @param dataFormat 格式
     */
    public void setDataFormat(Format dataFormat) {
        mDataFormat = dataFormat;
        postInvalidate();
    }

    public Format getDataFormat() {
        return mDataFormat;
    }

    protected void setOnWheelChangeListener(OnWheelChangeListener<T> onWheelChangeListener) {
        this.onWheelChangeListener = onWheelChangeListener;
    }

    public interface OnWheelChangeListener<T> {
        void onWheelSelected(T t, int position);
    }
}
