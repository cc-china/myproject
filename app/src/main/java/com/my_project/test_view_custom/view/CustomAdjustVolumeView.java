package com.my_project.test_view_custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.my_project.R;

/**
 * Created by Administrator on 2017\12\13 0013.
 */

public class CustomAdjustVolumeView extends View {

    private Context ctx;
    /**
     * 第一圈的颜色
     */
    private int mFirstColor;

    /**
     * 第二圈的颜色
     */
    private int mSecondColor;
    /**
     * 圈的宽度
     */
    private int mCircleWidth;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前进度
     */
    private int mCurrentCount = 3;

    /**
     * 中间的图片
     */
    private Bitmap mImage;
    /**
     * 每个块块间的间隙
     */
    private int mSplitSize;
    /**
     * 个数
     */
    private int mCount;

    private Rect mRect;

    public CustomAdjustVolumeView(Context context) {
        this(context, null);
    }

    public CustomAdjustVolumeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.ctx = context;
    }

    public CustomAdjustVolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //取出自定义的属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomAdjustVolumeView, defStyleAttr, 0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            //根据自定义属性key获取到xml值
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.CustomAdjustVolumeView_firstColor:
                    mFirstColor = array.getColor(index, Color.GREEN);
                    break;
                case R.styleable.CustomAdjustVolumeView_secondColor:
                    mSecondColor = array.getColor(index, Color.GRAY);
                    break;
                case R.styleable.CustomAdjustVolumeView_circleWidth:
                    mCircleWidth = array.getDimensionPixelSize(index, (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics())));
                    break;
                case R.styleable.CustomAdjustVolumeView_dotCount:
                    mCount = array.getInt(index, 20);
                    break;
                case R.styleable.CustomAdjustVolumeView_splitSize:
                    mSplitSize = array.getInt(index, 20);
                    break;
                case R.styleable.CustomAdjustVolumeView_bg:
                    mImage = BitmapFactory.decodeResource(getResources(), array.getResourceId(index, 0));
                    break;
            }
        }
        array.recycle();
        initPoint();
    }

    private void initPoint() {
        //实例化画笔，
        mPaint = new Paint();
        //画矩形
        mRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * MeasureSpec的mode共有三种模式：
         * MeasureSpec.EXACTLY---设置了明确的值或者是MATCH_PARENT
         * MeasureSpec.AT_MOST ---表示限制在一个最大值内，多数用来表示WRAP_CONTENT
         * MeasureSpec.UNSPECIFIED -- 表示布局想要多大就要多大，很少使用
         * */
        //先拿到宽高和模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        //手动计算view的宽高，view的宽一般包括：左间距 + TextView本身宽度 + 右间距
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (getPaddingLeft() + mImage.getWidth() + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (getPaddingTop() + mImage.getHeight() + getPaddingBottom());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStrokeWidth(mCircleWidth);//设置圆环宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置线段形状为圆头
        mPaint.setStyle(Paint.Style.STROKE);//设置为空心圆，其实就是圆环、
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth / 2;// 半径
        /**
         * 画圆块
         */
        drawRect(canvas, centre, radius);
        /**
         * 计算内切正方形的位置
         */
        int relRadius = radius - mCircleWidth / 2;//获取内切圆的半径
        /**
         * 内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        /**
         * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (mImage.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
            mRect.right = (int) (mRect.left + mImage.getWidth());
            mRect.bottom = (int) (mRect.top + mImage.getHeight());
        }
        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    /**
     * 根据参数画出每个小块
     */
    private void drawRect(Canvas canvas, int centre, int radius) {
        //计算需要画的代表音量的小滑块占整个圆周长比例
        float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;
        //用来定义圆弧形状和大小的界限
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        //设置圆环颜色--这是所有的音量，
        mPaint.setColor(mFirstColor);
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }

        //设置圆环颜色--这是当前的音量，
        mPaint.setColor(mSecondColor);
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
    }

    private int xDown, xUp;

    /**
     * 添加触摸监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown) {
                    //下滑
                    up();
                } else {
                    //上滑
                    down();
                }
                break;
        }
        return true;
    }

    /**
     * mCurrentCount - a,但是最低是1
     */
    private void down() {
        if (mCurrentCount == 1) {
            Toast.makeText(ctx, "音量已经最低", Toast.LENGTH_SHORT).show();
        } else {
            mCurrentCount--;
            Toast.makeText(ctx, mCurrentCount + "", Toast.LENGTH_SHORT).show();
            invalidate();
        }

    }

    /**
     * mCurrentCount + a，最高不能超过总音量
     */
    private void up() {
        if (mCurrentCount == mCount) {
            Toast.makeText(ctx, "音量已经最大", Toast.LENGTH_SHORT).show();
        } else {
            mCurrentCount++;
            Toast.makeText(ctx, mCurrentCount + "", Toast.LENGTH_SHORT).show();
            invalidate();
        }

    }
}
