package com.my_project.test_image_choose.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.my_project.R;
import com.my_project.test_image_choose.adapter.AddMyDataImageAdapter;
import com.my_project.test_image_choose.interfaces.AddMyDataImagerClickInterface;
import com.my_project.test_image_choose.interfaces.DeletePhotosListener;
import com.my_project.test_image_choose.model.PhotoInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nolan on 2017/10/16.
 */

public class TestImageChooseActivity extends Activity {
    @Bind(R.id.recycleview)
    RecyclerView recycleview;
    private List<PhotoInfo> photosLists = new ArrayList<>();
    private static final int PHOTO_SIZE = 12;//图片限制的个数
    private AddMyDataImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_choose);
        ButterKnife.bind(this);
        initAdapter();
    }
    /////////////////////- 编辑资料的操作 -/////////////////////////
    private List<String> deleteList = new ArrayList<>();
    private String photoType;//1照片
    int type = 2;
    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleview.setLayoutManager(manager);
        adapter = new AddMyDataImageAdapter(this, type, photosLists, PHOTO_SIZE);
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new AddMyDataImagerClickInterface() {
            @Override
            public void onItemClick(int position) {
            }
        });
        adapter.setDeletePhotosListener(new DeletePhotosListener() {
            @Override
            public void deletePhoto(String url) {
                deleteList.add(url);
            }

            @Override
            public void photoPosition(int postion) {
                photosLists.remove(postion);
            }
        });
    }
}
