package com.my_project.test_mpandroid_chart.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.my_project.R;
import com.my_project.test_mpandroid_chart.interfaces.DayAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by com_c on 2018/4/9.
 */

public class MPAndroidChartActivity extends Activity {
    @Bind(R.id.BarChart)
    BarChart mBarChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpandroid_chart);
        ButterKnife.bind(this);
        setBarChart();
    }
    private void setBarChart(){
        //设置背景颜色
        mBarChart.setBackgroundColor(getResources().getColor(R.color.bg_white));
        //BarChart的点击事件
        mBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        //设置数值选择的监听
        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
            }
        });
        //是否显示最右侧竖线
        mBarChart.getAxisRight().setEnabled(false);
        //设置高亮显示
        mBarChart.setHighlightFullBarEnabled(false);
        mBarChart.setDrawValueAboveBar(false);
        //设置支持触控
        mBarChart.setTouchEnabled(false);
        //设置是否支持拖拽
        mBarChart.setDragEnabled(false);
        //设置能否缩放
        mBarChart.setScaleEnabled(true);
        //设置true支持两个指头向X、Y轴的缩放，如果为false，只能支持X或者Y轴的当方向缩放
        mBarChart.setPinchZoom(true);
        //获取图表右下角的描述性文字，setEnable（）默认为true
        mBarChart.getDescription().setEnabled(false);
        Description description = new Description();
        description.setText("");
        //设置右下角的描述文字
        mBarChart.setDescription(description);
        //设置阴影
        mBarChart.setDrawBarShadow(false);
        //设置所有的数值在图形的上面,而不是图形上
        mBarChart.setDrawValueAboveBar(true);
        //设置最大的能够在图表上显示数字的图数
        mBarChart.setMaxVisibleValueCount(60);
        //设置背景是否网格显示
        mBarChart.setDrawGridBackground(false);
        //X轴的数据格式
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mBarChart);
        //得到X轴，设置X轴的样式
        XAxis xAxis = mBarChart.getXAxis();
        String[] xAxisValue = {"","党支部数量", "党小组数量", "阵地建设未达标数量", "软弱涣散基层组织数量"};
        //设置X轴上的字符串类型坐标点
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValue));
        //设置位置
        xAxis.setTextSize(6.0F);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置特定的标签类型
//        xAxis.setTypeface(mTfLight);
        //设置是否绘制网格线
        xAxis.setDrawGridLines(false);
        //设置最小的区间，避免标签的迅速增多
        xAxis.setGranularity(1f); // only intervals of 1 day
        //设置进入时的标签数量
        xAxis.setLabelCount(7);
        //设置数据格式
//        xAxis.setValueFormatter(xAxisFormatter);
//        IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = mBarChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //Sets the top axis space in percent of the full range. Default 10f
        leftAxis.setSpaceTop(15f);
        //设置Y轴最小的值
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        //设置图例样式，默认可以显示，设置setEnabled（false）；可以不绘制
        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        //设置X轴和Y轴显示的刻度
        setData(3, 20);
    }
    private void setData(int count, int range) {
        float start = 1f;
        ArrayList<String> XVals1 = new ArrayList<>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            if (i == 1) {
                XVals1.add("党支部数量");
                yVals1.add(new BarEntry(i, 15));
            } else if (i == 2) {
                XVals1.add("党小组数量");
                yVals1.add(new BarEntry(i, 10));
            } else if (i == 3) {
                XVals1.add("阵地建设未达标数量");
                yVals1.add(new BarEntry(i, 5));
            } else if (i == 4) {
                XVals1.add("软弱涣散基层组织数量");
                yVals1.add(new BarEntry(i, 20));
            }
        }
        BarDataSet set1;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.5f);
            mBarChart.setData(data);
        }

    }

}
