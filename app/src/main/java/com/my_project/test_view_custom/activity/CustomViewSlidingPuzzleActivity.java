package com.my_project.test_view_custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.my_project.R;
import com.my_project.test_view_custom.interfaces.OnCaptchaMatchCallback;
import com.my_project.test_view_custom.view.CustomerPuzzleCodeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by com_c on 2017/12/26.
 */

public class CustomViewSlidingPuzzleActivity extends Activity {
    @Bind(R.id.view_customer_puzzle_code)
    CustomerPuzzleCodeView customerPuzzleCode;
    @Bind(R.id.view)
    RelativeLayout view;
    @Bind(R.id.seb_sliding)
    SeekBar sebSliding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview_sliding_puzzle);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        customerPuzzleCode.setOnCaptchaMatchCallback(new OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(CustomerPuzzleCodeView customerPuzzleCodeView) {
                Toast.makeText(CustomViewSlidingPuzzleActivity.this,"验证成功了",Toast.LENGTH_SHORT).show();
                sebSliding.setEnabled(false);
            }

            @Override
            public void matchFailed(CustomerPuzzleCodeView customerPuzzleCodeView) {
                Toast.makeText(CustomViewSlidingPuzzleActivity.this,"验证失败了",Toast.LENGTH_SHORT).show();
                customerPuzzleCode.reSetPuzzleCode();
                sebSliding.setProgress(0);
            }
        });

        sebSliding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                customerPuzzleCode.setCurrentSliderValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                sebSliding.setMax(customerPuzzleCode.getMaxSlidingValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                customerPuzzleCode.matchSlider();
            }
        });
    }

    @OnClick({R.id.btn_change_code, R.id.seb_sliding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_code:
                customerPuzzleCode.createPuzzleCode();
                sebSliding.setEnabled(true);
                sebSliding.setProgress(0);
                break;
            case R.id.seb_sliding:
                break;
        }
    }
}
