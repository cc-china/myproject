package com.my_project.test_observer_model;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import com.my_project.test_observer_model.interfaces.Observer;
import com.my_project.test_observer_model.interfaces.Observerable;


/**
 * Created by com_c on 2018/6/2.
 */

public class custom1 implements Observer {

    private Activity ctx;
    private Observerable observerable;
    private String msg;

    public custom1(Activity ctx,Observerable observerable) {
        this.observerable = observerable;
        this.ctx = ctx;
        observerable.registerObserver(this);
    }

    @Override
    public void update(String msg) {
        this.msg = msg;
        Snackbar.make(ctx.getWindow().getDecorView(),msg,Snackbar.LENGTH_LONG).show();
    }
}
