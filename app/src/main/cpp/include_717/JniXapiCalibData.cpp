/*******************************************************************************
Copyright(c) 2017-2020 Xi'an PI  information technology Co. Ltd, All right reserved.

DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER
AUTHORIZATION.

*******************************************************************************/
#define LOG_TAG "JniXapiCalibData"

#include <stdio.h>
#include <fstream>
#include <stdlib.h>
#include <bits/ioctl.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include "cam_cal.h"
#include <string.h>

#include "jni.h"

#include "pi_dualcam_distance.h"

using namespace std;
//using namespace android;
/*
#define JNI_CALIB_LOGV(fmt, args...) \
               ALOGE("%s:%04d " fmt,  __FUNCTION__, __LINE__, ##args)
#define JNI_CALIB_LOGI(fmt, args...) \
               ALOGE("%s:%04d " fmt,  __FUNCTION__, __LINE__, ##args)
#define JNI_CALIB_LOGE(fmt, args...) \
               ALOGE("%s:%04d " fmt,  __FUNCTION__, __LINE__, ##args)
*/
#define LOG_DEBUG true

#define CLASS_PATH "com/xapi/calib/jni_calib_data"
//#define READ_FROM_EEPROM

#ifdef READ_FROM_EEPROM
#define DEV_NODE    "/sys/devices/platform/11008000.i2c/i2c-4/4-0077/eeprom";
#else
#define DEV_NODE    "/dev/CAM_CAL_DRV"
#endif
struct stCAM_CAL_INFO_STRUCT {
    int32_t u4Offset;
    int32_t u4Length;
    int32_t sensorID;
    /*
     * MAIN = 0x01,
     * SUB  = 0x02,
     * MAIN_2 = 0x04,
     * SUB_2 = 0x08,
     * MAIN_3 = 0x10,
     */
    int32_t deviceID;
    uint8_t *pu1Params;
};

struct pi_fields_t {
    jfieldID sensorIdField;
    jfieldID deviceIdField;
    jfieldID offsetField;
    jmethodID jresult_constructor;
    jclass    jresultClass;
    jmethodID jhyn_constructor;
    jclass    jhynClass;

    jfieldID leftField;
    jfieldID rightField;
    jfieldID rField;
    jfieldID tField;

    jfieldID fxField;
    jfieldID fyField;
    jfieldID cxField;
    jfieldID cyField;
    jfieldID k1Field;
    jfieldID k2Field;
    jfieldID p1Field;
    jfieldID p2Field;
    jfieldID k3Field;
};
struct jni_field_t {
    const char *class_name;
    const char *field_name;
    const char *field_type;
    jfieldID   *jfield;
};
//double type for calib data
typedef struct {
    PIDouble fx;
    PIDouble fy;
    PIDouble cx;
    PIDouble cy;
    PIDouble k1;
    PIDouble k2;
    PIDouble p1;
    PIDouble p2;
    PIDouble k3;
}BDM_CAMERA_PARAM_D;

typedef struct {
    BDM_CAMERA_PARAM_D left;
    BDM_CAMERA_PARAM_D right;
    
    PIDouble R[9];
    PIDouble T[3];
} BDM_CALIB_DATA_D;

