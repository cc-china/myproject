package com.my_project.test_view_custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.my_project.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2017\12\7 0007.
 */

public class CustomTextView extends View {

    private String mTtitleText;
    private int mTitleTextSize;
    private int mTitleTextColor;
    private Paint textPaint;
    private Rect backGround;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 在这里拿到自定义的各种属性
     */
    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomTextView, defStyleAttr, 0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            //拿到对应的自定义属性；
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.CustomTextView_titleText:
                    mTtitleText = array.getString(index);
                    break;
                case R.styleable.CustomTextView_titleTextSize:
                    /*
                    * TypedValue可以把dp换成对应的像素px，调用TypedValue的applyDimension方法
                    * 第一个参数：你要转换什么类型，sp，dp
                    * 第二个参数，转换前的初始值
                    * 第三个参数，我感觉是一个定值，上下文引出的getResources().getDisplayMetrics()
                    * */
                    mTitleTextSize = array.getDimensionPixelSize(index, (int)
                            (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    16, context.getResources().getDisplayMetrics())));
                    break;
                case R.styleable.CustomTextView_titleTextColor:
                    mTitleTextColor = array.getColor(index, Color.BLACK);
                    break;
            }
        }
        //释放资源，通知GC回收
        array.recycle();
        initPaint();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTtitleText = randomText();
            }
        });
    }

    /**
     * 生成一个四位数的随机数
     * a、实例化Random
     * 2、调用random.nextInt（），获取到一个随机数
     * 3、将其存储到集合中，如果这个四位数的随机数不能有重复的数字，可以使用hashSet来存储
     * 4、循环取出元素添加到一起，就成了一个随机数
     */
    private String randomText() {
        Random random = new Random();
        Set<Integer> elementSet = new HashSet<>();
        while (elementSet.size() < 4) {
            int i = random.nextInt(10);
            elementSet.add(i);
        }
        StringBuffer text = new StringBuffer();
        for (Integer i : elementSet) {
            text.append(i + "");
        }
        invalidate();
        return text.toString();
    }

    /**
     * 实例化画笔
     */
    private void initPaint() {
        textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(mTitleTextSize);
        //代表一个矩形，构造方法有L,T,R,B四个参数
        backGround = new Rect();
        textPaint.getTextBounds(mTtitleText, 0, mTtitleText.length(), backGround);
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
            textPaint.setTextSize(mTitleTextSize);
            textPaint.getTextBounds(mTtitleText, 0, mTtitleText.length(), backGround);
            width = (int) (getPaddingLeft() + backGround.width() + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            textPaint.setTextSize(mTitleTextSize);
            textPaint.getTextBounds(mTtitleText, 0, mTtitleText.length(), backGround);
            height = (int) (getPaddingTop() + backGround.height() + getPaddingBottom());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackGround(canvas);
        drawText(canvas);
    }

    /**
     * 画TextView内容
     */
    private void drawText(Canvas canvas) {
        textPaint.setColor(mTitleTextColor);
        canvas.drawText(mTtitleText
                , getMeasuredWidth() / 2 - backGround.width() / 2
                , getMeasuredHeight() / 2 + backGround.height() / 2
                , textPaint);
        textPaint.setColor(Color.BLUE);
    }

    /**
     * 画TextView的背景
     */
    private void drawBackGround(Canvas canvas) {
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), textPaint);
    }
}
