package com.my_project.test_more_thread.goods;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2019\2\25 0025.
 * 生产者和消费者先后处理的product资源是同一个，要确保这一点，
 * 可以按单例模式来设计product类，也可以将同一个product对象通过构造方法传递给生产者和消费者，
 */

public class Products_2 {
    private String name;
    public Products_2(String name, int i) {
        this.name = name + i;
    }
    public String getName() {
        return name;
    }

}
