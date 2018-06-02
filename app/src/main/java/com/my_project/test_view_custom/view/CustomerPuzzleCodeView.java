package com.my_project.test_view_custom.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.my_project.R;
import com.my_project.test_view_custom.interfaces.OnCaptchaMatchCallback;
import com.my_project.utils.DrawHelperUtils;

import java.util.Random;

/**
 * Created by com_c on 2017/12/27.
 */

public class CustomerPuzzleCodeView extends ImageView {
    //验证码滑块、阴影滑块的宽和高
    private int captchaWidth;
    private int captchaHeight;
    //随机数
    private Random mRandom;
    //view区域的阴影部分
    private PorterDuffXfermode mPorterDuffXfermode;
    //滑块的画笔
    private Paint mMaskPaint;
    //阴影滑块的画笔
    private Paint mMaskShadowPaint;
    //滑块、验证码阴影区域的path路径
    private Path mPuzzleCodePath;
    //ImageView的宽和高
    private int mWidth;
    private int mHeight;
    //验证码正确与否的回调函数接口对象
    private OnCaptchaMatchCallback onCaptchaMatchCallback;
    //是否绘制滑块（验证失败闪烁动画用）
    private boolean isDrawMask;
    //验证码正确的动画
    private ValueAnimator mSuccessAnim;
    //验证码失败的动画
    private ValueAnimator mFailAnim;
    //验证码正确的动画位移范围
    private int mSuccessAnimOffset;
    //是否展示验证码正确的动画flag
    private boolean isShowSuccessAnim;
    //是否处于验证状态下，在验证成功后为false，其余时间为true
    private boolean isMatchMode;
    //验证码成功的动画是从右到左的一个平行四边形的位移动画，这是平行四边形的画笔
    private Paint mSuccessPaint;
    //验证码成功的动画是从右到左的一个平行四边形的位移动画，这是平行四边形的path路径
    private Path mSuccessPath;
    //验证码左上角的起点坐标 x y
    private int mPuzzleCodeX;
    private int mPuzzleCodeY;
    //验证码阴影的画笔
    private Paint mPaint;
    //滑块bitmap
    private Bitmap sliderBitmap;
    //滑块阴影bitmap
    private Bitmap shadowSliderBitmap;
    //滑块的位移
    private int mDragerOffset;
    //验证的误差允许值
    private float mMatchDeviation;
    private String TAG = "CustomerPuzzleCodeView";

    public CustomerPuzzleCodeView(Context context) {
        this(context, null);
    }

