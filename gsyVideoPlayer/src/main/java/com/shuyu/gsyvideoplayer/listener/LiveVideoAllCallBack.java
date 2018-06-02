package com.shuyu.gsyvideoplayer.listener;

/**
 * Created by Administrator on 2017/2/24.
 */
public interface LiveVideoAllCallBack  extends VideoAllCallBack{
    //暂时保留standardVideo方法
    //点击了空白区域开始播放
    void onClickStartThumb(String url, Object... objects);

    //点击了播放中的空白区域
    void onClickBlank(String url, Object... objects);

    //点击了全屏播放中的空白区域
    void onClickBlankFullscreen(String url, Object... objects);
}
