//
// Created by com_c on 2018/3/8.
//
#include <jni.h>
#include "com_my_project_test_jni_JNIUtils.h"
JNIEXPORT jstring JNICALL Java_com_my_1project_test_1jni_JNIUtils_getName
        (JNIEnv * env, jobject jobject){
            return (*env)->NewStringUTF(env,"大唐盛世!!");
 }
