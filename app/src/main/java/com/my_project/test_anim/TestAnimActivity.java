package com.my_project.test_anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.my_project.R;
import com.my_project.internal_obj.TestActivity;

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        ObjectAnimator translationT = new ObjectAnimator().ofFloat(
                btn_anim, "translationY", 200f, -200f);
        ObjectAnimator translationB = new ObjectAnimator().ofFloat(
                btn_anim, "translationY", 0, 200f);
        ObjectAnimator reSetY = new ObjectAnimator().ofFloat(
                btn_anim, "translationY", -200f, 0);

        AnimatorSet set = new AnimatorSet();  //组合动画
        set.play(translationT).after(translationB).before(reSetY);
        set.setDuration(2000);  //设置动画时间
        set.start(); //启动


        ObjectAnimator translationR = new ObjectAnimator().ofFloat(
                btn_anim, "translationX", 0, 200f);
        ObjectAnimator translationL = new ObjectAnimator().ofFloat(
                btn_anim, "translationX", 200f, -200f);
        ObjectAnimator reSetX = ObjectAnimator.ofFloat(
                btn_anim, "translationX", -200f,0);

        AnimatorSet set1 = new AnimatorSet();  //组合动画
        set1.play(translationL).after(translationR).before(reSetX);
        set1.setDuration(2000);  //设置动画时间
        set1.start(); //启动
    }

}
