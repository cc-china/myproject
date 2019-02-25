package com.my_project.test_more_thread.product;

import android.util.Log;

import com.my_project.test_more_thread.Products;

/**
 * Created by Administrator on 2019\2\25 0025.
 */

public class ProductFactory implements Runnable {
    private Products p;

    public ProductFactory(Products product) {
        this.p = product;
    }

    //对外暴露生产产品的方法
    public void startProduct(String name) {
        p.setName(name + p.getCount());
        p.setCount(p.getCount() + 1);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (Products.class) {
                if (p.isFlag()) {
                    //没有在消费，说明仓库没有产品了，生产者不需要睡眠，赶紧醒来工作了
                    try {
                        Products.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startProduct("汉堡");
                Log.e("ProductFactory", Thread.currentThread().getName() + "----生产者------" + p.getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                p.setFlag(true);
                Products.class.notify();
            }
        }
    }
}
