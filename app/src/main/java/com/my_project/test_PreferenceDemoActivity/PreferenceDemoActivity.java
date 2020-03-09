package com.my_project.test_PreferenceDemoActivity;

/**
 * Created by cmm on 2020/3/6.
 */

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.my_project.R;

public class PreferenceDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_fragment);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl, new PreferenceDemoFragment()).commit();
    }
}
