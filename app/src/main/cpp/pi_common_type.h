/*******************************************************************************
 *  Copyright(c) 2017-2018 Xi'an PI information technology Co. Ltd, All right reserved.
 *
 *  DO NOT DISTRIBUTE, DO NOT DUPLICATE OR TRANSMIT IN ANY FORM WITHOUT PROPER 
 *  AUTHORIZATION.
*******************************************************************************/
#ifndef __PI_COMMON_TYPE_H__
#define __PI_COMMON_TYPE_H__

#ifdef __ANDROID__
#define PI_EXP_API __attribute__((visibility ("default")))
#else 
#define PI_EXP_API
#endif

typedef long                    PILong;
typedef float                   PIFloat;
typedef double                  PIDouble;
typedef unsigned char           PIByte;
typedef unsigned short          PIWord;    
typedef unsigned int            PIDWord;    
typedef void*                   PIHandle;
typedef char                    PIChar;
typedef long                    PIBool;
typedef void                    PIVoid;
typedef void*                   PIPVoid;
typedef char*                   PIPChar;
typedef unsigned char*          PIPUChar;
typedef short                   PIShort;
typedef const char*             PIPCChar;
typedef PILong                  PIRESULT;
typedef PIDWord                 PICOLORREF; 
typedef signed char             PIInt8;
typedef unsigned char           PIUInt8;
typedef signed short            PIInt16;
typedef unsigned short          PIUInt16;
typedef signed int              PIInt32;
typedef unsigned int            PIUInt32;

/*Define the image format space*/        
typedef struct __TAG_ORGINAL_IMAGE
{
    PIUInt32   u32PixelArrayFormat;
    PIInt32    i32Width;
    PIInt32    i32Height;
    PIInt32    i32Rotate;
    PIInt32    i32Mirror;
    PIInt32    i32RawSparkle;
    PIInt32    i32RawExposureTime;         // Exposure time of the main picture
    PIInt32    i32RawExposure;             // iso
    PIInt32    i32Reserved[13];
    PIUInt8*   ppu8Plane[4];               //pixel data array
    PIInt32    pi32Pitch[4];               //line length array in bytes
    PIPVoid    pRawBuffer;
    PIPVoid    pReserved[7];
} ORGINAL_IMAGE, *LP_ORGINAL_IMAGE;

#define PIOK             0
#define PIERR            1
#define PINull           NULL

#define PI_PAF_YV12   0
#define PI_PAF_NV21   1
#define PI_PAF_RGBA   2
#define PI_PAF_NV12   3

#endif