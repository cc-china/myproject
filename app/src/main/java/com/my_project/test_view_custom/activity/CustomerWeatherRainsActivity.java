package com.my_project.test_view_custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my_project.R;

/**
 * Created by Administrator on 2018\12\5 0005.
 */

public class CustomerWeatherRainsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_weather_rains);
    }
}
