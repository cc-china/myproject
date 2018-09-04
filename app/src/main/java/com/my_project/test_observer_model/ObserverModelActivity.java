package com.my_project.test_observer_model;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my_project.R;


/**
 * Created by com_c on 2018/6/2.
 */

public class ObserverModelActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Canvas canvas = new Canvas();
        custom1 custom1 = new custom1(this,canvas);
        canvas.setData();//发报纸
    }

}
