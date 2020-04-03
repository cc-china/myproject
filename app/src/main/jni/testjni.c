//
// Created by com_c on 2018/3/8.
//
#include <jni.h>
#include "com_my_project_test_jni_JNIUtils.h"
JNIEXPORT jstring JNICALL Java_com_my_1project_test_1jni_JNIUtils_getName
        (JNIEnv * env, jobject jobject,jstring str){
        const char * str1 = (*env)->GetStringUTFChars(env,str,0);
            return (*env)->NewStringUTF(env,str1);
//            return (*env)->NewStringUTF(env,"大唐盛世!!");
 }
