package com.my_project.test_anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.my_project.R;

/**
 * Created by com_c on 2018/8/27.
 */

public class TestAnimActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_anim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        btn_anim = findViewById(R.id.btn_anim);
        btn_anim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(
                btn_anim, "translationX", 0, 600f);
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(
                btn_anim, "translationY", 0, -600f);

        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX, translationY); //设置动画
        animatorSet.setDuration(3000);  //设置动画时间
        animatorSet.start(); //启动


        ObjectAnimator ra = ObjectAnimator.ofFloat(
                btn_anim, "rotation", 0f, 360f);
        ra.setDuration(3000);
        ra.start();
    }
}
