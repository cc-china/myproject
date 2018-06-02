package com.my_project.test_player.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.my_project.R;
import com.my_project.test_player.model.SwitchVideoModel;
import com.my_project.test_player.view.SampleVideo;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nolan on 2017/9/21.
 */

public class TestPlayerActivity extends Activity {
    @Bind(R.id.video_player)
    SampleVideo videoPlayer;
    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_player);
        ButterKnife.bind(this);
        init("http://rautumn.oss-cn-shanghai.aliyuncs.com/video/父zx兵.mp4");
//        init("http://rautumn.oss-cn-shanghai.aliyuncs.com/rautumn%2FiOS%2Fvideo%2F1505964706.18523.mov");
//        init("http://rautumn.oss-cn-shanghai.aliyuncs.com/rautumn/android/video/1505901343961.mp4");
    }

    private void init(String videoUrl) {

        List<SwitchVideoModel> list = new ArrayList<>();
        SwitchVideoModel switchVideoModel = new SwitchVideoModel("", videoUrl);
        list.add(switchVideoModel);
        videoPlayer.setUp(list, true, "");
        //增加封面
//        Glide.with(this).load(videoInfoVideoUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                ImageView imageView = new ImageView(VideoPlayerDetailsActivity.this);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setImageBitmap(resource);
//                videoPlayer.setThumbImageView(imageView);
//            }
//        });
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getTitleTextView().setText("视频");
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        videoPlayer.setClickAllHideView(false);
        //隐藏进度条
        videoPlayer.setHideKey(false);
        //设置全屏按键功能---短视频里禁止全屏播放
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(false);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //系统返回键功能
                onBackPressed();
            }
        });
        //过渡动画
        initTransition();
    }

    private void initTransition() {
        videoPlayer.startPlayLogic();
    }
}
