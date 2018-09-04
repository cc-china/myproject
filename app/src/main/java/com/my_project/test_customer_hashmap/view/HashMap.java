package com.my_project.test_customer_hashmap.view;

import java.util.Map;

/**
 * Created by com_c on 2018/7/31.
 */

public class HashMap {
    /**
     * 初始容量，默认16
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    /**
     * 最大初始容量，2^30
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * 负载因子，默认0.75，负载因子越小，hash冲突几率越低
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    /**
     * 临界值
     */
    private int threshold;
    /**
     * 初始化一个Entry的空数组
     */
    transient HashEntry[] table;

    public HashMap() {
        table = new HashEntry[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    /**
     * put方法
     * */
    public void put(Object key,Object value){
        if (table ==null){

        }
    }
    class HashEntry{}
}
