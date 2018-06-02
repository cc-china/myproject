package com.my_project.test.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.my_project.R;
import com.my_project.test.adapter.FashionBannerPagerAdapter;
import com.my_project.test.view.CustomViewpager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by com_c on 2017/12/31.
 */

public class TestMainActivity extends Activity {
    @Bind(R.id.vp_pager)
    CustomViewpager vpPager;
    private FashionBannerPagerAdapter bannerAdapter;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        ButterKnife.bind(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    private void initView() {
        int [] mList = {R.mipmap.a,R.mipmap.b,R.mipmap.c,R.mipmap.d};
        settingShufflingFigure(vpPager,mList);
        mHandler.sendEmptyMessageDelayed(2, 4000);
    }
    //设置轮播图
    private void settingShufflingFigure(CustomViewpager vpPager, int[] mList) {
        vpPager.setmPager(vpPager);
        bannerAdapter = new FashionBannerPagerAdapter(TestMainActivity.this, mList);
        vpPager.setAdapter(bannerAdapter);
        vpPager.setOnPageChangeListener(onPageChangeListener);
    }
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            int index = arg0 % bannerAdapter.getList().length;
//            dianCheck(index);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    // TODO 循环转动
                    int pagerIndex = vpPager.getCurrentItem();
                    mHandler.removeMessages(2);
                    vpPager.setCurrentItem(pagerIndex + 1);
                    mHandler.sendEmptyMessageDelayed(2, 2000);
                    break;
            }
        }
    };
}
