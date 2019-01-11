package com.my_project.internal_obj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.my_project.internal_obj.interfaces.IOnClick;
import com.my_project.internal_obj.obj.AnimObj;
import com.my_project.internal_obj.obj.DogObj;

public class TestActivity extends AppCompatActivity {

    private String age;
    private AnimObj dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        AnimObj anim = new DogObj();
        anim.getName();
    }

    private void show(IOnClick iOnClick) {
        iOnClick.onClick(1);
    }

    private RelativeLayout setView() {
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        final Button button = new Button(this);
        button.setGravity(Gravity.CENTER);
        button.setText("测试");
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                show(new IOnClick() {
//                    @Override
//                    public void onClick(int count) {
//                        Toast.makeText(TestActivity.this,
//                                age + "",
//                                Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                });
                button.setText(new Outer().setCode(""));
            }
        });
        return layout;
    }


}