static pi_fields_t g_fields;
static void find_jni_fields(JNIEnv *env, jni_field_t *fields, int count)
{
    for (int i = 0; i < count; i++) {
        jni_field_t *f = &fields[i];
        if (f) {
            jclass clazz = env->FindClass(f->class_name);
            jfieldID field = env->GetFieldID(clazz, f->field_name, f->field_type);
            *(f->jfield) = field;
        }
    }
}
#ifdef READ_FROM_EEPROM
static jobject nativeReadCalibData(JNIEnv *env, jobject thiz, jobject param)
{
    (void)env;
    (void)thiz;
    bool ret = false;
    const char cBuf[128] = DEV_NODE;
    int ioRet = 0;
    int fd = -1;
    int size = 2050;
    int sensorId = 0;
    int deviceId = 0;
    int offset = 0;
    char *pData = NULL;
    int pi_ret = -1;
    BDM_CALIB_DATA hyn;
    BDM_CALIB_DATA_D hyn_d;
    jobject resultObj = NULL;
    jclass resultClazz = NULL;
    jclass leftHynClazz = NULL;
    jclass rightHynClazz = NULL;

    //JNI_CALIB_LOGI("VERSION:%s %s D", __DATE__, __TIME__);

    pData = (char*)malloc(size);
    fd = open(cBuf, O_RDONLY);
    if (fd >= 0 && pData) {
        sensorId = env->GetIntField(param, g_fields.sensorIdField);
        deviceId = env->GetIntField(param, g_fields.deviceIdField);
        offset = env->GetIntField(param, g_fields.offsetField);
        lseek(fd, offset, SEEK_SET);
        ioRet = read(fd, pData, size);
        if (ioRet == size) {
            memset(&hyn, 0, sizeof(hyn));
            pi_ret = BDM_GetCalibData((PIPUChar)&pData[1], (PIPUChar)&hyn);
            if (PIOK == pi_ret) {
                ret = true;
#if 0
                JNI_CALIB_LOGI("left %lf %lf %lf %lf %lf %lf %lf %lf %lf",
                        hyn.left.fx, hyn.left.fy, hyn.left.cx, hyn.left.cy,
                        hyn.left.k1, hyn.left.k2, hyn.left.p1, hyn.left.p2,
                        hyn.left.k3);
                JNI_CALIB_LOGI("right %lf %lf %lf %lf %lf %lf %lf %lf %lf",
                        hyn.right.fx, hyn.right.fy, hyn.right.cx, hyn.right.cy,
                        hyn.right.k1, hyn.right.k2, hyn.right.p1, hyn.right.p2,
                        hyn.right.k3);
                for (int i = 0; i < 9; ++i)
                    JNI_CALIB_LOGI("R[%d]:%lf", i, hyn.R[i]);
                for (int i = 0; i < 3; ++i)
                    JNI_CALIB_LOGI("T[%d]:%lf", i, hyn.T[i]);
#endif
                //float2double
                memset(&hyn_d, 0, sizeof(hyn_d));
                hyn_d.left.fx = hyn.left.fx;
                hyn_d.left.fy = hyn.left.fy;
                hyn_d.left.cx = hyn.left.cx;
                hyn_d.left.cy = hyn.left.cy;
                hyn_d.left.k1 = hyn.left.k1;
                hyn_d.left.k2 = hyn.left.k2;
                hyn_d.left.p1 = hyn.left.p1;
                hyn_d.left.p2 = hyn.left.p2;
                hyn_d.left.k3 = hyn.left.k3;

                hyn_d.right.fx = hyn.right.fx;
                hyn_d.right.fy = hyn.right.fy;
                hyn_d.right.cx = hyn.right.cx;
                hyn_d.right.cy = hyn.right.cy;
                hyn_d.right.k1 = hyn.right.k1;
                hyn_d.right.k2 = hyn.right.k2;
                hyn_d.right.p1 = hyn.right.p1;
                hyn_d.right.p2 = hyn.right.p2;
                hyn_d.right.k3 = hyn.right.k3;
                for (int i = 0; i < 9; ++i) {
                    hyn_d.R[i] = hyn.R[i];
                }
                for (int i = 0; i < 3; ++i) {
                    hyn_d.T[i] = hyn.T[i];
                }

                resultClazz = env->FindClass(CLASS_PATH"/XapiCalibDataResult");
                resultObj = env->NewObject(resultClazz, g_fields.jresult_constructor);

                leftHynClazz = env->FindClass(CLASS_PATH"/XapiCalibDataHyn");
                jobject leftHynObj = env->NewObject(leftHynClazz, g_fields.jhyn_constructor);

                rightHynClazz = env->FindClass(CLASS_PATH"/XapiCalibDataHyn");
                jobject rightHynObj = env->NewObject(rightHynClazz, g_fields.jhyn_constructor);

                env->SetObjectField(resultObj, g_fields.leftField, leftHynObj);
                env->SetObjectField(resultObj, g_fields.rightField, rightHynObj);

                env->SetDoubleField(leftHynObj, g_fields.fxField, hyn_d.left.fx);
                env->SetDoubleField(leftHynObj, g_fields.fyField, hyn_d.left.fy);
                env->SetDoubleField(leftHynObj, g_fields.cxField, hyn_d.left.cx);
                env->SetDoubleField(leftHynObj, g_fields.cyField, hyn_d.left.cy);
                env->SetDoubleField(leftHynObj, g_fields.k1Field, hyn_d.left.k1);
                env->SetDoubleField(leftHynObj, g_fields.k2Field, hyn_d.left.k2);
                env->SetDoubleField(leftHynObj, g_fields.p1Field, hyn_d.left.p1);
                env->SetDoubleField(leftHynObj, g_fields.p2Field, hyn_d.left.p2);
                env->SetDoubleField(leftHynObj, g_fields.k3Field, hyn_d.left.k3);

                env->SetDoubleField(rightHynObj, g_fields.fxField, hyn_d.right.fx);
                env->SetDoubleField(rightHynObj, g_fields.fyField, hyn_d.right.fy);
                env->SetDoubleField(rightHynObj, g_fields.cxField, hyn_d.right.cx);
                env->SetDoubleField(rightHynObj, g_fields.cyField, hyn_d.right.cy);
                env->SetDoubleField(rightHynObj, g_fields.k1Field, hyn_d.right.k1);
                env->SetDoubleField(rightHynObj, g_fields.k2Field, hyn_d.right.k2);
                env->SetDoubleField(rightHynObj, g_fields.p1Field, hyn_d.right.p1);
                env->SetDoubleField(rightHynObj, g_fields.p2Field, hyn_d.right.p2);
                env->SetDoubleField(rightHynObj, g_fields.k3Field, hyn_d.right.k3);

                jdoubleArray jr = env->NewDoubleArray(9);
                env->SetDoubleArrayRegion(jr, 0, 9, hyn_d.R);
                env->SetObjectField(resultObj, g_fields.rField, jr);

                jdoubleArray jt = env->NewDoubleArray(3);
                env->SetDoubleArrayRegion(jt, 0, 3, hyn_d.T);
                env->SetObjectField(resultObj, g_fields.tField, jt);

                env->DeleteLocalRef(jr);
                env->DeleteLocalRef(jt);
                env->DeleteLocalRef(leftHynObj);
                env->DeleteLocalRef(rightHynObj);
            }
        }
    } else {
        //JNI_CALIB_LOGE("nativeReadDepthResult error fd=%d, pData=%p", fd, pData);
    }
    if (pData)
        free(pData);
out:
    //JNI_CALIB_LOGV("nativeReadDepthResult ret=%d ioRet=%d", ret, ioRet);
    if (ret)
        return resultObj;
    else
        return NULL;
}

