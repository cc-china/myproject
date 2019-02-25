package com.my_project.test_more_thread.customer;

import android.util.Log;

import com.my_project.test_more_thread.Products;

/**
 * Created by Administrator on 2019\2\25 0025.
 */

public class _Customer implements Runnable {

    private Products p;

    public _Customer(Products product) {
        this.p = product;
    }

    //提供消费产品的方法
    public String consume() {
        return p.getName();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (Products.class) {
                if (!p.isFlag()) {
                    try {
                        Products.class.wait();
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
                Products.class.notify();
            }
        }
    }
}
