package com.my_project.test_custom_date_picker.view.date;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.my_project.test_custom_date_picker.view.WheelPicker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by com_c on 2018/4/25.
 */

public class DayPicker extends WheelPicker {


    private int mEndDay;
    private int mSelectedDay;

    public DayPicker(Context context) {
        this(context, null);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
        updateDay();
        setSelectedDay(mSelectedDay, false);
    }

    private void updateDay() {
        List<Integer> DayDate = new ArrayList<>();
        for (int i = 1; i <= mEndDay; i++) {
            DayDate.add(i);
        }
        setDataList(DayDate);
    }

    public void setMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, 1);
        mEndDay = calendar.getActualMaximum(Calendar.DATE);
        if (mSelectedDay > mEndDay) {
            setSelectedDay(mSelectedDay, true);
        }
        updateDay();
    }

    public void setselectedDay(int mEndDay) {
        setSelectedDay(mEndDay, true);
    }

    public void setSelectedDay(int selectedDay, boolean smoothScroll) {
        setCurrentPosition(selectedDay - 1, smoothScroll);
    }

    private void initAttrs() {
        //设置天数的显示格式
        setmItemMaximumWidthText("00");
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMinimumIntegerDigits(2);
        setDataFormat(numberInstance);

        //获取当前月共有多少天
        Calendar.getInstance().clear();
        mEndDay = Calendar.getInstance().getActualMaximum(Calendar.DATE);
        mSelectedDay = Calendar.getInstance().get(Calendar.DATE);

    }
}
