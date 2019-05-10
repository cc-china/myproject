package com.my_project.test_refreing_data.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.caoyun.xrecycleview.GridSpacingItemDecoration;
import com.caoyun.xrecycleview.ProgressStyle;
import com.caoyun.xrecycleview.XRecyclerView;
import com.my_project.R;
import com.my_project.test_refreing_data.adapter.TestRefreshingAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nolan on 2017/9/14.
 */

public class TestRefershingDataActivity extends Activity {
    @Bind(R.id.recyclerview)
    XRecyclerView recyclerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refershing);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        final List<String> mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add("大唐第个" + i + "盛世");
        }
        int spanCount = 4;//跟布局里面的spanCount属性是一致的
        int spacing = 2;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距s
        //设置每个item间距
        recyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(manager);
        final TestRefreshingAdapter adapter = new TestRefreshingAdapter(this, mList);
        recyclerview.setAdapter(adapter);

        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerview.setArrowImageView(R.mipmap.iconfont_downgrey);
        //添加recycleView的头部
        recyclerview.addHeaderView(View.inflate(this,R.layout.view_recycleview_header_test,null));

        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.add(0, "这是下拉刷新出来的数据");
                        adapter.notifyDataSetChanged();
                        recyclerview.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.add(mList.size(), "这是上拉加载更多出来的数据");
                        adapter.notifyDataSetChanged();
                        recyclerview.refreshComplete();
                    }
                }, 1000);
            }
        });
    }
}
