/*******************************************************************************
 *  Copyright(c) 2017-2018 Xi'an PI information technology Co. Ltd, All right reserved.
 *
 *  DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER 
 *  AUTHORIZATION.
*******************************************************************************/
#ifndef __PI_DUALCAM_DISTANCE_H__
#define __PI_DUALCAM_DISTANCE_H__

#include "pi_common_type.h"
#include "pi_dualcam_common.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct _TAG_BDM_PARAM {
    PIPOINT        ptFocus;                          // [in]  The focus point to decide which region should be clear
    PIInt32        i32BlurIntensity;                 // [in]  The intensity of blur,Range is [0,11].
    PIInt32        crossEnable;
    PIInt32        i32Reserved[16];
    PIPVoid        pReserved[8];
} BDM_PARAM, *LP_BDM_PARAM;

typedef struct _TAG_BDM_STATUS {
    PIInt32  i32Distance;
    PIInt32  i32Disparity;
    PIInt32  i32CalculateStatus;
    PIInt32  i32SubCovered;
    PIInt32  i32Reserved[15];
    PIPVoid  pReserved[8];
} BDM_STATUS, *LP_BDM_STATUS;

enum BDM_CALC_STATUS{
    PS_AF_UNSTABLE,
    PS_CALCULATING,
    PS_FINISH,
};

typedef struct {
    PIFloat fx;
    PIFloat fy;
    PIFloat cx;
    PIFloat cy;
    PIFloat k1;
    PIFloat k2;
    PIFloat p1;
    PIFloat p2;
    PIFloat k3;
} BDM_CAMERA_PARAM;

typedef struct {
    BDM_CAMERA_PARAM left;
    BDM_CAMERA_PARAM right;

    PIFloat R[9];
    PIFloat T[3];
} BDM_CALIB_DATA;

/************************************************************************
* the following functions for preview
************************************************************************/
PI_EXP_API PIPCChar BDM_Version();                    // return Version Info string

PI_EXP_API PIRESULT BDM_Init(                         // return PIOK if success, otherwise fail
    PIHandle              *phHandle,                  // [out] The algorithm engine will be initialized by this API
    PIInt32               i32Mode,                    // [in]  The mode of algorithm engine
    PIPChar               configPath
);

PI_EXP_API PIRESULT BDM_SetCameraImageInfo(            // return PIOK if success, otherwise fail
    PIHandle                      hHandle,             // [in]  The algorithm engine
    LP_CAPTURE_CAMERA_IMAGE_PARAM   pParam             // [in]  Camera and image information
);

PI_EXP_API PIRESULT BDM_SetCalibData(                   // return PIOK if success, otherwise fail
    PIHandle            hHandle,                       // [in]  The algorithm engine
    PIPUChar            pCalibData                    // [in]  Calibration param
);

PI_EXP_API PIRESULT BDM_GetCalibData(
    PIPUChar            pCalibData,
    PIPUChar            pCalibParam
);

PI_EXP_API PIRESULT BDM_Preview(                    // return PIOK if success, otherwise fail
    PIHandle                hHandle,                 // [in]  The algorithm engine
    LP_ORGINAL_IMAGE        pLeftImg,                // [in]  The off-screen of left image
    LP_ORGINAL_IMAGE        pDstImg                  // [out]  The off-screen of result image
);

PI_EXP_API PIRESULT BDM_CalcDepth(
    PIHandle                hHandle,                  // [in]  The algorithm engine
    LP_ORGINAL_IMAGE        pLeftImg,                 // [in]  The off-screen of left mini image
    LP_ORGINAL_IMAGE        pRightImg,                // [in]  The off-screen of right image
    LP_BDM_PARAM            pRFParam,                 // [in]  The parameter for finger position
    LP_BDM_STATUS           pStatus                   // [out] The distance of finger and book
);

PI_EXP_API PIRESULT BDM_Uninit(                       // return PIOK if success, otherwise fail
    PIHandle                hHandle                   // [in/out] The algorithm engine will be un-initialized by this API
);

#ifdef __cplusplus
}
#endif

#endif