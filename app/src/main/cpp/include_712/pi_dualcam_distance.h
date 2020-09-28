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

typedef struct _TAG_DPRV_PARAM {
    PIInt32        i32ImgDegree;                        // [in]  The degree of input images
    PIFloat        fMaxFOV;                            // [in]  The maximum camera FOV among horizontal and vertical in degree
    // DPRV_FACE_PARAM    faceParam;                   // [in]  The information of faces in the left image
    PIInt32        i32MaxDistance;
    PIInt32        i32MinDistance;
    PIInt32        i32Reserved[16];
    PIPVoid        pReserved[8];
} DPRV_PARAM, *LP_DPRV_PARAM;

typedef struct _TAG_DPRV_CAPTURE_PARAM {
    PIPOINT        ptFocus;                          // [in]  The focus point to decide which region should be clear
    PIInt32        i32BlurIntensity;                 // [in]  The intensity of blur,Range is [0,11].
    PIInt32        crossEnable;
    PIInt32        i32Reserved[16];
    PIPVoid        pReserved[8];
} DPRV_CAPTURE_PARAM, *LP_DPRV_CAPTURE_PARAM;

typedef struct _TAG_DPRV_STATUS {
    PIInt32  i32Distance;
    PIInt32  i32Disparity;
    PIInt32  i32CalculateStatus;
    PIInt32  i32SubCovered;
    PIInt32  i32Reserved[15];
    PIPVoid  pReserved[8];
} DPRV_STATUS, *LP_DPRV_STATUS;

enum PREVIEW_STATUS{
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
}_CALIB_DATA_HYN;

typedef struct {
    _CALIB_DATA_HYN left;
    _CALIB_DATA_HYN right;
    
    PIFloat R[9];
    PIFloat T[3];
} CAMCALIB_HYN;

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

PI_EXP_API PIRESULT BDM_SetCaliData(                   // return PIOK if success, otherwise fail
    PIHandle            hHandle,                       // [in]  The algorithm engine
    PIPUChar            pCalibParam                    // [in]  Calibration param
);

PI_EXP_API PIRESULT BDM_GetCalidata(
    PIPUChar            pCalibParam,
    PIPUChar            pHYNParam
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
    LP_DPRV_CAPTURE_PARAM   pRFParam,                 // [in]  The parameter for finger position
    LP_DPRV_STATUS          pStatus                   // [out] The distance of finger and book
);

PI_EXP_API PIRESULT BDM_Uninit(                       // return PIOK if success, otherwise fail
    PIHandle                hHandle                   // [in/out] The algorithm engine will be un-initialized by this API
);

#ifdef __cplusplus
}
#endif

#endif