package com.my_project.test_custom_date_picker.view.date;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.my_project.R;

/**
 * Created by com_c on 2018/4/25.
 */

public class DatePicker extends LinearLayout {

    private YearPicker mYearPicker;
//    private MonthPicker mMonthPicker;
//    private DayPicker mDayPicker;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.viewgroup_date_picker, this);
        initChildren(view);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

    }

    private void initChildren(View view) {
        mYearPicker = view.findViewById(R.id.yearPicker_layout_date);
        mYearPicker.setOnYearSelectedListener(new YearPicker.OnYearSelectedListener() {
            @Override
            public void onYearSelected(int year) {
//                int month = getMonth();
//                mDayPicker.setMonth(year,month);
            }
        });
//        mMonthPicker = view.findViewById(R.id.monthPicker_layout_date);
//        mMonthPicker.setOnMonthSelectedLinstener(new MonthPicker.OnMonthSelectedListener() {
//            @Override
//            public void onMonthSelected(int month) {
//                int year = getYear();
//                mDayPicker.setMonth(year,month);
//            }
//        });
//        mDayPicker = view.findViewById(R.id.dayPicker_layout_date);
    }


    //拿到月份
//    private int getMonth(){
//        return mMonthPicker.getmSelectedMonth();
//    }
    //拿到年份
    private int getYear(){
        return mYearPicker.getSelectedYear();
    }
}
