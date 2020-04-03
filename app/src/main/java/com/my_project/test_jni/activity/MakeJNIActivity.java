package com.my_project.test_jni.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mikephil.charting.utils.Utils;
import com.my_project.R;
import com.my_project.test_jni.JNIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by com_c on 2018/3/8.
 */

public class MakeJNIActivity extends Activity {
    @Bind(R.id.tv_jni)
    TextView tvJni;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_jni);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
//        tvJni.setText(new JNIUtils().getName("84668464545"));
//        tvJni.setText(new JNIUtils().getBackTotalNum(1,7)+"");
        tvJni.setText(new JNIUtils().getBackText("12321312312"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
