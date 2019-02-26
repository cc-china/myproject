package com.my_project.test_more_thread.product;

import android.util.Log;

import com.my_project.test_more_thread.goods.Products_1;

/**
 * Created by Administrator on 2019\2\25 0025.
 */

public class ProductFactory_1 implements Runnable {
    private Products_1 p;

    public ProductFactory_1(Products_1 product_1) {
        this.p = product_1;
    }

    //对外暴露生产产品的方法
    public void startProduct(String name) {
        p.setName(name + p.getCount());
        p.setCount(p.getCount() + 1);
    }

    @Override
    public void run() {
        while (true) {
            //使用lock锁
            p.lock.lock();
            try {
                while (p.isFlag()){
                    //没有在消费，说明仓库没有产品了，生产者不需要睡眠，赶紧醒来工作了
                    try {
                        Log.e("ProductFactory","我要开始睡了");
                        p.pro_con.await();
                        Log.e("ProductFactory","我要被唤醒了");
                    } catch (InterruptedException e) {e.printStackTrace();}

                }
                startProduct("汉堡");
                Log.e("ProductFactory", Thread.currentThread().getName() + "----生产者------" + p.getName());
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                p.setFlag(true);
                p.con_con.signal();
            } catch (Exception e) {
            } finally {
                p.lock.unlock();
            }
        }
    }
}
