package com.my_project.test_more_listview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.my_project.R;
import com.my_project.test_more_listview.DBModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\24 0024.
 */

public class OrganizationDetails extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_organization_name)
    TextView tvOrganizationName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        ButterKnife.bind(this);
        getIntentData();
        setToolbar();
    }

    private void setToolbar() {
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getIntentData() {
        DBModel itemData = (DBModel) getIntent().getSerializableExtra("itemData");
        tvOrganizationName.setText(itemData.getUserName());
    }
}
