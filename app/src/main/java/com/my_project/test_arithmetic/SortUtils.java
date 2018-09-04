package com.my_project.test_arithmetic;

import android.util.Log;

/**
 * Created by com_c on 2018/8/24.
 */

public class SortUtils {
    /**
     * 冒泡排序
     */
    public static void bubbleSort(int[] arr) {
        /**
         * 冒泡排序就是两两比较数组元素，大的元素和小的元素互换位置，最终达到有序数组的目的
         * 冒泡排序的规则：
         * 1、外层for循环控制循环多少次，那一个数组需要循环多少次，应当是它的length - 1，
         *    因为 下标0和1去对比（假设只有三个元素）1和2去对比，就完了，
         * 2、内层循环控制一趟对比，比较多少次，为什么不直接从头对比到结尾？
         *    因为每一躺两两比较数组元素，一定会找到数组最大的元素，而这个最大的数组元素是确定已知的，
         *    无需再次放到数组中去对比，
         *    例如：第一次对比后，数组最后一位就是排序正确最大的数组元素，固定
         *         第二次对比后，数组倒数第二位就是排序正确倒数第二的数组元素，固定
         *         第三次对比后，数组倒数第三位就是排序正确倒数第三的数组元素，固定
         *                            。。。
         *     所以，内层循环不需要每次都从头到尾的去对比，每次对比后找出的固定元素，下次对比排序就不在参与
         *     所以内层循环的循环次数，就需要减去当前是第几次循环，因为第几次循环，它就会确定固定了第几个最大值
         * 3、在对比两个元素的时候，if判断arr[i]和arr[i+1] 如果满足arr[i]>arr[i+1]，用一个局部变量记录
         *    最大值，也就是temp = arr[i],然后将arr[i+1](也就是两个数中较小的那个)赋值给arr[i]
         *    最后将temp赋值给arr[i+1]，来完成数组元素位置的互换
         * */
        //冒泡排序
        int temp = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                Log.e("111_arr------", arr[i] + "");
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            Log.e("111_arr", arr[i] + "");
        }
    }

    /**
     * 归并排序
     */
    public static void mergeSort(int[] arr1, int[] arr2) {
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

    /**
     * 选择排序
     */
    public static void chooseSort(int[] arr) {
        /**
         * 选择排序的原理：
         * 外层循环控制循环次数，当内层循环每次找到最小值的时候，将外层元素i下标与每次找到的最小值互换位置
         * 内层循环找到数组最小值
         * */
        for (int i = 0; i < arr.length; i++) {
            int least = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[least]) {
                    least = j;
                }
                int temp = arr[least];
                arr[least] = arr[i];
                arr[i] = temp;
            }
        }
    }


    /**
     * 二分法查找
     */
    public static int binarySearch(int[] arr, int key) {
        int start = 0;
        int end = arr.length - 1;
        int center ;
        //有可能key不在数组中
        if (key < arr[start] || key > arr[end] || start > end) {
            return -1;
        }
        while (start <= end) {
            center = (start + end) / 2;
            if (key > arr[center]) {
                //key在数组后半段
                start = center + 1;
            } else if (key == arr[center]) {
                return center;
            } else if (key < arr[center]) {
                //key在数组前半段
                end = center - 1;
            }
        }
        return -1;
    }
}
