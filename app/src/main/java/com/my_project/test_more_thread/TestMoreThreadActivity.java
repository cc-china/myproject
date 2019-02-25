package com.my_project.test_more_thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.my_project.test_more_thread.customer._Customer;
import com.my_project.test_more_thread.product.ProductFactory;

import java.net.URL;

/**
 * Created by Administrator on 2019\2\25 0025.
 * 生产者消费者模式是一个经典的多线程模式，要搞懂线程的同步异步，
 * 生产者消费者模式出发是一个很好的学习模式，但是这个模式是面向过程开发的软件模式，
 * 它并不是面向对象的JAVA方式
 */

public class TestMoreThreadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Products product = new Products();
        ProductFactory pro = new ProductFactory(product);
        _Customer con = new _Customer(product);
        Thread p = new Thread(pro);
        Thread c = new Thread(con);
        p.start();
        c.start();

    }

    Object o1 = new Object();
    Object o2 = new Object();

    public class T1 extends Thread {
        @Override
        public void run() {
//            while (true) {
            synchronized (o1) {
                T1 t1 = new T1();
                T2 t2 = new T2();
                t1.start();
                t2.start();
                for (int i = 0; i < 100; i++) {
                    Log.e("T1", i + "---我是谁");
                    if (i == 5) {
                        try {
                            Log.e("T1", i + "---我要开始睡了");
                            o1.wait();
                            Log.e("T1", i + "---我被T2叫醒了");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("T1", i + "---我被T2叫醒了，现在正在乱跑");
                }
            }
//            }
        }
    }

    class T2 extends Thread {
        @Override
        public void run() {
//            while (true) {
            synchronized (o1) {
                for (int i = 0; i < 100; i++) {
                    if (i == 5) {
                        Log.e("T2", i + "---我要叫醒T1");
                        o1.notifyAll();
                    }
                }
            }
//            }
        }
    }

    private class MyTask extends AsyncTask<URL, Integer, Long> {
        //任务执行前
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //使用工作线程执行任务---子线程
        @Override
        protected Long doInBackground(URL... urls) {
            return null;
        }

        //实时刷新进度条
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        //任务执行完成后
        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
        }
    }
}
