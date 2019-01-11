package com.my_project.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my_project.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\13 0013.
 */

public class HomePageFragment extends Fragment {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<String> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_page, null);
        ButterKnife.bind(this, view);
        initData();
        initAdapter();
        return view;
    }

    private void initData() {
        data = new ArrayList<>();
        data.add("测试recycleview刷新");
        data.add("测试播放器");
        data.add("测试进度条");
        data.add("测试系统相册视频");
        data.add("监听控件的触摸事件");
        data.add("仿快手上传视频");
        data.add("图片多选器");
        data.add("自定义View");
        data.add("多层级listview联动");
        data.add("事件分发与拦截机制");
        data.add("仿斗鱼滑动拼图验证码View");
        data.add("JNI调用");
        data.add("RXJava使用");
        data.add("仿小米日历选择器");
        data.add("MPAndroidChart图表类使用");
        data.add("JavaIO流操作");
        data.add("测试");
        data.add("单链表反转");
        data.add("NFC读取写入");
        data.add("自定义相册拍照");
        data.add("MatericalDialog");
        data.add("测试自定义更新");
        data.add("自定义HashMap简化实现");
        data.add("MVP模型");
        data.add("Android多线程通信");
        data.add("冒泡快排二分法查找等算法");
        data.add("Android动画");
        data.add("子线程创建Handler");
        data.add("aidl服务端");
        data.add("H5页面与Android相互调用");
        data.add("人脸识别SDK");
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(new HomePageAdapter(getActivity(), data));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
