package com.my_project.test_more_thread.product;

import android.util.Log;

import com.my_project.test_more_thread.basket._Basket;
import com.my_project.test_more_thread.goods.Products_2;

/**
 * Created by Administrator on 2019\2\25 0025.
 */

public class ProductFactory_2 implements Runnable {
    public static int num = 1;
    private _Basket mList;

    public ProductFactory_2(_Basket _basket) {
        this.mList = _basket;
    }

    @Override
    public void run() {
        while (true) {
            mList.in();
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }
}
