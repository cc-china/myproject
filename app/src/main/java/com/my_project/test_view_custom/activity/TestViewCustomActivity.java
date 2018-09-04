package com.my_project.test_view_custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.my_project.R;
import com.my_project.test_view_custom.adapter.CustomViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\16 0016.
 */

public class TestViewCustomActivity extends Activity {
    @Bind(R.id.recycleview)
    RecyclerView recycleview;
    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view_custom);
        ButterKnife.bind(this);
        initData();
        initAdapter();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add("自定义正七边形view");
        mList.add("自定义属性");
        mList.add("音量调节的自定义view");
        mList.add("ViewGrounp自定义view");
        mList.add("仿斗鱼拼图验证码");
    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(this,2);
        recycleview.setLayoutManager(manager);
        recycleview.setAdapter(new CustomViewAdapter(this,mList));
    }
}
