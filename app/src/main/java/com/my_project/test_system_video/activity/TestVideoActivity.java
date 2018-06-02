package com.my_project.test_system_video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.my_project.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nolan on 2017/9/29.
 */

public class TestVideoActivity extends Activity {
    private final int REQUEST_VIDEO_CODE = 1004;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.btn_video)
    public void onClick() {
        //去系统相册选取
        Intent intent;
        //获取之前拍摄好的小视频
        if (Build.VERSION.RELEASE.equals("4.3")) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_PICK);
        }
        //相片类型  MediaStore.EXTRA_OUTPUT
        intent.setDataAndType(
                MediaStore.getMediaScannerUri(), "video/*");
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_VIDEO_CODE:

                break;
        }
    }
}
