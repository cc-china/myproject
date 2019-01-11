package com.my_project.test_bug;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by com_c on 2018/7/5.
 */

public class TempraryActivity extends Activity {
    private final Integer END = Integer.MAX_VALUE;
    private final Integer START = END - 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        int count = 0;
        for (int i = START; i <= END; i++) {
            count++;
        }
        Toast.makeText(this, "" + count, Toast.LENGTH_SHORT).show();
    }

}