#else
static jobject nativeReadCalibData(JNIEnv *env, jobject thiz, jobject param)
{
    (void)env;
    (void)thiz;
    bool ret = false;
    const char cBuf[128] = DEV_NODE;
    int ioRet = 0;
    stCAM_CAL_INFO_STRUCT  cam_calCfg;
    int fd = -1;
    int size = 2050;
    int sensorId = 0;
    int deviceId = 0;
    int offset = 0;
    char *pData = NULL;
    int pi_ret = -1;
    BDM_CALIB_DATA hyn;
    BDM_CALIB_DATA_D hyn_d;
    jobject resultObj = NULL;
    jclass resultClazz = NULL;
    jclass leftHynClazz = NULL;
    jclass rightHynClazz = NULL;


    //JNI_CALIB_LOGI("VERSION:%s %s D", __DATE__, __TIME__);

    pData = (char*)malloc(size);
    fd = open(cBuf, O_RDONLY);
    if (fd >= 0 && pData) {
        sensorId = env->GetIntField(param, g_fields.sensorIdField);
        deviceId = env->GetIntField(param, g_fields.deviceIdField);
        offset = env->GetIntField(param, g_fields.offsetField);

        memset(&cam_calCfg, 0, sizeof(cam_calCfg));
        cam_calCfg.u4Length = size;
        cam_calCfg.u4Offset = offset;
        cam_calCfg.pu1Params= (uint8_t*)pData;
        cam_calCfg.sensorID = sensorId;
        cam_calCfg.deviceID = deviceId;
        ioRet= ioctl(fd, CAM_CALIOC_G_READ, &cam_calCfg);
        if (ioRet == size) {
            memset(&hyn, 0, sizeof(hyn));
            pi_ret = BDM_GetCalibData((PIPUChar)&pData[1], (PIPUChar)&hyn);
            if (PIOK == pi_ret) {
                ret = true;
#if 0
                JNI_CALIB_LOGI("left %lf %lf %lf %lf %lf %lf %lf %lf %lf",
                        hyn.left.fx, hyn.left.fy, hyn.left.cx, hyn.left.cy,
                        hyn.left.k1, hyn.left.k2, hyn.left.p1, hyn.left.p2,
                        hyn.left.k3);
                JNI_CALIB_LOGI("right %lf %lf %lf %lf %lf %lf %lf %lf %lf",
                        hyn.right.fx, hyn.right.fy, hyn.right.cx, hyn.right.cy,
                        hyn.right.k1, hyn.right.k2, hyn.right.p1, hyn.right.p2,
                        hyn.right.k3);
                for (int i = 0; i < 9; ++i)
                    JNI_CALIB_LOGI("R[%d]:%lf", i, hyn.R[i]);
                for (int i = 0; i < 3; ++i)
                    JNI_CALIB_LOGI("T[%d]:%lf", i, hyn.T[i]);
#endif
                //float2double
                memset(&hyn_d, 0, sizeof(hyn_d));
                hyn_d.left.fx = hyn.left.fx;
                hyn_d.left.fy = hyn.left.fy;
                hyn_d.left.cx = hyn.left.cx;
                hyn_d.left.cy = hyn.left.cy;
                hyn_d.left.k1 = hyn.left.k1;
                hyn_d.left.k2 = hyn.left.k2;
                hyn_d.left.p1 = hyn.left.p1;
                hyn_d.left.p2 = hyn.left.p2;
                hyn_d.left.k3 = hyn.left.k3;

                hyn_d.right.fx = hyn.right.fx;
                hyn_d.right.fy = hyn.right.fy;
                hyn_d.right.cx = hyn.right.cx;
                hyn_d.right.cy = hyn.right.cy;
                hyn_d.right.k1 = hyn.right.k1;
                hyn_d.right.k2 = hyn.right.k2;
                hyn_d.right.p1 = hyn.right.p1;
                hyn_d.right.p2 = hyn.right.p2;
                hyn_d.right.k3 = hyn.right.k3;
                for (int i = 0; i < 9; ++i) {
                    hyn_d.R[i] = hyn.R[i];
                }
                for (int i = 0; i < 3; ++i) {
                    hyn_d.T[i] = hyn.T[i];
                }

                resultClazz = env->FindClass(CLASS_PATH"/XapiCalibDataResult");
                resultObj = env->NewObject(resultClazz, g_fields.jresult_constructor);

               // JNI_CALIB_LOGI("create leftHynObj");
                leftHynClazz = env->FindClass(CLASS_PATH"/XapiCalibDataHyn");
                jobject leftHynObj = env->NewObject(leftHynClazz, g_fields.jhyn_constructor);

                rightHynClazz = env->FindClass(CLASS_PATH"/XapiCalibDataHyn");
                jobject rightHynObj = env->NewObject(rightHynClazz, g_fields.jhyn_constructor);

                env->SetObjectField(resultObj, g_fields.leftField, leftHynObj);
                env->SetObjectField(resultObj, g_fields.rightField, rightHynObj);

                env->SetDoubleField(leftHynObj, g_fields.fxField, hyn_d.left.fx);
                env->SetDoubleField(leftHynObj, g_fields.fyField, hyn_d.left.fy);
                env->SetDoubleField(leftHynObj, g_fields.cxField, hyn_d.left.cx);
                env->SetDoubleField(leftHynObj, g_fields.cyField, hyn_d.left.cy);
                env->SetDoubleField(leftHynObj, g_fields.k1Field, hyn_d.left.k1);
                env->SetDoubleField(leftHynObj, g_fields.k2Field, hyn_d.left.k2);
                env->SetDoubleField(leftHynObj, g_fields.p1Field, hyn_d.left.p1);
                env->SetDoubleField(leftHynObj, g_fields.p2Field, hyn_d.left.p2);
                env->SetDoubleField(leftHynObj, g_fields.k3Field, hyn_d.left.k3);

                env->SetDoubleField(rightHynObj, g_fields.fxField, hyn_d.right.fx);
                env->SetDoubleField(rightHynObj, g_fields.fyField, hyn_d.right.fy);
                env->SetDoubleField(rightHynObj, g_fields.cxField, hyn_d.right.cx);
                env->SetDoubleField(rightHynObj, g_fields.cyField, hyn_d.right.cy);
                env->SetDoubleField(rightHynObj, g_fields.k1Field, hyn_d.right.k1);
                env->SetDoubleField(rightHynObj, g_fields.k2Field, hyn_d.right.k2);
                env->SetDoubleField(rightHynObj, g_fields.p1Field, hyn_d.right.p1);
                env->SetDoubleField(rightHynObj, g_fields.p2Field, hyn_d.right.p2);
                env->SetDoubleField(rightHynObj, g_fields.k3Field, hyn_d.right.k3);

                //JNI_CALIB_LOGI("start set end");

                jdoubleArray jr = env->NewDoubleArray(9);
                env->SetDoubleArrayRegion(jr, 0, 9, hyn_d.R);
                env->SetObjectField(resultObj, g_fields.rField, jr);

                jdoubleArray jt = env->NewDoubleArray(3);
                env->SetDoubleArrayRegion(jt, 0, 3, hyn_d.T);
                env->SetObjectField(resultObj, g_fields.tField, jt);

                env->DeleteLocalRef(jr);
                env->DeleteLocalRef(jt);
                env->DeleteLocalRef(leftHynObj);
                env->DeleteLocalRef(rightHynObj);
            }
        }
    } else {
        //JNI_CALIB_LOGE("nativeReadDepthResult error fd=%d, pData=%p", fd, pData);
    }
    if (pData)
        free(pData);
out:
   //JNI_CALIB_LOGV("nativeReadDepthResult ret=%d ioRet=%d", ret, ioRet);
    if (ret)
        return resultObj;
    else
        return NULL;
}
#endif

