package com.my_project.test_arithmetic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.my_project.R;

/**
 * Created by com_c on 2018/8/24.
 */

public class TestArithMeticActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_activity);
        initView2();
    }

    private void initView2() {

        //冒泡排序
        int[] arr = {12, 5, 8, 21, 87, 3, 4, 1, 76, 30, 25, 9};
        SortUtils.bubbleSort(arr);

        //选择排序
        SortUtils.chooseSort(arr);

        //归并排序
        int[] arr1 = {0, 1, 2, 5, 8};
        int[] arr2 = {1, 2, 5, 8};
        SortUtils.mergeSort(arr1, arr2);
        int result = SortUtils.binarySearch(arr1, 3);
        Toast.makeText(this, result + "", Toast.LENGTH_SHORT).show();


    }

}
