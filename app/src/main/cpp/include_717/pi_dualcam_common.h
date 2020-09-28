/*******************************************************************************
 *  Copyright(c) 2017-2018 Xi'an PI information technology Co. Ltd, All right reserved.
 *
 *  DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER 
 *  AUTHORIZATION.
*******************************************************************************/
#ifndef __PI_DUALCAM_COMMON_H__
#define __PI_DUALCAM_COMMON_H__

#include "pi_common_type.h"

typedef struct __TAG_RECT
{
   PIInt32 left;
   PIInt32 top;
   PIInt32 right;
   PIInt32 bottom;
} PIRECT, *LP_PIRECT;

typedef struct __TAG_POINT
{ 
   PIInt32 x; 
   PIInt32 y; 
} PIPOINT, *LP_PIPOINT;

typedef struct _TAG_CAPTURE_CAMERA_IMAGE_PARAM
{
   PIInt32              i32OriginWidth;               //Width of Left full image by ISP.
   PIInt32              i32OriginHeight;              //Height of Left full image by ISP.
   PIInt32              i32MiniWidth;              //Width of Right full image by ISP.
   PIInt32              i32MiniHeight;             //Height of Right full image by ISP.
   PIInt32              i32AfStep;
   PIInt32              i32AfStepStableFlag;
   PIPChar              i32ImageStorePath;
   PIInt32              i32Reserved[15];
   PIPVoid              pReserved[8];
} CAPTURE_CAMERA_IMAGE_PARAM, *LP_CAPTURE_CAMERA_IMAGE_PARAM;

enum e_aperture_level {
    APERTURE_F1_0,
    APERTURE_F1_4,
    APERTURE_F2_0,
    APERTURE_F2_8,
    APERTURE_F4_0,
    APERTURE_F5_6,
    APERTURE_F8_0,
    APERTURE_F11,
    APERTURE_F16,
};

#endif