    public CustomerPuzzleCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerPuzzleCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置滑块的默认大小
        int defaultSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        captchaWidth = defaultSize;
        captchaHeight = defaultSize;
        mMatchDeviation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
        //获取自定义的属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomerPuzzleCodeView, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.CustomerPuzzleCodeView_captchaWidth:
                    captchaWidth = (int) typedArray.getDimension(index, defaultSize);
                    break;
                case R.styleable.CustomerPuzzleCodeView_captchaHeight:
                    captchaHeight = (int) typedArray.getDimension(index, defaultSize);
                    break;
                case R.styleable.CustomerPuzzleCodeView_matchDeviation:
                    mMatchDeviation = typedArray.getDimension(index, mMatchDeviation);
                    break;
            }
        }
        typedArray.recycle();
        mRandom = new Random(System.nanoTime());
        setPaint();
    }

    /**
     * 设置验证码验证回调
     */
    public void setOnCaptchaMatchCallback(OnCaptchaMatchCallback onCaptchaMatchCallback) {
        this.onCaptchaMatchCallback = onCaptchaMatchCallback;
    }

    /**
     * 设置画笔
     */
    private void setPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0x77000000);
        //设置画笔遮罩滤镜
        mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        //滑块区域
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        //实例化阴影画笔
        mMaskShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mMaskShadowPaint.setColor(Color.BLACK);
        mMaskShadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        //实例化path路径
        mPuzzleCodePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //拿到宽高
        mWidth = w;
        mHeight = h;

        //动画区域会用宽高
        createMatchAnim();

        //启用UI线程，创建验证码
        post(new Runnable() {
            @Override
            public void run() {
                createPuzzleCode();
            }
        });
    }

    public void createPuzzleCode() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            resetFlags();//开启验证模式
            createPuzzleCodePath();//创建验证码的Path
            createMask();//生成滑块
            invalidate();
        }
    }

    /**
     * 生成滑块
     */
    private void createMask() {
        sliderBitmap = getSliderBitmap(((BitmapDrawable) getDrawable()).getBitmap());
        //滑块阴影
        shadowSliderBitmap = sliderBitmap.extractAlpha();
        //拖动的位移重置
        mDragerOffset = 0;
        //isDrawMask 绘制失败闪烁动画用
        isDrawMask = true;
    }

    private Bitmap getSliderBitmap(Bitmap mBitmap) {
        //抠图，把验证码的阴影部分区域生成出来
        Bitmap tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Log.e(TAG, " getMaskBitmap: width:" + mBitmap.getWidth() + ",  height:" + mBitmap.getHeight());
        Log.e(TAG, " View: width:" + mWidth + ",  height:" + mHeight);
        //把创建的bitmap作为画板
        Canvas mTempCanvas = new Canvas(tempBitmap);
//        mTempCanvas.clipPath(mPuzzleCodePath);
        mTempCanvas.drawPath(mPuzzleCodePath, mMaskPaint);
//        mTempCanvas.drawBitmap(mBitmap,getMatrix(),mMaskPaint);
//        sliderzBitmap.extractAlpha()
        return tempBitmap;
    }

    /**
     * 生成验证码path，这个就是图片的阴影部分区域，是我们滑动滑块要放到的验证码区域，
     */
    private void createPuzzleCodePath() {
        //原本打算随机生成gap，后来发现 width / 3  的效果比较理想
        int gap = captchaWidth / 3;
        //随机生成验证码阴影左上角的 x y 坐标
        mPuzzleCodeX = mRandom.nextInt(mWidth - captchaWidth - gap);
        mPuzzleCodeY = mRandom.nextInt(mHeight - captchaHeight - gap);
        Log.d(TAG, "createCaptchaPath() called mWidth:" + mWidth + ", mHeight:" + mHeight
                + ", mCaptchaX:" + mPuzzleCodeX + ", mCaptchaY:" + mPuzzleCodeY);

        mPuzzleCodePath.reset();
        mPuzzleCodePath.lineTo(0, 0);

        //从左上角开始，绘制一个不规则的阴影
        mPuzzleCodePath.moveTo(mPuzzleCodeX, mPuzzleCodeY);//左上角
        mPuzzleCodePath.lineTo(mPuzzleCodeX+ gap, mPuzzleCodeY );//左上角
        //画一个随机凹凸的半圆
        DrawHelperUtils.drawPartCircle(new PointF(mPuzzleCodeX + gap, mPuzzleCodeY),
                new PointF(mPuzzleCodeX + gap * 2, mPuzzleCodeY), mPuzzleCodePath, mRandom.nextBoolean());

        mPuzzleCodePath.lineTo(mPuzzleCodeX + captchaWidth, mPuzzleCodeY);//右上角
        mPuzzleCodePath.lineTo(mPuzzleCodeX + captchaWidth, mPuzzleCodeY + gap);
        //画一个随机凹凸的半圆
        DrawHelperUtils.drawPartCircle(
                new PointF(mPuzzleCodeX + captchaWidth, mPuzzleCodeY + gap),
                new PointF(mPuzzleCodeX + captchaWidth, mPuzzleCodeY + gap * 2),
                mPuzzleCodePath, mRandom.nextBoolean());

        mPuzzleCodePath.lineTo(mPuzzleCodeX + captchaWidth, mPuzzleCodeY + captchaHeight);//右下角
        mPuzzleCodePath.lineTo(mPuzzleCodeX + captchaWidth - gap, mPuzzleCodeY + captchaHeight);//右下角
        //画一个随机凹凸的半圆
        DrawHelperUtils.drawPartCircle(
                new PointF(mPuzzleCodeX + captchaWidth - gap, mPuzzleCodeY + captchaHeight),
                new PointF(mPuzzleCodeX + captchaWidth - gap * 2, mPuzzleCodeY + captchaHeight),
                mPuzzleCodePath, mRandom.nextBoolean());


        mPuzzleCodePath.lineTo(mPuzzleCodeX, mPuzzleCodeY + captchaHeight);//左下角
        mPuzzleCodePath.lineTo(mPuzzleCodeX, mPuzzleCodeY + captchaHeight - gap);//左下角
        //画一个随机凹凸的半圆
        DrawHelperUtils.drawPartCircle(
                new PointF(mPuzzleCodeX, mPuzzleCodeY + captchaWidth - gap),
                new PointF(mPuzzleCodeX, mPuzzleCodeY + captchaHeight - gap * 2),
                mPuzzleCodePath, mRandom.nextBoolean());


        mPuzzleCodePath.close();//设置为闭合环
    }

    /**
     * 重启一些flags，开启验证模式
     */
    private void resetFlags() {
        isMatchMode = true;
    }

    private void createMatchAnim() {
        //验证失败的动画
        mFailAnim = ValueAnimator.ofFloat(0, 1);
        mFailAnim.setDuration(100).setRepeatCount(4);//设置动画持续时间和重复次数
        mFailAnim.setRepeatMode(ValueAnimator.REVERSE);

        //失败的时候先闪一闪 动画，斗鱼直播是隐藏 -- 显示 -- 隐藏 -- 显示
        mFailAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onCaptchaMatchCallback.matchFailed(CustomerPuzzleCodeView.this);
            }
        });
        mFailAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) mFailAnim.getAnimatedValue();
                if (animatedValue < 0.5f) {
                    isDrawMask = false;
                } else {
                    isDrawMask = true;
                }
                invalidate();
            }
        });
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        //成功的动画,验证成功之后白光一闪的动画
        mSuccessAnim = ValueAnimator.ofInt(mWidth + width, 0);
        mSuccessAnim.setDuration(500);
        //根据时间流逝的百分比来设置计算当前属性值改变的百分比，插值器
        mSuccessAnim.setInterpolator(new FastOutLinearInInterpolator());
        mSuccessAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSuccessAnimOffset = (int) mSuccessAnim.getAnimatedValue();
                invalidate();
            }
        });
        mSuccessAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isShowSuccessAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onCaptchaMatchCallback.matchSuccess(CustomerPuzzleCodeView.this);
                isShowSuccessAnim = false;
                isMatchMode = false;
            }
        });
        //创建验证成功后的动画 ---白色平行四边形，反向滚过来
        mSuccessPaint = new Paint();
        mSuccessPaint.setShader(new LinearGradient(0, 0, width / 2 * 3, mHeight
                , new int[]{0x00ffffff, 0x88ffffff}, new float[]{0, 0.5f}, Shader.TileMode.MIRROR));
        //验证成功后的动画 ---白色平行四边形 的path
        mSuccessPath = new Path();
        mSuccessPath.moveTo(0, 0);//代表起点
        mSuccessPath.rLineTo(width, 0);//代表终点
        mSuccessPath.rLineTo(width / 2, mHeight);//代表终点
        mSuccessPath.rLineTo(-width, 0);//代表终点
        mSuccessPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isMatchMode) {
            //画阴影部分（验证码区域）
            canvas.drawPath(mPuzzleCodePath, mPaint);
            //画滑块，先画阴影，再画
            if (sliderBitmap != null && shadowSliderBitmap != null && isDrawMask) {
                canvas.drawBitmap(sliderBitmap.extractAlpha(), -mPuzzleCodeX + mDragerOffset, 0, mMaskShadowPaint);
                canvas.drawBitmap(sliderBitmap, -mPuzzleCodeX + mDragerOffset, 0, null);
            }
            //当验证成功后画 成功的平行四边形
            if (isShowSuccessAnim) {
                canvas.translate(mSuccessAnimOffset, 0);
                canvas.drawPath(mSuccessPath, mSuccessPaint);
            }
        }
    }

    /**
     * 校验验证码的正确与否
     */
    public void matchSlider() {
        if (isMatchMode && onCaptchaMatchCallback != null) {
            /**
             * 校验的逻辑
             *    通过比较 拖拽的距离 和 验证码阴影区域的 起始坐标 x 的距离，默认3dp以内算验证成功
             */
            if (Math.abs(mDragerOffset - mPuzzleCodeX) < mMatchDeviation) {
                mSuccessAnim.start();
            } else {
                //就是失败了
                mFailAnim.start();
            }
        }
    }

    /**
     * 重置验证码滑动距离
     */
    public void reSetPuzzleCode() {
        mDragerOffset = 0;
        invalidate();
    }

    /**
     * 设置当前滑动值  -- 距离
     */
    public void setCurrentSliderValue(int value) {
        mDragerOffset = value;
        invalidate();
    }

    /**
     * 最大可滑动值
     */
    public int getMaxSlidingValue() {
        return mWidth - captchaWidth;
    }
}
