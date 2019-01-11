package com.my_project.test_more_thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by com_c on 2018/9/4.
 */

public class UIThread2ChildThreadCommunicationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        for (; ; ) {
            return;
        }
    }

    private void init() {
        MyThread childThread = new MyThread();
        childThread.start();
        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        Handler mHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };
        mHandler.sendMessageDelayed(new Message(),2000);
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
}
