#include <jni.h>
#include <string>
#include <iostream>
#include <android/log.h>
using namespace std;

extern "C"{
 jint getBackTotalNum(JNIEnv * env,jobject job,jint x,jint y){
    printf("getBackTotalNum,x is :%d,y is :%d",x,y);
    return x+y;
 }
 class Test{
     Test();
     ~Test();
     public: static void tost(){
         std::cout << "1111111111111111-------------3";
     }
  };

 jstring getBackText(JNIEnv * env,jobject job,jstring str){
    const char * str1 = env->GetStringUTFChars(str,0);
    printf("getBackText,jstring is :%s",str1);
    env->ReleaseStringUTFChars(str,str1);
    Test::tost();
    return env->NewStringUTF(str1);
 }

 const char * className = "com/my_project/test_jni/JNIUtils";
 const JNINativeMethod getMethods[] = {
        {"getBackTotalNum","(II)I",(void*) getBackTotalNum}
        ,{"getBackText","(Ljava/lang/String;)Ljava/lang/String;",(void*)getBackText}
        };

 jint RegisterNative(JNIEnv* env){
    //获取映射的Java类
    jclass javaClass = env->FindClass(className);
    if(javaClass==NULL){
        printf("cannot get class : %s\n" ,className);
        return -1;
    }
    return env->RegisterNatives(javaClass,getMethods,sizeof(getMethods)/sizeof(getMethods[0]));
 }

 //jni函数入口，类似main函数
 jint JNI_OnLoad(JavaVM* vm,void* reserved){
    printf("jni_onload_start");
    //注册时在JNIEnv中实现，所以必须首先获取它
    JNIEnv* env = NULL;
    jint result = -1;
    //从JavaVM中获取JNIEnv，一般使用1.4的版本
    if(vm->GetEnv((void **)&env,JNI_VERSION_1_4)!=JNI_OK){
        return result;
    }
    result = RegisterNative(env);
    printf("RegisterNatives result:%d",result);
    printf("jni_onload_end");
    return JNI_VERSION_1_4;//必须返回版本，否则加载会失败
 }
}
