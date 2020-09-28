#include <jni.h>
#include <string>
#include <android/log.h>
#define  LOG_TAG    "native-dev"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C"{
 jint getBackTotalNum(JNIEnv * env,jobject job,jint x,jint y){
    LOGI("getBackTotalNum,x is :%d,y is :%d",x,y);
    return x+y;
 }

 jstring getBackText(JNIEnv * env,jobject job,jstring str){
    const char * str1 = env->GetStringUTFChars(str,0);
    LOGI("getBackText,jstring is :%s",str1);
    env->ReleaseStringUTFChars(str,str1);
    return env->NewStringUTF(str1);
 }

 jobject setObjectData(JNIEnv * env,jobject job,jobject objData){

    //根据全路径类名查找到java对象
    jclass obj = env->FindClass("com/my_project/test_jni/JniObjResult");
    //根据java对象、成员属性名、属性签名查找 到对象的成员变量
    jfieldID jfieldID = env->GetFieldID(obj,"byteArray","[B");
    jbyteArray byteArray = (jbyteArray)env->GetObjectField(objData, jfieldID);
    //获取数组长度
    int byteArrayLength = env->GetArrayLength(byteArray);
    //将jni类型转变为char *指针
    char* pByteArray = (char*)env->GetByteArrayElements(byteArray,0);
    //申请数组，从指针中取出每一个字符
        jbyte arr[byteArrayLength];
        for(int i =0;i<byteArrayLength;i++){
            arr[i] = pByteArray[i];
            LOGI("setObjectData,byteArray is :%d",arr[i]);
        }
        //再从c代码中取出数组，返回给java
        jbyteArray c_result = env->NewByteArray(byteArrayLength);
        env->SetByteArrayRegion(c_result,0,byteArrayLength,arr);
        jclass objBack = env->FindClass("com/my_project/test_jni/JniObjResult");
        //创建java对象
        // objBack的默认构造函数
        jmethodID mthodidDataConstructor = env->GetMethodID(objBack, "<init>", "()V");
        jobject objDate = env->NewObject(objBack, mthodidDataConstructor);
        //释放内存
        //env->ReleaseByteArrayElements(byteArry,byteArray,0);

        return objDate;
 }

jbyteArray setByteArrayData(JNIEnv * env,jobject job,jbyteArray byteArry){
    //获取数组长度
    int byteArryLength = env->GetArrayLength(byteArry);
    LOGI("setByteArryrdata,byteArryLength is :%d",byteArryLength);
    //获取数组指针
    signed char* byteArray=(signed char*)env->GetByteArrayElements(byteArry,0);

    //申请数组，从指针中取出每一个字符
    jbyte arr[byteArryLength];
    for(int i =0;i<byteArryLength;i++){
        arr[i] = byteArray[i];
        LOGI("setByteArryrdata,byteArray is :%d",arr[i]);
    }
    //再从c代码中取出数组，返回给java
    jbyteArray c_result = env->NewByteArray(byteArryLength);
    env->SetByteArrayRegion(c_result,0,byteArryLength,arr);
    //释放内存
    env->ReleaseByteArrayElements(byteArry,byteArray,0);
    return c_result;
}
 const char * className = "com/my_project/test_jni/JNIUtils";
 const JNINativeMethod getMethods[] = {
        {"getBackTotalNum","(II)I",(void*) getBackTotalNum}
        ,{"getBackText","(Ljava/lang/String;)Ljava/lang/String;",(void*)getBackText}
        ,{"setByteArrayData","([B)[B",(void*)setByteArrayData}
        ,{"setObjectData","(Lcom/my_project/test_jni/JniObjResult;)Lcom/my_project/test_jni/JniObjResult;",(void*)setObjectData}
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
