/*******************************************************************************
Copyright(c) 2017-2019 Xi'an PI  information technology Co. Ltd, All right reserved.

DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER
AUTHORIZATION.

*******************************************************************************/
#define LOG_TAG "JniXapiCalib"

#include <android/log.h>
#include <stdio.h>
#include <fstream>
#include <string.h>

#include "jni.h"
#include "XapiCalibImpl.h"

using namespace std;
using namespace android;

static XapiCalibImpl* gCalibImpl = NULL;

#define TAG "JniXapiCalib"

#define JNI_CALIB_LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define JNI_CALIB_LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define JNI_CALIB_LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)
#define LOG_DEBUG true

static jboolean nativeInit(JNIEnv *env, jobject thiz, jstring paramFile, jint socket_on)
{
    (void)env;
    (void)thiz;
    bool ret = false;
    char* xmlFile = NULL;
    int strLen = 0;

    xmlFile = (char*)env->GetStringUTFChars(paramFile, JNI_FALSE);
    if (xmlFile == NULL) {
        JNI_CALIB_LOGE("xmlFile = NULL err!");
        goto out;
    }
    strLen = strlen(xmlFile);
    if (strLen == 0) {
        JNI_CALIB_LOGE("xmlFileName strLen = 0 err!");
        goto out;
    }

    JNI_CALIB_LOGI("native init begin");
    gCalibImpl = new XapiCalibImpl();
    if (gCalibImpl == NULL) {
        goto out;
    }
    ret = gCalibImpl->init(xmlFile, socket_on);
out:
    if (xmlFile)
        env->ReleaseStringUTFChars(paramFile, xmlFile);
    JNI_CALIB_LOGI("nativeInit ret=%d", ret);
    return ret;
}

static void nativeUnInit(JNIEnv *env, jobject thiz)
{
    (void)env;
    (void)thiz;

    JNI_CALIB_LOGI("native uninit begin");

    if (gCalibImpl != NULL) {
        delete gCalibImpl;
        gCalibImpl = NULL;
    }

    JNI_CALIB_LOGI("nativeUninit ok");
}

static void nativeSendResult(JNIEnv *env, jobject thiz, jint result)
{

    (void)env;
    (void)thiz;
    JNI_CALIB_LOGI("native nativeSendResult begin,result = %d", result);

    if (gCalibImpl != NULL) {
        gCalibImpl->CalibSendResult(result);
    }

    JNI_CALIB_LOGI("nativeSendResult end");
}

