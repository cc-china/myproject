package com.my_project.test_more_listview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.my_project.R;
import com.my_project.sqlitedatabase.SQLiteDB;
import com.my_project.test_more_listview.DBModel;
import com.my_project.test_more_listview.adapter.AutoEViewAdapter;
import com.my_project.test_more_listview.adapter.OrganizationPersonsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\11\24 0024.
 */

public class OrganizationPersonsListActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.auto_editview)
    AutoCompleteTextView autoEditview;
    @Bind(R.id.recycleview)
    RecyclerView recycleview;
    private List<DBModel> personData;
    private SQLiteDB sqLiteDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_personlist);
        ButterKnife.bind(this);
        sqLiteDB = new SQLiteDB(this);
        getIntentData();
        setToolbar();
        initView();
    }

    private void initView() {
        /*
        * 初始化autoCompleteTextView
        * */
            ArrayList<String> str = new ArrayList<>();
            sqLiteDB.openDB();
            //查询具体某条数据
//        DBModel data = sqLiteDB.findOneData("北京市人民政府");
            List<DBModel> data = sqLiteDB.findAllData();
            for (int i = 0; i < data.size(); i++) {
                str.add(data.get(i).getUserName());
            }
            sqLiteDB.destoryInstance();
            AutoEViewAdapter autoEViewAdapter = new AutoEViewAdapter(this, str);
            autoEditview.setAdapter(autoEViewAdapter);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(manager);
        OrganizationPersonsAdapter adapter = new  OrganizationPersonsAdapter(this,personData);
        recycleview.setAdapter(adapter);
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
        personData = (List<DBModel>) getIntent().getSerializableExtra("personData");
    }
}
