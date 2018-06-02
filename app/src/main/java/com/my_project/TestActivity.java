package com.my_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.my_project.test_image_choose.activity.TestImageChooseActivity;
import com.my_project.test_player.activity.TestPlayerActivity;
import com.my_project.test_progressbar.ui.TestProgressBar;
import com.my_project.test_pulish_video.activity.TestPulishVideoActivity;
import com.my_project.test_refreing_data.activity.TestRefershingDataActivity;
import com.my_project.test_system_video.activity.TestVideoActivity;
import com.my_project.test_touch_event.activity.TestTouchEventActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_test_refershing, R.id.btn_test_player
            , R.id.btn_test_progressBar, R.id.btn_test_video
            , R.id.btn_test_touch_event, R.id.btn_test_pulish_video
            , R.id.btn_test_image_more_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test_refershing:
                startActivity(new Intent(this, TestRefershingDataActivity.class));
                break;
            case R.id.btn_test_player:
                startActivity(new Intent(this, TestPlayerActivity.class));
                break;
            case R.id.btn_test_progressBar:
                startActivity(new Intent(this, TestProgressBar.class));
                break;
            case R.id.btn_test_video:
                startActivity(new Intent(this, TestVideoActivity.class));
                break;
            case R.id.btn_test_touch_event:
                startActivity(new Intent(this, TestTouchEventActivity.class));
                break;
            case R.id.btn_test_pulish_video:
                startActivity(new Intent(this, TestPulishVideoActivity.class));
                break;
            case R.id.btn_test_image_more_choose:
                startActivity(new Intent(this, TestImageChooseActivity.class));
                break;

        }
    }
}
