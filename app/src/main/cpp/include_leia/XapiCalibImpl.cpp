/*******************************************************************************
Copyright(c) 2017-2019 Xi'an PI  information technology Co. Ltd, All right reserved.

DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER
AUTHORIZATION.

*******************************************************************************/
#define LOG_TAG "XapiCalibImpl"

#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <unistd.h>
//#include <log/log.h>
#include <android/log.h>

//#include <cutils/properties.h>

#include "includes/pi_calib.h"
#include "XapiCalibImpl.h"

#define XAPI_CALIB_DBG_INFO_LENGTH 1024
#define PI_VALUE_MAX 32

#define CALIB_RESULT_FILE "/sdcard/calib_data"

using namespace std;

namespace android {
#define TAG "XapiCalibImpl"

#define PI_CALIB_LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define PI_CALIB_LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define PI_CALIB_LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

XapiCalibImpl::XapiCalibImpl()
{
    PI_CALIB_LOGI("VERSION:%s %s", __DATE__, __TIME__);
    mPiCalibHandle = NULL;
    return;
}

XapiCalibImpl::~XapiCalibImpl()
{
    PI_CALIB_LOGI("xapi calib impl uninit");
    //API_Uninit(mPiCalibHandle);

    if(mPiCalibHandle) {
        PI_CALIB_LOGE("Uninit calib impl: %p", mPiCalibHandle);
        mPiCalibHandle = NULL;
    }

}
bool XapiCalibImpl::init(char *paramFile, int socket_on)
{
    PI_CALIB_LOGI("xapi calib impl init begin");
    if (PIOK != API_Init(&mPiCalibHandle, paramFile,socket_on)) {
        PI_CALIB_LOGE("API_Init calib handle failed!");
        return false;
    }
    PI_CALIB_LOGI("xapi calib impl init OK mPiCalibHandle=%p", mPiCalibHandle);
    return true;
}

int XapiCalibImpl::CalibSendResult(int result)
{
    int ret = API_SendResult(mPiCalibHandle, result);
    return ret;
}

bool XapiCalibImpl::dumpFile(const char* fileName, unsigned char * buf, int size)
{
    int fd = 0;

    if(NULL == fileName || NULL == buf || 0 == size) {
        PI_CALIB_LOGE("dumpFile: input error: file_name(%s), buf(%p), size(%d)", fileName, buf, size);
    }

    fd = open(fileName, O_RDWR | O_CREAT, 0644);
    if(fd < 0) {
        PI_CALIB_LOGE("failed to open file!");
        return false;
    }

    write(fd, buf, size);

    close(fd);

    return true;
}

bool XapiCalibImpl::selfCalibration(unsigned char *leftBufferData, unsigned char *rightBufferData,
                                            unsigned char *byteParam, char *xmlFile, char *result, int af_calib)

{
    int ret;
    int fd = 0;
    char value[PI_VALUE_MAX];

    PI_CALIB_LOGI("selfCalibration handle: %p", mPiCalibHandle);

    if(access(xmlFile, R_OK)) {
        PI_CALIB_LOGE("Please check parameters xml!");
        return false;
    }

//    property_get("persist.pi.calib.control", value, "0");

    ORGINAL_IMAGE mainImg,subImg;
    STEREOCALIB_PARAM stereoParam;
    memset(&mainImg,0,sizeof(mainImg));
    memset(&subImg,0,sizeof(subImg));
    memset(&stereoParam,0,sizeof(stereoParam));
    stereoParam.pbyteParam = byteParam;
    stereoParam.paramFile = xmlFile;
    stereoParam.af_calib = af_calib;
    if(atoi(value) == 2) { //test mode
        unsigned char *pTestLeftBuffer = (unsigned char *)malloc(2688 * 2016 * 3 / 2 + 1);
        unsigned char *pTestRightBuffer = (unsigned char *)malloc(2560 * 1920 * 3 / 2 + 1);

        fd = open("/data/vendor/left.yuv", O_RDWR);
        if(fd < 0) {
            PI_CALIB_LOGE("open left.yuv fail!!!");
            return false;
        }
        memset(pTestLeftBuffer, 0, 2688* 2016 * 3 / 2 + 1);
        read(fd, pTestLeftBuffer, 2688 * 2016 * 3 / 2);
        close(fd);

        fd = open("/data/vendor/right.yuv", O_RDWR);
        if(fd < 0) {
            PI_CALIB_LOGE("open right.yuv fail!!!");
            return false;
        }
        memset(pTestRightBuffer, 0, 2560 * 1920 * 3 / 2 + 1);
        read(fd, pTestRightBuffer, 2560 * 1920 * 3 / 2);
        close(fd);

        mainImg.i32Width = MAIN_STILL_WIDTH;
        mainImg.i32Height = MAIN_STILL_HEIGHT;
        mainImg.ppu8Plane[0] = pTestLeftBuffer;
        mainImg.pi32Pitch[0] = MAIN_STILL_WIDTH;

        subImg.i32Width = SUB_STILL_WIDTH;
        subImg.i32Height = SUB_STILL_HEIGHT;
        subImg.ppu8Plane[0] = pTestRightBuffer;
        subImg.pi32Pitch[0]= SUB_STILL_WIDTH;
        ret = API_StereoCalib_3C(&mainImg, &subImg, NULL, &stereoParam, mPiCalibHandle, result);

        if(pTestLeftBuffer) {
            free(pTestLeftBuffer);
            pTestLeftBuffer = NULL;
        }

        if(pTestRightBuffer) {
            free(pTestRightBuffer);
            pTestRightBuffer = NULL;
        }

        (void)leftBufferData;
        (void)rightBufferData;
    }else {
        if(atoi(value) == 1) { //dump mode
            dumpFile("/sdcard/calib_left_4032x3024.NV21", leftBufferData, 4032 * 3024 * 3 / 2);
            dumpFile("/sdcard/calib_right_2592x1944.NV21", rightBufferData, 2592 * 1944 * 3 / 2);
        }
        mainImg.i32Width = MAIN_STILL_WIDTH;
        mainImg.i32Height = MAIN_STILL_HEIGHT;
        mainImg.ppu8Plane[0] = leftBufferData;
        mainImg.pi32Pitch[0] = MAIN_STILL_WIDTH;

        subImg.i32Width = SUB_STILL_WIDTH;
        subImg.i32Height = SUB_STILL_HEIGHT;
        subImg.ppu8Plane[0] = rightBufferData;
        subImg.pi32Pitch[0] = SUB_STILL_WIDTH;
        ret = API_StereoCalib_3C(&mainImg, &subImg, NULL, &stereoParam, mPiCalibHandle, result);
    }

    if(ret != 0) {
        PI_CALIB_LOGE("stereo_calib error : %d",ret);
        return false;
    } else {
        PI_CALIB_LOGI("stereo calib ok!");
    }

    /*if(atoi(value) != 0) {
        dumpFile("/sdcard/calib_data", byteArr, 2048);
    }*/
    if (0x08 == (atoi(value) & 0x08))
        dumpFile(CALIB_RESULT_FILE, byteParam, 2048);

    return true;
}

bool XapiCalibImpl::selfRectify(unsigned char *leftBufferData, unsigned char *rightBufferData,
                                        unsigned char *byteParam, char *xmlFile, char *result)
{
    int ret;
    int fd = 0;
    char value[PI_VALUE_MAX];
    unsigned char byteRes[2048];

    if(NULL == byteParam) {
        fd = open(CALIB_RESULT_FILE, O_RDWR);
        if(fd < 0) {
            PI_CALIB_LOGE("can not read calib result file! Please check /sdcard/calib_data exists!");
            return false;
        }

        write(fd, byteRes, 2048);
        close(fd);
    }

    if(access(xmlFile, R_OK)) {
        PI_CALIB_LOGE("Please check parameters xml!");
        return false;
    }

//    property_get("persist.pi.calib.control", value, "0");

    ORGINAL_IMAGE mainImg,subImg;
    STEREOCALIB_PARAM stereoParam;
    memset(&mainImg,0,sizeof(mainImg));
    memset(&subImg,0,sizeof(subImg));
    memset(&stereoParam,0,sizeof(stereoParam));
    stereoParam.paramFile = xmlFile;

    if(atoi(value) == 2) { //test mode
        unsigned char *pTestLeftBuffer = (unsigned char *)malloc(2688 * 2016 * 3 / 2 + 1);
        unsigned char *pTestRightBuffer = (unsigned char *)malloc(2560 * 1920 * 3 / 2 + 1);

        fd = open("/data/vendor/left.yuv", O_RDWR);
        if(fd < 0) {
            PI_CALIB_LOGE("open left.yuv fail!!!");
            return false;
        }
        memset(pTestLeftBuffer, 0, 2688 * 2016 * 3 / 2 + 1);
        read(fd, pTestLeftBuffer, 2688 * 2016 * 3 / 2);
        close(fd);

        fd = open("/data/vendor/right.yuv", O_RDWR);
        if(fd < 0) {
            PI_CALIB_LOGE("open right.yuv fail!!!");
            return false;
        }
        memset(pTestRightBuffer, 0, 2560 * 1920 * 3 / 2 + 1);
        read(fd, pTestRightBuffer, 2560 * 1920 * 3 / 2);
        close(fd);

        mainImg.i32Width = MAIN_STILL_WIDTH;
        mainImg.i32Height = MAIN_STILL_HEIGHT;
        mainImg.ppu8Plane[0] = pTestLeftBuffer;
        mainImg.pi32Pitch[0] = MAIN_STILL_WIDTH;

        subImg.i32Width = SUB_STILL_WIDTH;
        subImg.i32Height = SUB_STILL_HEIGHT;
        subImg.ppu8Plane[0] = pTestRightBuffer;
        subImg.pi32Pitch[0] = SUB_STILL_WIDTH;

        if(byteParam != NULL){
            stereoParam.pbyteParam = byteParam;
        }else{
            stereoParam.pbyteParam = byteRes;
        }
        ret = API_RectifyImg_3C(&mainImg, &subImg, NULL, &stereoParam, result);
        if(pTestLeftBuffer) {
            free(pTestLeftBuffer);
            pTestLeftBuffer = NULL;
        }

        if(pTestRightBuffer) {
            free(pTestRightBuffer);
            pTestRightBuffer = NULL;
        }

        (void)leftBufferData;
        (void)rightBufferData;
    } else {
        mainImg.i32Width = MAIN_STILL_WIDTH;
        mainImg.i32Height = MAIN_STILL_HEIGHT;
        mainImg.ppu8Plane[0] = leftBufferData;
        mainImg.pi32Pitch[0] = MAIN_STILL_WIDTH;

        subImg.i32Width = SUB_STILL_WIDTH;
        subImg.i32Height = SUB_STILL_HEIGHT;
        subImg.ppu8Plane[0] = rightBufferData;
        subImg.pi32Pitch[0] = SUB_STILL_WIDTH;
        if(byteParam != NULL){
            stereoParam.pbyteParam = byteParam;
        } else {
            stereoParam.pbyteParam = byteRes;
        }
        ret = API_RectifyImg_3C(&mainImg, &subImg, NULL, &stereoParam, result);
    }
    if (0x08 == (atoi(value) & 0x08))
        dumpFile("/sdcard/.calib_info", (unsigned char*)result, XAPI_CALIB_DBG_INFO_LENGTH);

    if(ret != 0) {
        PI_CALIB_LOGE("stereo rectify error : %d",ret);
        return false;
    } else {
        PI_CALIB_LOGI("stereo rectify ok!");
    }

    return true;
}

int XapiCalibImpl::selfCalibGetAfMode(char *xmlFile)
{
    if(access(xmlFile, R_OK)) {
        PI_CALIB_LOGE("Please check parameters xml!");
        return -1;
    }
    return API_GetFocusMode(xmlFile);
}

int XapiCalibImpl::selfRectifyGetAfStep(unsigned char *byteParam)
{
    int af_calib = 0;
    int fd = 0;
    unsigned char byteRes[2048];

    if(access(CALIB_RESULT_FILE, R_OK)) {
        PI_CALIB_LOGE("Please check /sdcard/calib_data exists!");
        return -1;
    }

    if(NULL == byteParam) {
        fd = open(CALIB_RESULT_FILE, O_RDWR);
        if(fd < 0) {
            PI_CALIB_LOGE("can not read calib result file! Please check /sdcard/calib_data exists!");
            return -1;
        }

        write(fd, byteRes, 2048);
        close(fd);

        af_calib = API_GetMotorstep(byteRes);
        if(af_calib < 0) {
            PI_CALIB_LOGE("af_calib abnormal,please check!");
            return -1;
        }
    } else {
        af_calib = API_GetMotorstep(byteParam);
        if(af_calib < 0) {
            PI_CALIB_LOGE("af_calib abnormal,please check!");
            return -1;
        }
    }

    return af_calib;
}
}
