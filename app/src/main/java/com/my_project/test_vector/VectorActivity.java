package com.my_project.test_vector;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.my_project.R;

/**
 * Created by cmm on 2020/4/3.
 */

public class VectorActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector);
        ImageView iv_vector = findViewById(R.id.iv_vector);
    }
}