static const char *jniClassName  = CLASS_PATH"/XapiCalibDataJni";

static JNINativeMethod methods[] = {
    {"nativeReadCalibData", "(Ljava/lang/Object;)Ljava/lang/Object;", (void*)nativeReadCalibData},
};
struct jni_field_t field_list[] = {
    {CLASS_PATH"/XapiCalibDataParam", "sensorId", "I", &g_fields.sensorIdField},
    {CLASS_PATH"/XapiCalibDataParam", "deviceId", "I", &g_fields.deviceIdField},
    {CLASS_PATH"/XapiCalibDataParam", "offset", "I", &g_fields.offsetField},
    {CLASS_PATH"/XapiCalibDataResult", "left", "L" CLASS_PATH "/XapiCalibDataHyn;", &g_fields.leftField},
    {CLASS_PATH"/XapiCalibDataResult", "right", "L" CLASS_PATH "/XapiCalibDataHyn;", &g_fields.rightField},
    {CLASS_PATH"/XapiCalibDataResult", "R", "[D", &g_fields.rField},
    {CLASS_PATH"/XapiCalibDataResult", "T", "[D", &g_fields.tField},
    {CLASS_PATH"/XapiCalibDataHyn", "fx", "D", &g_fields.fxField},
    {CLASS_PATH"/XapiCalibDataHyn", "fy", "D", &g_fields.fyField},
    {CLASS_PATH"/XapiCalibDataHyn", "cx", "D", &g_fields.cxField},
    {CLASS_PATH"/XapiCalibDataHyn", "cy", "D", &g_fields.cyField},
    {CLASS_PATH"/XapiCalibDataHyn", "k1", "D", &g_fields.k1Field},
    {CLASS_PATH"/XapiCalibDataHyn", "k2", "D", &g_fields.k2Field},
    {CLASS_PATH"/XapiCalibDataHyn", "p1", "D", &g_fields.p1Field},
    {CLASS_PATH"/XapiCalibDataHyn", "p2", "D", &g_fields.p2Field},
    {CLASS_PATH"/XapiCalibDataHyn", "k3", "D", &g_fields.k3Field}
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
        //JNI_CALIB_LOGE("Native registration unable to find  '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, methods, numMethods) < 0) {
        //JNI_CALIB_LOGE("RegisterNatives failed for '%s'", className);
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
    //JNI_CALIB_LOGI("JNI_OnLoad");

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
        //JNI_CALIB_LOGE("ERROR: GetEnv failed");
        goto bail;
    }
    env = uenv.env;

    memset(&g_fields, 0, sizeof(g_fields));
    find_jni_fields(env, field_list, sizeof(field_list) / sizeof(field_list[0]));
    g_fields.jresultClass = env->FindClass(CLASS_PATH"/XapiCalibDataResult");
    g_fields.jresult_constructor = env->GetMethodID(g_fields.jresultClass, "<init>", "()V");
    g_fields.jhynClass = env->FindClass(CLASS_PATH"/XapiCalibDataHyn");
    g_fields.jhyn_constructor = env->GetMethodID(g_fields.jhynClass, "<init>", "()V");

    if (registerNatives(env) != JNI_TRUE) {
        //JNI_CALIB_LOGE("ERROR: registerNatives failed");
        goto bail;
    }

    result = JNI_VERSION_1_4;

bail:
    return result;
}

