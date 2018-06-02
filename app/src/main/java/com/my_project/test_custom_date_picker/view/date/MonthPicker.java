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

public class MonthPicker extends WheelPicker {

    private int mSelectedMonth;
    private OnMonthSelectedListener onMonthSelectedLinstener;

    public MonthPicker(Context context) {
        this(context, null);
    }

    public MonthPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setmItemMaximumWidthText("00");
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMinimumIntegerDigits(2);
        setDataFormat(numberInstance);
        initAttrs();
        updateMonth();
        setSelectedMonth(mSelectedMonth, false);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer item, int position) {
                mSelectedMonth = item;
                if (onMonthSelectedLinstener !=null){
                    onMonthSelectedLinstener.onMonthSelected(mSelectedMonth);
                }
            }
        });
    }

    private void updateMonth() {
        List<Integer> monthDate = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthDate.add(i);
        }
        setDataList(monthDate);
    }
    public void setSelectedMonth(int mSelectedMonth){
        setSelectedMonth(mSelectedMonth,true);
    }
    public void setSelectedMonth(int mSelectedMonth, boolean smoothScroll) {
        setCurrentPosition(mSelectedMonth - 1, smoothScroll);
    }
    public int getmSelectedMonth(){
        return mSelectedMonth;
    }
    private void initAttrs() {
        //获取当前月
        Calendar.getInstance().clear();
        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public void setOnMonthSelectedLinstener(OnMonthSelectedListener onMonthSelectedLinstener) {
        this.onMonthSelectedLinstener = onMonthSelectedLinstener;
    }

    public interface OnMonthSelectedListener {
        void onMonthSelected(int month);
    }
}
