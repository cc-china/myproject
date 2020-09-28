package com.my_project.test_jni;

/**
 * Created by com_c on 2018/3/8.
 */

public class JNIUtils {
    static {
//        System.loadLibrary("testJNI");
        System.loadLibrary("linkJNI");
    }

    //JNI接口需要用native关键字修饰
    public native String getName(String str);
    public native int getBackTotalNum(int x,int y);
    public native String getBackText(String str);
    public static native byte[] setByteArrayData(byte[] byteArrayData);
    public static native void setObjectData(JniObjResult jniObjResult);
}
