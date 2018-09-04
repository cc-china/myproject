package com.my_project.test_customer_hashmap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.my_project.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by com_c on 2018/7/30.
 */

public class TestCustomerHashMapActivity extends Activity implements View.OnClickListener {

    private Button btn_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);
        initData();
    }

    private void initData() {
        StringBuffer buffer = new StringBuffer();
        Map<Integer, String> map = new HashMap<>();
        map.put(3, "3");
        map.put(3, "2");
        Set<Map.Entry<Integer, String>> set = map.entrySet();
        Iterator<Map.Entry<Integer, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> next = iterator.next();
            Integer key = next.getKey();
            String value = next.getValue();
            buffer.append("+" + key + "+" + value);
        }
        btn_test.setText(buffer.toString());
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int arg1 = msg.arg1;
            switch (msg.what) {
                case 1:
                    btn_test.setText(arg1+"");
                    break;
                case 2:
                    btn_test.setText(arg1+"");
                    break;
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                testHandler();
                break;
        }
    }

    private void testHandler() {
        Message message = new Message();
        message.what = 2;
        message.arg1 = 2;
        handler.sendMessage(message);
    }
}
