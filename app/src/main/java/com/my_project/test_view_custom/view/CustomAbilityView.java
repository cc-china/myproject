package com.my_project.test_view_custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.my_project.test_view_custom.model.AbilityResultBean;
import com.my_project.utils.Dp2PxF;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\11\16 0016.
 */

public class CustomAbilityView extends View {
    //声明view是几边形
    private int n;

    //七边形的半径
    private float R;

    //能力值数据一共有几个等级
    private int intervalCount;

    //正边形 对角线夹角
    private float angle;

    private int viewWidth;//控件宽度
    private int viewHeight;//控件高度
    private ArrayList<ArrayList<PointF>> pointsArrayList;//多边形的所有顶点
    private ArrayList<PointF> abilityArrayPoint;//玩家能力点集合
    private Paint linePaint;//画线条的画笔
    private Paint textPaint;//画文字的笔
    private AbilityResultBean data;//设置view的源数据对象，有不同的属性

    public CustomAbilityView(Context context) {
        //使用this，使得不论如何初始化都会调用第三个构造方法
        this(context, null);
    }

    public CustomAbilityView(Context context, @Nullable AttributeSet attrs) {
        //使用this，使得不论如何初始化都会调用第三个构造方法
        this(context, attrs, 0);
    }

    public CustomAbilityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSize(context);
        initPoints(context);
        initPaint(context);
    }

    /**
     * 初始化画笔
     */
    private void initPaint(Context context) {
        //画线的笔
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置线条的宽度
        linePaint.setStrokeWidth(Dp2PxF.dp2px(context, 1f));
        //画文字的笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);//设置文字居中
        textPaint.setTextSize(Dp2PxF.dp2px(context, 14));
        textPaint.setColor(Color.BLACK);
    }

    /**
     * 初始化多边形的所有点，每一圈n个点，共intervalCount圈
     */
    private void initPoints(Context context) {
        //一个集合中每个元素又是一个点集合，有几个多边形就有几个点集合
        pointsArrayList = new ArrayList<>();
        float x;
        float y;
        /**
         * 画LOL能力点的逻辑，一共设置了四个层次的能力等级，用for循环表示出来
         * */
        for (int i = 0; i < intervalCount; i++) {
            //创建一个存储点的集合
            ArrayList<PointF> pointFs = new ArrayList<>();
            /**
             * TODO--每一个层次(每条边)有7个顶点，用for循环表示出来
             * */
            for (int j = 0; j < n; j++) {
                //TODO  每一圈的半径都要按比例减少，
                float r = R * ((float) (intervalCount - i) / intervalCount);
                /**
                 * 有半径r，有弧度 angle ，用三角函数可以计算出对应这个点的横纵坐标
                 * x = r * cosθ;
                 * y = r * sinθ;
                 * TODO -- 源码要求这个弧度必须逆时针旋转90°，也就是说将弧度减去90°
                 * */
                x = (float) (r * Math.cos(j * angle - Math.PI / 2));
                y = (float) (r * Math.sin(j * angle - Math.PI / 2));
                pointFs.add(new PointF(x, y));
            }
            pointsArrayList.add(pointFs);
        }
    }

    /**
     * 初始化一些固定的数据
     */
    private void initSize(Context context) {
        n = 7;//七边形
        R = Dp2PxF.dp2pxF(context, 100);//半径
        intervalCount = 4;//4层
        angle = (float) ((2 * Math.PI) / n);//圆心角 360°除以 边的个数 = 顶点到圆心的对角线夹角 （必须是正几边形）
        //屏幕宽度 ，单位是像素
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        //设置控件方向为正方向
        viewWidth = widthPixels;
        viewHeight = widthPixels;
    }

    /**
     * 对外提供一个方法，设置view的源数据
     */
    public void setData(AbilityResultBean data) {
        if (data == null) {
            return;
        }
        this.data = data;
        //view本身调用 迫使view重画(就是通俗的刷新界面)
        invalidate();
}

    /**
     * 重写onMeasure方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(viewWidth, viewHeight);
        //设置view的最终视图大小
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //把画布的原点移动到控件中心点
        canvas.translate(viewWidth / 2, viewHeight / 2);
        drawPolygon(canvas);//画多边形的框，每一层都绘制
        drawOutLine(canvas);//先画最外边的多边形轮廓线、再画顶点到多边形中心的直线
        drawAbilityLine(canvas);//画玩家的能力值不规则七边形
        drawAbilityText(canvas);//画玩家能力值的汉字

        //画俩条横纵坐标的坐标轴，必须要给放到draw方法最后画，不然会被其他线条覆盖住
        linePaint.setColor(Color.RED);
        canvas.drawLine(-(viewWidth / 2), 0, viewWidth / 2, 0, linePaint);//x
        canvas.drawLine(0, -(viewWidth / 2), 0, viewWidth / 2, linePaint);//y

    }

    /**
     * 先画最外边的多边形轮廓线、再画顶点到多边形中心的直线
     * @param canvas
     */
    private void drawOutLine(Canvas canvas) {
        //画边上的线条
        //保存画布的当前状态（平移、缩放、旋转、裁剪等），和canvas配合使用
        canvas.save();
        //设置画线条的笔为描边模式，空心的
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor("#99DCE2"));
        //初始化路径
        Path path = new Path();
        for (int i = 0; i < n; i++) {
            //取出 x y 坐标值
            float x = pointsArrayList.get(0).get(i).x;
            float y = pointsArrayList.get(0).get(i).y;
            //判断起点
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        //设置闭合环
        path.close();
        //画布调用路径方法，传入路径和画笔这俩构造参数
        canvas.drawPath(path, linePaint);
        //再画顶点到中心的线条
        for (int i = 0; i < n; i++) {
            //取出 x y 坐标值
            float x = pointsArrayList.get(0).get(i).x;
            float y = pointsArrayList.get(0).get(i).y;
            //原点都是中心点
            canvas.drawLine(0, 0, x, y, linePaint);
        }
        canvas.restore();
    }

    /**
     * TODO - 画多边形的框，每一层都绘制
     */
    private void drawPolygon(Canvas canvas) {
        //保存画布当前状态（平移、缩放、旋转、裁剪等），和canvas.restore（）配合使用
        canvas.save();
        //设置画线条的笔为填充模式且描边
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();//路径
        for (int i = 0; i < intervalCount; i++) {
            //设置每一层的颜色都不相同
            switch (i) {
                case 0:
                    linePaint.setColor(Color.parseColor("#D4F0F3"));
                    break;
                case 1:
                    linePaint.setColor(Color.parseColor("#99DCE2"));
                    break;
                case 2:
                    linePaint.setColor(Color.parseColor("#56C1C7"));
                    break;
                case 3:
                    linePaint.setColor(Color.parseColor("#278891"));
                    break;
            }
            //取出来我们在initPoint方法中存储的点，拿到这些点的横纵坐标,n边行当然有n个顶点
            for (int j = 0; j < n; j++) {
                float x = pointsArrayList.get(i).get(j).x;
                float y = pointsArrayList.get(i).get(j).y;
                //如果是每层的第一个点，就把路径Path设置为起点
                if (j == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            //路径设置为闭合环
            path.close();
            //调用画布画路径的方法，传入路径path和画笔
            canvas.drawPath(path, linePaint);
            //画完以后清除路径Path
            path.reset();
        }
        canvas.restore();
    }

    private void drawAbilityLine(Canvas canvas) {
        //保存画布当前状态（平移、缩放、旋转、裁剪等），和canvas.restore（）配合使用
        canvas.save();
        //设置画线条笔的模式，空心的，描边模式，画笔的颜色，画笔的宽度
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.parseColor("#E96153"));
        linePaint.setStrokeWidth(Dp2PxF.dp2px(getContext(), 2f));
        //把玩家的能力值初始化出来
        abilityArrayPoint = new ArrayList<>();
        int[] allAbilitys = data.getAllAbilitys();
        for (int i = 0; i < n; i++) {
            //玩家能力值范围的半径r = 传进来参数 除以单位1 把它换算成 百分比% ，用原始多边形半径
            //乘以这个百分比%，就得到了当前点的半径，再拿着半径，按之前的三角函数可以计算出横纵坐标
            float r = R * (allAbilitys[i] / 100.0f);
            float x = (float) (r * Math.cos(i * angle - Math.PI / 2));
            float y = (float) (r * Math.sin(i * angle - Math.PI / 2));
            abilityArrayPoint.add(new PointF(x, y));
        }
        //初始化路径Path
        Path path = new Path();
        for (int i = 0; i < n; i++) {
            //取出来玩家能力值点的横纵坐标
            float x = abilityArrayPoint.get(i).x;
            float y = abilityArrayPoint.get(i).y;
            //判断起点
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, linePaint);
        canvas.restore();
    }

    private void drawAbilityText(Canvas canvas) {
        canvas.save();
        //先计算出坐标来
        ArrayList<PointF> textPoints = new ArrayList<>();
        /**
         *因为这个文字是要写在最外层轮廓对应的多边形顶点上
         * ，所以这个半径就应当是多边形半径R+文字的字体大小
         */
        for (int i = 0; i < n; i++) {
            float r = R + Dp2PxF.dp2pxF(getContext(), 15f);
            float x = (float) (r * Math.cos(i * angle - Math.PI / 2));
            float y = (float) (r * Math.sin(i * angle - Math.PI / 2));
            textPoints.add(new PointF(x, y));
        }

        //TODO 拿到字体测量器
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        String[] abilitys = AbilityResultBean.getAbilitys();
        for (int i = 0; i < n; i++) {
            float x = textPoints.get(i).x;
            /**
             * ascent 上坡度：文字基准线到文字最高处的距离
             * descent下坡度：文字基准线到文字最低处的距离
             * */
            float y = textPoints.get(i).y - (fontMetrics.ascent + fontMetrics.descent) / 2;
            canvas.drawText(abilitys[i], x, y, textPaint);
        }
        canvas.restore();
    }
}
