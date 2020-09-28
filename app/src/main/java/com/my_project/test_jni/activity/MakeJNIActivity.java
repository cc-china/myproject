package com.my_project.test_jni.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mikephil.charting.utils.Utils;
import com.my_project.R;
import com.my_project.test_jni.JNIUtils;
import com.my_project.test_jni.JniObjResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        StringBuilder sb = new StringBuilder();
//        byte[] bytes = JNIUtils.setByteArrayData(new byte[]{11, 22, 33, 44, 55});
        JniObjResult obj = new JniObjResult();
        obj.setByteArray(new byte[]{11, 22, 33, 44, 55});
        //JniObjResult bytes = (JniObjResult)
                JNIUtils.setObjectData(obj);
//        for (int i = 0; i < bytes.getByteArray().length; i++) {
//            sb.append( bytes.getByteArray()[i]);
//        }
//        tvJni.setText(sb);
//        tvJni.setText(new JNIUtils().getBackTotalNum(1, 7) + "=1+7");
//        tvJni.setText(new JNIUtils().getBackText("Hello，world"));
//        JNIUtils.setByteArrayData(new byte[]{11,22,33,44,55});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
