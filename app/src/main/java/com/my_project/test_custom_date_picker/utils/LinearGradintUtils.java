package com.my_project.test_custom_date_picker.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * 颜色线性渐变的工具
 * Created by com_c on 2018/4/8.
 */

public class LinearGradintUtils {
    private int mStartColor;
    private int mEndColor;
    private int mRedStart;
    private int mGreenStart;
    private int mBlueStart;
    private int mRedEnd;
    private int mGreenEnd;
    private int mBlueEnd;

    public LinearGradintUtils(@ColorInt int startColor, @ColorInt int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        updateColor();
    }

    public void setStartColor(@ColorInt int startColor) {
        mStartColor = startColor;
        updateColor();
    }

    public void setEndColor(@ColorInt int endColor) {
        mEndColor = endColor;
        updateColor();
    }

    private void updateColor() {
        mRedStart = Color.red(mStartColor);
        mGreenStart = Color.green(mStartColor);
        mBlueStart = Color.blue(mStartColor);
        mRedEnd = Color.red(mEndColor);
        mGreenEnd = Color.green(mEndColor);
        mBlueEnd = Color.blue(mEndColor);
    }

    public int getColor(float ratio) {
        int red = (int)(mRedStart + ((mRedEnd - mRedStart) * ratio + 0.5));
        int green = (int)(mGreenStart + ((mGreenEnd - mGreenStart) * ratio + 0.5));
        int blue = (int)(mBlueStart + ((mBlueEnd - mBlueStart) * ratio + 0.5));
        return Color.rgb(red,green,blue);
    }
}
