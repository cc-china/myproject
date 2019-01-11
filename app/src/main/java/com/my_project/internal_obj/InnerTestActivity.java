package com.my_project.internal_obj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.my_project.internal_obj.interfaces.IOnClick;

/**
 * Created by Administrator on 2018\10\11 0011.
 */

public class InnerTestActivity extends Activity{
    private IOnClick onClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
    }

    private View setView() {
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Button button = new Button(this);
        button.setText("外部类内部类相互调用");
        final int num = 0;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InnerTestActivity.this.show(num);
            }
        });
        layout.addView(button);
        return layout;
    }

    private void show(int a) {
        onClick.onClick(a);
    }


    public void setOnClick(IOnClick click) {
        this.onClick = click;
        show(1);
    }
}
