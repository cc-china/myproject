package com.my_project.test_more_thread.basket;

import android.util.Log;

import com.my_project.test_more_thread.goods.Products_2;
import com.my_project.test_more_thread.product.ProductFactory_2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2019\2\26 0026.
 */

public class _Basket {
    private Products_2[] arr;

    //the size of basket
    public _Basket(int size) {
        arr = new Products_2[size];
    }

    //the pointer of in and out
    private int in_ptr, out_ptr;
    //how many product left in basket
    private int left;
    private volatile boolean flag = false;
    private Lock lock = new ReentrantLock();
    private Condition pro_lock = lock.newCondition();
    private Condition cus_lock = lock.newCondition();

    //product into basket
    public void in() {
        lock.lock();
        try {
            while (flag) {
                try {
                    cus_lock.signal();
                    Log.e("in----------", "我要开始睡了" + left + out_ptr);
                    pro_lock.await();
                    Log.e("in----------", "我被叫醒来了" + left + out_ptr);
                } catch (InterruptedException i) {
                    i.printStackTrace();
                }
            }
            arr[in_ptr] = new Products_2("汉堡", ProductFactory_2.num++);

            Log.e("Put the product: ", arr[in_ptr].getName() + "------into basket[" + in_ptr + "]");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            left++;
            Log.e("in----------", "left------" + left);
            if (++in_ptr == arr.length) {
                in_ptr = 0;
                flag = true;
            }

        } finally {
            lock.unlock();
        }
    }

    //bread out from basket
    public Products_2 out() {
        lock.lock();
        try {
            while (!flag) {
                try {
                    pro_lock.signal();
                    Log.e("out----------", "我要开始睡了" + left + out_ptr);
                    cus_lock.await();

                    Log.e("out----------", "我被叫醒来了" + left + out_ptr);
                } catch (InterruptedException i) {
                    i.printStackTrace();
                }
            }
            Products_2 out_Products_2 = arr[out_ptr];
            Log.e("Get the bread:", out_Products_2.getName() + "-----------from basket[" + out_ptr + "]");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            left--;
            Log.e("out----------", "left------" + left);
            if (++out_ptr == arr.length) {
                out_ptr = 0;
            }
            if (left == 0) {
                flag = false;
            }
            return out_Products_2;
        } finally {
            lock.unlock();
        }
    }
}
