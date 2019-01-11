package com.my_project.test_view_custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2018\10\25 0025.
 */

public class CustomerCircle extends View {

    private Paint paint;
    private int cx;
    private int cy;

    public CustomerCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        cx = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        cy = MeasureSpec.getSize(heightMeasureSpec);


        Log.e("222widthSize", "" + cx);
        Log.e("222heightSize", "" + cy);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(cx/10,cx/10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.e("222onSizeChanged", "w" + w + "h" + h + "oldw" + oldw + "oldh" + oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.min(cx,cy);
//        canvas.drawCircle(cx/2,cy/2,radius/20,paint);
        canvas.drawCircle(radius/20,radius/20,radius/20,paint);
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
    }
}
