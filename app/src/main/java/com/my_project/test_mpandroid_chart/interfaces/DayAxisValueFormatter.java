package com.my_project.test_mpandroid_chart.interfaces;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by com_c on 2018/4/9.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {
    public DayAxisValueFormatter(BarChart mBarChart) {
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        return null;
    }
}
