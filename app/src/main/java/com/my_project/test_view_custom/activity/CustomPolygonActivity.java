package com.my_project.test_view_custom.activity;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.my_project.R;
import com.my_project.test_view_custom.model.AbilityResultBean;
import com.my_project.test_view_custom.view.CustomAbilityView;
import com.my_project.test_view_custom.view.CustomerCircle;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017\12\7 0007.
 */

public class CustomPolygonActivity extends Activity {
    @Bind(R.id.view_polygon)
    CustomAbilityView viewPolygon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test();
        setContentView(R.layout.activity_custom_polygon);
        ButterKnife.bind(this);
        init() ;
        Log.e("11111-oncreate",test()+"");
        //设置源数据
        viewPolygon.setData(new AbilityResultBean(65, 70, 80, 70, 80, 80, 80));
    }

    private int test() {
        
        try {
            Log.e("11111_1","1");
            return 1;
        } catch (Exception e) {
            Log.e("11111_e",e.toString());
            return 2;
        }finally {
            return 3;
        }
    }

    private void init() {
        final CustomerCircle c_circle = findViewById(R.id.c_circle);
        c_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0, 200, 0, 500);
                animation.setDuration(2000);
                animation.setFillAfter(true);

                Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(2000);
                rotateAnimation.setFillAfter(true);

                AnimationSet set = new AnimationSet(true);
                set.addAnimation(animation);
                set.addAnimation(rotateAnimation);
                set.setDuration(2000);
                set.setFillAfter(true);
                c_circle.startAnimation(set);
            }
        });

    }
}
