package com.my_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by com_c on 2018/1/17.
 * 集合转同步的工具类
 */

public class a extends Activity {

    private Button btn;
    Handler handler;

     {
        if (handler == null) {
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what){
                        case 1:
                            Toast.makeText(a.this,(String)msg.obj,0).show();
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        initView();
        initData();
    }


    private void initData() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final Snackbar make = Snackbar.make(btn, "design 测试", Snackbar.LENGTH_INDEFINITE);
                        make.setAction("点击", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                make.dismiss();
                            }
                        }).show();

                    }
                });
                Message msg = new Message();
                msg.what = 1;
                msg.obj = "哈哈";
                handler.handleMessage(msg);
            }
        });
    }

    private void initView() {
        btn = findViewById(R.id.btn_test);
    }

    private void init() {
        List<Object> objects = Collections.synchronizedList(new ArrayList<Object>());
        Map<Object, Object> objectObjectMap = Collections.synchronizedMap(new TreeMap<Object, Object>());
    }
}
