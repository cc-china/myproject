package com.my_project.test_rx_java.retrofit;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by com_c on 2018/3/27.
 */

public class RetrofitUtils {
    //懒汉式
    private static RetrofitUtils retrofit1 = null;
    private static Retrofit retrofit;
    private static String BASE_URL = "http://fy.iciba.com/";

    private RetrofitUtils() {
    }

    public static synchronized RetrofitUtils getInstance1() {
        if (retrofit1 == null) {
            retrofit1 = new RetrofitUtils();
        }
        return retrofit1;
    }

    //饿汉式
    private static RetrofitUtils retrofit2 = new RetrofitUtils();

    // private Retrofit(){}
    public static RetrofitUtils getInstance2() {
        return retrofit2;
    }

    //双重同步锁方式
    private static RetrofitUtils retrofit3 = null;

    // private Retrofit(){}
    public static RetrofitUtils getInstance3() {
        if (retrofit3 == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofit3 == null) {
                    retrofit3 = new RetrofitUtils();
                }
            }
        }
        return retrofit3;
    }

    //create RetrofitAPI
    public static GetRequest_Interface createrApi() {
        if (retrofit == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofit == null) {
                    //打印服务器请求头
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    //okhttp client
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(interceptor)
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .writeTimeout(5, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .build();
                    //Retrofit 实例化并返回
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(new Gson()))
                            .build();
                }
            }
        }
        return retrofit.create(GetRequest_Interface.class);
    }
}
