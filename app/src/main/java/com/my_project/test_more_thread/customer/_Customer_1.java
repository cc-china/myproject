package com.my_project.test_more_thread.customer;

import android.util.Log;

import com.my_project.test_more_thread.goods.Products_1;

/**
 * Created by Administrator on 2019\2\25 0025.
 */

public class _Customer_1 implements Runnable {

    private Products_1 p;

    public _Customer_1(Products_1 product_1) {
        this.p = product_1;
    }

    //提供消费产品的方法
    public String consume() {
        return p.getName();
    }

    @Override
    public void run() {
        while (true) {
            p.lock.lock();
            try {
                while (!p.isFlag()) {
                    try {
                        Log.e("_Customer_1","我要开始睡了");
                        p.con_con.await();
                        Log.e("_Customer_1","我被唤醒了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("_Customer", Thread.currentThread().getName() + "----消费者------" + consume());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException i) {
                }
                p.setFlag(false);
                p.pro_con.signal();
            } catch (Exception e) {
            } finally {
                p.lock.unlock();
            }
        }
    }
}
