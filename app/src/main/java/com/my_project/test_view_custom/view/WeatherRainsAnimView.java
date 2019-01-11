package com.my_project.test_view_custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.my_project.R;

/**
 * Created by Administrator on 2018\12\5 0005.
 */

public class WeatherRainsAnimView extends View {

    private Paint mCloudPaint;
    private Path mLeftCloudPath;
    private float mLeftCloud_x;
    private float mLeftCloud_y;
    private float mLeftCloud_radius;
    private int mRightCloud_x;
    private int mRightCloud_y;
    private int mRightCloud_radius;
    private float mDistanceCloudsBetween;
    private Matrix mMatrix;
    private int mLeftCloudAnimatorPlayTime;
    private int mRightCloudAnimatorPlayTime;
    private ValueAnimator mLeftCloudAnimator;
    private ValueAnimator mRightCloudAnimator;
    private float mLeftCloudAnimatorValue;
    private float mRightCloudAnimatorValue;

    public WeatherRainsAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //初始化云的画笔
        mCloudPaint = new Paint();
        mCloudPaint.setColor(getResources().getColor(R.color.color_B0));
        mCloudPaint.setStrokeWidth(5);
        mCloudPaint.setStyle(Paint.Style.FILL);

        //初始化左边云的底端,就是一个椭圆
        RectF mLeftCloudRect = new RectF();
        mLeftCloudRect.set(150, 150, 550, 250);
        mLeftCloudPath = new Path();
        /**
         * 给Path路径添加画椭圆的函数
         * 第一个参数是整体椭圆的  l，t，r，b 参数
         * 第二、三个参数是圆角的 X轴 、Y轴 半径
         * 第四个参数是Path路径的绘制方向，顺时针和逆时针方向
         * */
        mLeftCloudPath.addRoundRect(mLeftCloudRect, 80, 80, Path.Direction.CCW);


        /**
         * 云的左边小云
         * 云的半径，圆心
         * */
        mLeftCloud_x = (550 - 150) / 2 / 5 * 2 + (550 - 150) / 2 / 5 * 3 / 2 + 150;
        mLeftCloud_y = 150;
        mLeftCloud_radius = (550 - 150) / 2 / 5 * 3 / 2;
        //云的上部分，左边半圆
        mLeftCloudPath.addCircle(mLeftCloud_x, mLeftCloud_y, mLeftCloud_radius, Path.Direction.CCW);

        /**
         * 云的右边小云
         * 云的半径，圆心
         * */
        mRightCloud_x = (550 - 150) / 2 + 150 + (550 - 150) / 2 / 6 * 5 / 2 - 10;
        mRightCloud_y = 150;
        mRightCloud_radius = (550 - 150) / 2 / 6 * 5 / 2 + 10;
        //云的上部分，右边半圆
        mLeftCloudPath.addCircle(mRightCloud_x, mRightCloud_y, mRightCloud_radius, Path.Direction.CCW);
        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /**
         * 左边云和右边云相差 云长度的 3/4
         * */
//        mDistanceCloudsBetween = w / 4 * 3;
        float centerX = w / 2; //view's center x coordinate
        float minSize = Math.min(w, h); //get the min size

        //************************compute left cloud**********************

        float leftCloudWidth = minSize / 2.5f; //the width of cloud
        float leftCloudBottomHeight = leftCloudWidth / 3f; //the bottom height of cloud
        float leftCloudBottomRoundRadius = leftCloudBottomHeight; //the bottom round radius of cl
        mDistanceCloudsBetween = leftCloudBottomRoundRadius / 2;
        setupAnimator();
    }

    private void setupAnimator() {
        mLeftCloudAnimatorPlayTime = 0;
        mRightCloudAnimatorPlayTime = 0;
        mLeftCloudAnimator = ValueAnimator.ofFloat(1, 0, -1);
        mLeftCloudAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLeftCloudAnimator.setDuration(1000);
        mLeftCloudAnimator.setInterpolator(new LinearInterpolator());
        mLeftCloudAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mLeftCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLeftCloudAnimatorValue = (float) animation.getAnimatedValue();
                if (mLeftCloudAnimatorValue > 0) {
                    mLeftCloudAnimatorValue = mLeftCloudAnimatorValue -1;
                } else {
                    mLeftCloudAnimatorValue = -mLeftCloudAnimatorValue ;
                }
                Log.e("11111_mLeftClox",
                        mLeftCloudAnimatorValue + "");
                invalidate();
            }
        });

        mRightCloudAnimator = ValueAnimator.ofFloat(1, 0, -1);
        mRightCloudAnimator.setRepeatCount(1);
        mRightCloudAnimator.setDuration(8000);
        mRightCloudAnimator.setInterpolator(new LinearInterpolator());
        mRightCloudAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mRightCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRightCloudAnimatorValue = (float) animation.getAnimatedValue();
                if (mRightCloudAnimatorValue > 0) {
                    mRightCloudAnimatorValue = mRightCloudAnimatorValue -1;
                } else {
                    mRightCloudAnimatorValue = -mRightCloudAnimatorValue ;
                }
                invalidate();
            }
        });

        mLeftCloudAnimator.start();
        mRightCloudAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //左边云 - 灰的
        //云的底端,就是一个椭圆
//        canvas.drawPath(mLeftCloudPath, mCloudPaint);

        mMatrix.postTranslate(mLeftCloudAnimatorValue * (mDistanceCloudsBetween / 6), 0);
        mLeftCloudPath.transform(mMatrix, mLeftCloudPath);
        canvas.drawPath(mLeftCloudPath, mCloudPaint);


        /**
         * Matrix类,矩阵转换
         * preTranslate（dx，dy） 函数作用是在绘制当前view之前，
         *                        先将View在X轴和Y轴上平移dx，dy个像素
         * postTranslate（dx，dy）函数作用是在绘制当前view之后，
         *                        再将View在X轴和Y轴上平移dx，dy个像素
         * */
        mMatrix.reset();
        mCloudPaint.setColor(getResources().getColor(R.color.color_DE));
        mMatrix.postTranslate((mDistanceCloudsBetween / 12) * mRightCloudAnimatorValue, 0);
        mLeftCloudPath.transform(mMatrix, mLeftCloudPath);
        canvas.drawPath(mLeftCloudPath, mCloudPaint);
        mCloudPaint.setColor(getResources().getColor(R.color.color_B0));

    }


    class RainsDrop {
        float speedX;  //雨滴x轴移动速度
        float speedY;   //雨滴y轴移动速度
        float xLength; //雨滴的x轴长度
        float yLength; //雨滴的y轴长度
        float x;        //雨滴的x轴坐标
        float y;        //雨滴的y轴坐标
        float slope; //雨滴的斜率
    }
}
