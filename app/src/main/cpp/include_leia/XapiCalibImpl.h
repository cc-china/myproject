#ifndef __XAPI_CALIB_IMPL_H__
#define __XAPI_CALIB_IMPL_H__
#define MAIN_STILL_WIDTH              4656
#define MAIN_STILL_HEIGHT             3492
#define SUB_STILL_WIDTH                2592
#define SUB_STILL_HEIGHT               1944

namespace android {

class XapiCalibImpl
{
public:
    XapiCalibImpl();
    ~XapiCalibImpl();
    bool init(char *paramFile, int socket_on);
    bool selfCalibration(unsigned char *leftBufferData, unsigned char *rightBufferData,
                                unsigned char *byteParam, char *xmlFile, char *result, int af_calib);
    bool selfRectify(unsigned char *leftBufferData, unsigned char *rightBufferData,
                        unsigned char *byteParam, char *xmlFile, char *result);
    int selfCalibGetAfMode(char *xmlFile);
    int selfRectifyGetAfStep(unsigned char *byteParam);
    int CalibSendResult(int result);

private:
    bool dumpFile(const char* fileName, unsigned char * buf, int size);
    void* mPiCalibHandle;
};

}

#endif

