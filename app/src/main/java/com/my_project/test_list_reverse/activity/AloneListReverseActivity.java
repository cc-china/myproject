package com.my_project.test_list_reverse.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my_project.R;
import com.my_project.test_list_reverse.model.Node;

/**
 * Created by Administrator on 2018\5\19 0019.
 */

public class AloneListReverseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        initData();
    }

    private void initData() {
        Node node = new Node(0);
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
    }

}
