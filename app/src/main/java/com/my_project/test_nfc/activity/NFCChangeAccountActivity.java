package com.my_project.test_nfc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.R;

/**
 * Created by com_c on 2018/6/6.
 */

public class NFCChangeAccountActivity extends NfcActivity implements View.OnClickListener {

    private TextView tv_value, tv_value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_nfc_change_account);
        tv_value = findViewById(R.id.tv_value);
        tv_value2 = findViewById(R.id.tv_value2);
        findViewById(R.id.btn_write).setOnClickListener(this);
        initView();
    }

    private void initView() {

    }

    @Override
    protected void onNfcTouch() {
        tv_value.setText("NFC TagId:" + getTagId());
        try {
            tv_value2.setText(readNfcContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_write:
                try {
                    boolean b = writeNfc("13521607066,姓名：龙哥,入党时间：1978年6月,组织关系：栖霞街道党委书记");
                    Toast.makeText(NFCChangeAccountActivity.this, b + "", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
