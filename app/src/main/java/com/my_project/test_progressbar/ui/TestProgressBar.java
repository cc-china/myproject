package com.my_project.test_progressbar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nolan on 2017/9/27.
 */

public class TestProgressBar extends Activity {
    @Bind(R.id.pb_progress_bar)
    ProgressBar pbProgressBar;
    @Bind(R.id.tv_main_text)
    TextView tvMainText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_progressbar);
        ButterKnife.bind(this);

    }

    Handler handler = new Handler() {
        //接收消息，用于更新UI界面
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            tvMainText.setText(i + "");
        }
    };

    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 0; i <= 30; i++) {
                pbProgressBar.setProgress(i);
                //在子线程中发送消息
                handler.sendEmptyMessage(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //下载的方法
    public void download(View view) {
        //启动线程
        new MyThread().start();
    }
}
