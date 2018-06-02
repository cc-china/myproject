package com.my_project.test_rx_java.retrofit;

import com.my_project.test_rx_java.javabean.Translation;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by com_c on 2018/3/27.
 */

public interface GetRequest_Interface {

    @FormUrlEncoded
    @POST("ajax.php?a=fy&f=auto&t=auto")
    Observable<Translation> getCall(
            @Field("w") String context
    );

    //评论---多图片上传
    @Multipart
    @POST("evaluate/addEvaluate.htm")
    Observable<Translation> EvaluateComment2(
//            @PartMap Map<String, RequestBody> map,//可以传多个参数
//            @PartMap Map<String, MultipartBody.Part> file//可以上传多张图片
            @Part("loginKey") RequestBody loginKey,
            @Part("orderId") RequestBody orderId,
            @Part("goodsId") RequestBody goodsId,
            @Part("evaluate_info") RequestBody evaluate_info,
            @Part("description_evaluate") RequestBody description_evaluate,
            @Part("ship_evaluate") RequestBody ship_evaluate,
            @Part MultipartBody.Part file0,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2);
}
