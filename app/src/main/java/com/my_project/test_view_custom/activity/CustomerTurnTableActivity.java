package com.my_project.test_view_custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my_project.R;

/**
 * Created by Administrator on 2019\3\12 0012.
 */

public class CustomerTurnTableActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_turntable);
    }
}
