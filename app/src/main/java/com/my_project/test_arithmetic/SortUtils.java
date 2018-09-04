package com.my_project.test_arithmetic;

import android.util.Log;

/**
 * Created by com_c on 2018/8/24.
 */

public class SortUtils {
    public static void quickSort(int[] arr1, int[] arr2) {
        int[] newArr = new int[arr1.length + arr2.length];
        int a1 = 0;
        int b1 = 0;
        int n1 = 0;
        while ((a1 < arr1.length) && (b1 < arr2.length)) {
            if (arr1[a1] < arr2[b1]) {
                //说明arr1中的数组元素小
                newArr[n1++] = arr1[a1];
                a1++;
            } else if (arr1[a1] == arr2[b1]) {
                newArr[n1++] = arr1[a1];
                a1++;
                b1++;
            } else {
                newArr[n1++] = arr2[b1];
                b1++;
            }

        }
        while (a1 < arr1.length) {
            newArr[n1++] = arr1[a1];
            a1++;
        }

        while (b1 < arr2.length) {
            newArr[n1++] = arr2[b1];
            b1++;
        }
        for (int i = 0; i < newArr.length; i++) {
            Log.e("11111", newArr[i] + "   ");
        }

    }
}