static jboolean nativeSelfCalibration(JNIEnv *env, jobject thiz,
                                            jbyteArray leftBuffer, jbyteArray rightBuffer,
                                            jobject result,
                                            jstring xmlFileName, jint af_calib)
{
    bool ret = false;
    char* xmlFile = NULL;
    int strLen = 0;

    int leftBufferLen = 0;
    int rightBufferLen = 0;
    unsigned char* leftBufferData = NULL;
    unsigned char* rightBufferData = NULL;
    unsigned char *byteParamData = NULL;
    char *byteInfoData = NULL;
    jfieldID bufferParamField;
    jfieldID bufferInfoField;
    jclass xapiCalibResultClass;
    jbyteArray jParamData = NULL;
    jbyteArray jInfoData = NULL;

#if 0
    jbyteArray jByteParam = env->NewByteArray(2048);
    jbyteArray jByteInfo = env->NewByteArray(1024);
    unsigned char *byteParamData = (unsigned char*)env->GetByteArrayElements(jByteParam, 0);
    char *byteInfoData = (char*)env->GetByteArrayElements(jByteInfo, 0);
#endif

    (void)thiz;
    if(gCalibImpl == NULL) {
        JNI_CALIB_LOGE("gCalibImpl is NULL");
        return false;
    }

    xmlFile = (char*)env->GetStringUTFChars(xmlFileName, JNI_FALSE);
    if (xmlFile == NULL) {
        JNI_CALIB_LOGE("xmlFile = NULL err!");
        goto out;
    }

    strLen = strlen(xmlFile);
    if (strLen == 0) {
        JNI_CALIB_LOGE("xmlFileName strLen = 0 err!");
        goto out;
    }

    if (LOG_DEBUG) {
        JNI_CALIB_LOGI("xmlFileName : %s", xmlFile);
    }

    leftBufferLen = env->GetArrayLength(leftBuffer);
    rightBufferLen = env->GetArrayLength(rightBuffer);
    if (leftBufferLen == 0 || rightBufferLen == 0) {
        JNI_CALIB_LOGE("input Buffer len =  0 err!");
        goto out;
    }

    leftBufferData = (unsigned char*)env->GetByteArrayElements(leftBuffer, 0);
    rightBufferData = (unsigned char*)env->GetByteArrayElements(rightBuffer, 0);
    if (NULL == leftBufferData || NULL == rightBufferData) {
        JNI_CALIB_LOGE("input Buffer is NULL err!");
        goto out;
    }

    xapiCalibResultClass = env->FindClass(
            "com/xapi/calib/feature/mode/vsdof/photo/jni_xapi/XapiCalibResult");
    if (xapiCalibResultClass == NULL) {
        JNI_CALIB_LOGE("<nativeSelfCalibration><error> can't find class xapiCalibResult");
        ret = false;
        goto out;
    }

    bufferParamField = env->GetFieldID(xapiCalibResultClass, "byteParam", "[B");
    bufferInfoField = env->GetFieldID(xapiCalibResultClass, "byteInfo", "[B");

    jParamData = (jbyteArray)env->GetObjectField(result, bufferParamField);
    if (jParamData == NULL) {
        JNI_CALIB_LOGE("<nativeSelfCalibration><error>  jParamData");
        goto out;
    }
    jInfoData = (jbyteArray)env->GetObjectField(result, bufferInfoField);
    if (jInfoData == NULL) {
        JNI_CALIB_LOGE("<nativeSelfCalibration><error>  jInfoData");
        goto out;
    }

    byteParamData = (unsigned char*)env->GetByteArrayElements(jParamData, 0);
    if (NULL == byteParamData) {
        JNI_CALIB_LOGE("<nativeSelfCalibration><error>  byteParamData");
        goto out;
    }
    byteInfoData = (char*)env->GetByteArrayElements(jInfoData, 0);
    if (NULL == byteInfoData) {
        JNI_CALIB_LOGE("<nativeSelfCalibration><error>  byteInfoData");
        goto out;
    }

    JNI_CALIB_LOGE(" byteParamData %#x %#x %#x %#x",
                byteParamData[0], byteParamData[1], byteInfoData[0], byteInfoData[1]);

    ret = gCalibImpl->selfCalibration(leftBufferData, rightBufferData, byteParamData, xmlFile, byteInfoData, af_calib);
    {
        env->SetByteArrayRegion(jParamData, 0, 2048, (jbyte*)byteParamData);
        env->SetByteArrayRegion(jInfoData, 0, 1024, (jbyte*)byteInfoData);
    }

    JNI_CALIB_LOGI("nativeSelfCalibration ret=%d", ret);

out:
    if (rightBufferData)
        env->ReleaseByteArrayElements(rightBuffer, (jbyte *)rightBufferData, 0);
    if (leftBufferData)
        env->ReleaseByteArrayElements(leftBuffer, (jbyte *)leftBufferData, 0);
    if (xmlFile)
        env->ReleaseStringUTFChars(xmlFileName, xmlFile);
    if (jParamData)
        env->ReleaseByteArrayElements(jParamData, (jbyte *)(byteParamData), 0);
    if (jInfoData)
        env->ReleaseByteArrayElements(jInfoData, (jbyte *)(byteInfoData), 0);

    return ret;
}

static jboolean nativeSelfRectify(JNIEnv *env, jobject thiz,
                                            jbyteArray leftBuffer, jbyteArray rightBuffer,
                                            jbyteArray byteParam, jstring xmlFileName,
                                            jbyteArray jByteInfo)
{
    bool ret = false;
    char* xmlFile = NULL;
    int strLen = 0;

    int leftBufferLen = 0;
    int rightBufferLen = 0;
    unsigned char* leftBufferData = NULL;
    unsigned char* rightBufferData = NULL;
    unsigned char* byteParamData = NULL;
    char *byteInfo = NULL;

    (void)thiz;
    if(gCalibImpl == NULL) {
        JNI_CALIB_LOGE("gCalibImpl is NULL");
        return ret;
    }

    xmlFile = (char*)env->GetStringUTFChars(xmlFileName, JNI_FALSE);
    if (xmlFile == NULL) {
        JNI_CALIB_LOGE("xmlFile = NULL err!");
        goto out;
    }

    strLen = strlen(xmlFile);
    if (strLen == 0) {
        JNI_CALIB_LOGE("xmlFileName strLen = 0 err!");
        goto out;
    }

    if (LOG_DEBUG) {
        JNI_CALIB_LOGI("xmlFileName : %s", xmlFile);
    }

    leftBufferLen = env->GetArrayLength(leftBuffer);
    rightBufferLen = env->GetArrayLength(rightBuffer);
    if (leftBufferLen == 0 || rightBufferLen == 0) {
        JNI_CALIB_LOGE("input Buffer len =  0 err!");
        goto out;
    }

    leftBufferData = (unsigned char*)env->GetByteArrayElements(leftBuffer, 0);
    rightBufferData = (unsigned char*)env->GetByteArrayElements(rightBuffer, 0);
    if (NULL == leftBufferData || NULL == rightBufferData) {
        JNI_CALIB_LOGE("input Buffer is NULL err!");
        goto out;
    }

    byteParamData = (unsigned char*)env->GetByteArrayElements(byteParam, 0);
    if (NULL == byteParamData) {
        JNI_CALIB_LOGE("byteParamData Buffer is NULL err!");
        goto out;
    }
    byteInfo = (char*)env->GetByteArrayElements(jByteInfo, 0);
    if (NULL == byteInfo) {
        JNI_CALIB_LOGE("byteInfo Buffer is NULL err!");
        goto out;
    }

    ret = gCalibImpl->selfRectify(leftBufferData, rightBufferData, byteParamData, xmlFile, byteInfo);

    JNI_CALIB_LOGI("nativeSelfRectify ret=%d", ret);

    env->SetByteArrayRegion(jByteInfo, 0, 1024, (jbyte*)byteInfo);

out:
    if (rightBufferData)
        env->ReleaseByteArrayElements(rightBuffer, (jbyte *)rightBufferData, 0);
    if (leftBufferData)
        env->ReleaseByteArrayElements(leftBuffer, (jbyte *)leftBufferData, 0);
    if (xmlFile)
        env->ReleaseStringUTFChars(xmlFileName, xmlFile);
    if (byteParamData)
        env->ReleaseByteArrayElements(byteParam, (jbyte *)byteParamData, 0);
    if (byteInfo)
        env->ReleaseByteArrayElements(jByteInfo, (jbyte *)byteInfo, 0);

    return ret;
}

