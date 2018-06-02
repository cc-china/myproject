package com.my_project.test_touch_event.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.my_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nolan on 2017/10/12.
 */

public class TestTouchEventActivity extends Activity {
    @Bind(R.id.record_button)
    ImageView recordButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_touch_event);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //touch 监听
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_video_press);
                        recordButton.setImageDrawable(drawable);
//                        flag = true;
//                        thread = new ProgressBarThread();
//                        thread.setStop(false);
//                        thread.start();
//                        // TODO -- 定时器 过了时间默认 控件touch事件变为 Up
//                        setTouchDownOrUp();
                        break;
                    case MotionEvent.ACTION_UP:
//                        Drawable drawable1 = getResources().getDrawable(R.mipmap.icon_video_nomal);
//                        recordButton.setImageDrawable(drawable1);
//                        flag = false;
//                        thread.interrupt();
//                        thread.setStop(true);
////                        clickRecord();
//                        //TODO -- 停止录制
//                        endRecord();
                        break;
                }
                return false;
            }
        });
    }
}
