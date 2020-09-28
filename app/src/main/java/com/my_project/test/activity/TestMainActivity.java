package com.my_project.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.my_project.R;


/**
 * Created by com_c on 2017/12/31.
 */

public class TestMainActivity extends Activity implements ViewCallback{

    private Button btn_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        btn_test = findViewById(R.id.btn_test);
        EditText edit_query = findViewById(R.id.edit_query);
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        new B().setData(this);
        ATest test = new ATest();
        test.setId(1);
        test.setName("hahaha");
        ATest b = test;
        test.setName("哈哈");
        btn_test.setText(c.getName());
    }


    @Override
    public void showText(String ctx) {
        btn_test.setText(ctx);
    }

    class B {
        void setData(ViewCallback callback){
            callback.showText("哈哈哈");
        }
    }

    static ATest c ;

    class ATest {
        int id;
        String name;

        public ATest() {
            c = this;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