static jint nativeGetAfMode(JNIEnv *env, jobject thiz, jstring xmlFileName)
{
    int ret = 0;
    char* xmlFile = NULL;
    int strLen = 0;

    (void)thiz;
    if(gCalibImpl == NULL) {
        JNI_CALIB_LOGE("gCalibImpl is NULL");
        return 0;
    }

    xmlFile = (char*)env->GetStringUTFChars(xmlFileName, JNI_FALSE);
    if (xmlFile == NULL) {
        JNI_CALIB_LOGE("xmlFile = NULL err!");
        goto out;
    }

    strLen = strlen(xmlFile);
    if (strLen == 0) {
        JNI_CALIB_LOGE("xmlFileName strLen = 0 err!");
        goto out;
    }

    if (LOG_DEBUG) {
        JNI_CALIB_LOGI("xmlFileName : %s", xmlFile);
    }

    ret = gCalibImpl->selfCalibGetAfMode(xmlFile);

    JNI_CALIB_LOGI("nativeGetAfMode ret=%d", ret);
out:
    if (xmlFile)
        env->ReleaseStringUTFChars(xmlFileName, xmlFile);

    return ret;
}

static jint nativeGetAfStep(JNIEnv *env, jobject thiz, jbyteArray byteParam)
{
    int ret = 0;
    unsigned char* byteParamData = NULL;

    (void)thiz;
    if(gCalibImpl == NULL) {
        JNI_CALIB_LOGE("gCalibImpl is NULL");
        return 0;
    }

    byteParamData = (unsigned char*)env->GetByteArrayElements(byteParam, 0);
    if (byteParamData == NULL) {
        JNI_CALIB_LOGE("byteParamData is NULL");
        goto out;
    }
    ret = gCalibImpl->selfRectifyGetAfStep(byteParamData);

    JNI_CALIB_LOGI("nativeGetAfMode ret=%d", ret);
out:
    if (byteParamData)
        env->ReleaseByteArrayElements(byteParam, (jbyte *)byteParamData, 0);

    return ret;
}

static const char *jniClassName  = "com/xapi/calib/feature/mode/vsdof/photo/jni_xapi/XapiCalibJni";


static JNINativeMethod methods[] = {
    {"nativeInit", "(Ljava/lang/String;I)Z", (void*)nativeInit},
    {"nativeUninit", "()V", (void*)nativeUnInit},
    {"nativeSelfCalibration", "([B[BLjava/lang/Object;Ljava/lang/String;I)Z", (void*)nativeSelfCalibration},
    {"nativeSelfRectify", "([B[B[BLjava/lang/String;[B)Z", (void*)nativeSelfRectify},
    {"nativeSendResult", "(I)V", (void*)nativeSendResult},
    //{"nativeGetAfMode", "(Ljava/lang/String;)I", (void*)nativeGetAfMode},
    //{"nativeGetAfStep", "([B)I",  (void*)nativeGetAfStep},
};

/*
 * Register several native methods for one .
 */
static jint registerNativeMethods(JNIEnv* env, const char* className,
                                    JNINativeMethod* methods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        JNI_CALIB_LOGE("Native registration unable to find  '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, methods, numMethods) < 0) {
        JNI_CALIB_LOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static jint registerNatives(JNIEnv* env)
{
    if (JNI_TRUE != registerNativeMethods(env, jniClassName,
                 methods, sizeof(methods) / sizeof(methods[0]))) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


// ----------------------------------------------------------------------------

/*
 * This is called by the VM when the shared library is first loaded.
 */

typedef union {
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv* env = NULL;

    (void)reserved;
    JNI_CALIB_LOGI("JNI_OnLoad");

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
        JNI_CALIB_LOGE("ERROR: GetEnv failed");
        goto bail;
    }
    env = uenv.env;

    if (registerNatives(env) != JNI_TRUE) {
        JNI_CALIB_LOGE("ERROR: registerNatives failed");
        goto bail;
    }

    result = JNI_VERSION_1_4;

bail:
    return result;
}

