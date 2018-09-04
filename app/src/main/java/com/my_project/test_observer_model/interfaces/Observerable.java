package com.my_project.test_observer_model.interfaces;

/**
 * Created by com_c on 2018/6/2.
 * 被观察者 ---报社
 */

public interface Observerable {
    /*
       * 三个方法：1、客户订阅报纸
       * 2、客户取消报纸
       * 3、报社通知客户有新报纸
       * */
    void registerObserver(Observer o);

    void removeObServer(Observer o);

    void notifyObServer();
}
