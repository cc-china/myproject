package com.my_project.test_materical_dialog.acitivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.my_project.R;

import com.my_project.test_materical_dialog.view.MatericalDialogUtils;

/**
 * Created by com_c on 2018/7/13.
 */

public class MatericalDialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materical_dialog);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatericalDialogUtils materical = new MatericalDialogUtils();
                materical.showMatericalDialog(MatericalDialogActivity.this, "", "确定选择吗？",
                        "取消", "确定");
                materical.setOnClickOnClickButtonListener(new MatericalDialogUtils.OnClickButtonListener() {
                    @Override
                    public void clickNegativeButton() {
                        //取消后就重新选择
                    }

                    @Override
                    public void clickPositiveButton() {
                    //取消后就重新选择
                    }
                });
            }
        });
    }
}
