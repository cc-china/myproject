package com.my_project.test_view_custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2019\3\12 0012.
 * 做一个抽奖用的转盘：70%的中奖率
 */

public class TurnTableView extends View {
    public TurnTableView(Context context) {
        this(context,null);
    }

    public TurnTableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TurnTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //转盘圆的圆心，半径，画笔颜色
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setFlags(Paint.LINEAR_TEXT_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
