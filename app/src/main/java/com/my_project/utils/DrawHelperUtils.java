package com.my_project.utils;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * introduce:帮助绘图工具类
 * Created by com_c on 2017/12/29.
 */

public class DrawHelperUtils {
    /**
     * 传入起点、终点坐标，Path，和凹凸
     * 自动绘制凹凸的半圆弧
     */
    public static void drawPartCircle(PointF start, PointF end, Path path, boolean outer) {
        float c = 0.551915024494f;
        //计算一下半圆弧的圆心
        PointF circleCenter = new PointF(start.x + (end.x - start.x) / 2
                , start.y + (end.y - start.y) / 2);
        /**
         *半圆弧的半径
         * Math.sqrt(a,b)  指a + b平方跟  比如Math.sqrt（8,2）就是8+2 = 9的平方根，也就是等于3
         * Math.pow（a,b） 指a 的 b次方    比如Math.pow(3,2)   就是3的2次方（平方），也就是等于9
         */

        float radius = (float) Math.sqrt(
                Math.pow(circleCenter.x - start.x, 2)
                        + Math.pow(circleCenter.y - start.y, 2));
        //gap值，代指差值，
        float gap = radius * c;
        if (start.x == end.x) {
            //垂直方向上绘制
            //是否是从上到下
            boolean topToButtom = end.y > start.y ? true : false;
            /**
             * 旋转系数,反正就是记录一下到底是从左到右，还是从右到左
             * flag = a   是从左到右
             * flag = -a   是从又到左
             * */
            int flag;
            if (topToButtom) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x + gap * flag, start.y,
                        circleCenter.x + radius * flag, circleCenter.y - gap * flag,
                        circleCenter.x + radius * flag, circleCenter.y);
                path.cubicTo(circleCenter.x + radius * flag, circleCenter.y + gap * flag,
                        end.x + gap * flag, end.y,
                        end.x, end.y);
            } else {
                // 凹 俩个半圆
                path.cubicTo(start.x, start.y + gap * flag,
                        circleCenter.x - gap * flag, circleCenter.y + radius * flag,
                        circleCenter.x, circleCenter.y + radius * flag);
                path.cubicTo(circleCenter.x + gap * flag, circleCenter.y + radius * flag,
                        end.x, end.y + gap * flag,
                        end.x, end.y);
            }
        } else {
            //水平方向绘制
            //是否是从左到右
            boolean leftToRight = end.x > start.x ? true : false;
            /**
             * 旋转系数,反正就是记录一下到底是从左到右，还是从右到左
             * flag = a   是从左到右
             * flag = -a   是从又到左
             * */
            int flag;
            if (leftToRight) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x, start.y - gap * flag,
                        circleCenter.x - gap * flag, circleCenter.y - radius * flag,
                        circleCenter.x, circleCenter.y - radius * flag);
                path.cubicTo(circleCenter.x + gap * flag, circleCenter.y - radius * flag,
                        end.x, end.y - gap * flag,
                        end.x, end.y);
            } else {
                // 凹 俩个半圆
                path.cubicTo(start.x, start.y + gap * flag,
                        circleCenter.x - gap * flag, circleCenter.y + radius * flag,
                        circleCenter.x, circleCenter.y + radius * flag);
                path.cubicTo(circleCenter.x + gap * flag, circleCenter.y + radius * flag,
                        end.x, end.y + gap * flag,
                        end.x, end.y);
            }
        }
    }
}
