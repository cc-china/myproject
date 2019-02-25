package com.my_project.test_more_thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.net.URL;

/**
 * Created by com_c on 2018/8/22.
 * 测试
 */

public class TestMoreThreadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

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
