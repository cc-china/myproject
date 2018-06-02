package com.my_project.test_custom_date_picker.view.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.my_project.R;
import com.my_project.test_custom_date_picker.view.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by com_c on 2018/4/8.
 */

public class YearPicker extends WheelPicker<Integer> {

    private int mSelectedYear;
    private int mStartYear;
    private int mEndYear;
    private OnYearSelectedListener onYearSelectedListener;

    public YearPicker(Context context) {
        this(context, null);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setmItemMaximumWidthText("0000");
        updateYear();
        setSelectedYear(mSelectedYear,false);
        //拿到当前选择好的年份
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer integer, int position) {
                mSelectedYear = integer;
                if (onYearSelectedListener != null) {
                    onYearSelectedListener.onYearSelected(integer);
                }
            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        //获取当前年
        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YearPicker);
        mStartYear = a.getInteger(R.styleable.YearPicker_startYear, 1900);
        mEndYear = a.getInteger(R.styleable.YearPicker_endYear, 2200);
        a.recycle();
    }

    private void updateYear() {
        List<Integer> list = new ArrayList<>();
        for (int i = mStartYear; i <= mEndYear; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    public void setYear(int startYear, int endYear) {
        mStartYear = startYear;
        mEndYear = endYear;
        updateYear();
    }

    public void setSelectedYear(int selectedYear){
        setSelectedYear(selectedYear,true);
    }

    public void setSelectedYear(int selectedYear, boolean smoothScroll) {
        mSelectedYear = selectedYear;
        setCurrentPosition(mSelectedYear - mStartYear,smoothScroll);
    }

    public int getSelectedYear(){
        return mSelectedYear;
    }
    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        this.onYearSelectedListener = onYearSelectedListener;
    }

    public interface OnYearSelectedListener {
        void onYearSelected(int year);
    }
}
