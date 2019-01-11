package com.my_project.test_aidl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2018\11\5 0005.
 */

public class AidlActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
    }

    private View getView() {
        TextView textView = new TextView(this);

        return textView;
    }
}
