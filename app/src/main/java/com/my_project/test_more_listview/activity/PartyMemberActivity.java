package com.my_project.test_more_listview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.my_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\28 0028.
 */

public class PartyMemberActivity extends AppCompatActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_peoples)
    TextView tvPeoples;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_educational)
    TextView tvEducational;
    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.tv_duty)
    TextView tvDuty;
    @Bind(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_member);
        ButterKnife.bind(this);
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
}
