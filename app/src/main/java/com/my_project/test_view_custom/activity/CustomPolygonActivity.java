package com.my_project.test_view_custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my_project.R;
import com.my_project.test_view_custom.model.AbilityResultBean;
import com.my_project.test_view_custom.view.CustomAbilityView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\12\7 0007.
 */

public class CustomPolygonActivity extends Activity {
    @Bind(R.id.view_polygon)
    CustomAbilityView viewPolygon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_polygon);
        ButterKnife.bind(this);
        //设置源数据
        viewPolygon.setData(new AbilityResultBean(65, 70, 80, 70, 80, 80, 80));
    }
}
