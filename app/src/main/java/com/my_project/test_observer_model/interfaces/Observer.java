package com.my_project.test_observer_model.interfaces;

/**
 * Created by com_c on 2018/6/2.
 * 观察者 ---客户
 */

public interface Observer {
    //接受报社的通知
    void update(String msg);
}
