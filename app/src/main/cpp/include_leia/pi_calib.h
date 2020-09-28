#ifndef __API_CALIB_H__
#define __API_CALIB_H__

#include "pi_common_type.h"

#define SC_SUCCESS              0
#define SC_CONFIG_OPEN_FAILED   1
#define SC_SIZE_SETTING_ERROR   2
#define SC_IMAGE_OPEN_FAILED    3
#define SC_IMAGE_SIZE_CONFLICT  4
#define SC_VCM_STEP_INVALID     5

#define SC_ENCRYPT_ERROR        6
#define SC_DECRYPT_ERROR        7

#define SC_IMAGE_SPLIT_ERROR    8
#define SC_NO_CORNER_FOUND      9
#define SC_BASELINE_ERROR       10
#define SC_VECTOR_T_ERROR       11
#define SC_PIXEL_OUTBOUNDARY    12
#define SC_DEVIATION_ERROR      13

#define SC_ERROR                14
#define SC_NO_LICENSE           15
#define SC_CONNECT_FAILED        16
#define SC_COMMUNICATE_FAILED    17
#define SC_UNCLEAR_IMAGE         18
#define SC_LICENSE_ERR           19
#define SC_CREATE_POINT_ERR      20
#define SC_SINGLE_CHECK_ERROR    21
#define SC_PARAM_EMPTY           22

typedef struct __PI_STEREOCALIB_PARAM {
    PIPUChar         pbyteParam;
    PIPChar          paramFile;
    PIInt32          af_calib;
    PIInt32          Reserved[3];
    PIPChar          pReserved[5];
}STEREOCALIB_PARAM, *LP_STEREOCALIB_PARAM;

PI_EXP_API int API_Init(
    PIPVoid     *phandle,
    PIPChar     paramFile,
    PIInt32     socket_on = 1
);

PI_EXP_API int API_Uninit(PIPVoid phandle);

PI_EXP_API int API_StereoCalib_3C(
    LP_ORGINAL_IMAGE        pMainImg,
    LP_ORGINAL_IMAGE        pSubImg,
    LP_ORGINAL_IMAGE        pWideImg,
    LP_STEREOCALIB_PARAM    pStereoParam,
    PIPVoid                 phandle,
    PIPChar                 result
);

PI_EXP_API int API_RectifyImg_3C(
    LP_ORGINAL_IMAGE        pMainImg,
    LP_ORGINAL_IMAGE        pSubImg,
    LP_ORGINAL_IMAGE        pWideImg,
    LP_STEREOCALIB_PARAM    pStereoParam,
    PIPChar                 result
);

PI_EXP_API int API_SendResult(
    PIPVoid    phandle,
    PIInt32    result
);

PI_EXP_API int API_GetFocusMode(
    PIPChar    paramFile
);

PI_EXP_API int API_GetMotorstep(
    PIPUChar   pbyteParam
);

#endif