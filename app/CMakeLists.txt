#CMakeLists.txt
cmake_minimum_required(VERSION 3.4.1)

# 指定so生成到libs目录
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI})
#add_library( testJNI SHARED src/main/jni/testjni.c)
#add_library(linkJNI SHARED src/main/cpp/launchScan.cpp)

#add_library( pi_calib_data_jni SHARED src/main/cpp/include_709/JniXapiCalibData.cpp)
#add_library( pi_calib_data_jni SHARED src/main/cpp/include_717/JniXapiCalibData.cpp)
add_library( pi_calib_jni SHARED src/main/cpp/include_leia/*.cpp)


#include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include_717/)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include_leia/)

#jni里链接第三方so库
#set_target_properties(pi_calib_data_jni PROPERTIES output_name "pi_calib_data_jni")
set_target_properties(pi_calib_jni PROPERTIES output_name "pi_calib_jni")

#find_library(bdm ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include_717/libxapi_bdm.so)
find_library(calib ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include_leia/xapi_calib.so)

#target_link_libraries(pi_calib_data_jni ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include_709/libxapi_bdm.so)
#target_link_libraries(pi_calib_data_jni ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include_717/libxapi_bdm.so)
#target_link_libraries(pi_calib_data_jni ${CMAKE_CURRENT_SOURCE_DIR}/libs/arm64-v8a/libxapi_bdm.so)
#target_link_libraries(pi_calib_data_jni ${CMAKE_CURRENT_SOURCE_DIR}/libs/armeabi-v7a/libxapi_bdm.so)


target_link_libraries(pi_calib_jni ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/include_leia/libxapi_calib.so)

 find_library( log-lib log )

 #target_link_libraries(linkJNI ${log-lib} )
 #target_link_libraries(pi_calib_data_jni ${log-lib} )
 target_link_libraries(pi_calib_jni ${log-lib} )
