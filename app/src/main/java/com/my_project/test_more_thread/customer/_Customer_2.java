package com.my_project.test_more_thread.customer;

import android.util.Log;

import com.my_project.test_more_thread.basket._Basket;
import com.my_project.test_more_thread.goods.Products_1;
import com.my_project.test_more_thread.goods.Products_2;

/**
 * Created by Administrator on 2019\2\25 0025.
 */

public class _Customer_2 implements Runnable {

    private _Basket mList;
    public _Customer_2(_Basket b) {
        this.mList = b;
    }

    @Override
    public void run() {
        while (true) {
            Products_2 out = mList.out();
            try {Thread.sleep(1000);} catch (InterruptedException i) {}
        }
    }
}
