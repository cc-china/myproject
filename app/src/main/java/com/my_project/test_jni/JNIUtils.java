package com.my_project.test_jni;

/**
 * Created by com_c on 2018/3/8.
 */

public class JNIUtils {
    static {
        //注意名字需要跟你的build.gradle节点下面的名字一样
//        System.loadLibrary("testJNI");
        System.loadLibrary("linkJNI");
    }

    //JNI接口需要用native关键字修饰
    public native String getName(String str);
    public native int getBackTotalNum(int x,int y);
    public native String getBackText(String str);
}
