package com.my_project.test_observer_model;


import com.my_project.test_observer_model.interfaces.Observer;
import com.my_project.test_observer_model.interfaces.Observerable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by com_c on 2018/6/2.
 */

public class Canvas implements Observerable {

    private List<Observer> observers;

    public Canvas() {
        //创建观察者列表
        observers = new ArrayList();
    }

    public void setData() {
        notifyObServer();
    }

    //观察者客户订阅报纸（被观察者）
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    //观察者客户取消订阅报纸（被观察者）
    @Override
    public void removeObServer(Observer o) {
        int indexOf = observers.indexOf(o);
        if (indexOf > 0) {
            observers.remove(indexOf);
        }
    }

    //被观察者  报社 给观察者 客户 发报纸
    @Override
    public void notifyObServer() {
        for (int i = 0; i < observers.size(); i++) {
            Observer o = observers.get(i);
            o.update("seng msg------" + i);
        }
    }
}
