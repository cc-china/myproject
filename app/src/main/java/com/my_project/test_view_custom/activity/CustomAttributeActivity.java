package com.my_project.test_view_custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my_project.R;

/**
 * 自定义属性，学习制作一个验证码，原理就是
 * a、画出来一个跟系统的textView控件一样的view
 * 2、给这个view加点击事件，当点击的同时生成一个4位数的数字
 * 这样就做成了一个简易的验证码
 * Created by Administrator on 2017\12\7 0007.
 */

public class CustomAttributeActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_attr);
    }
}
