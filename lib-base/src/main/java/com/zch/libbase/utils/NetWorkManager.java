package com.zch.libbase.utils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zch on 2018/5/3.
 */
public class NetWorkManager {

    private static final Object lock1 = new Object();

    private static Retrofit sRetrofit;

    public static String base_url;

    public static Retrofit getRetrofit() {
        if (sRetrofit != null) {
            return sRetrofit;
        }
        synchronized (lock1) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(base_url) // 设置网络请求 Url
                    .addConverterFactory(GsonConverterFactory.create()) // 设置使用 Gson 解析（记得加入依赖）
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持 RxJava
                    .build();
            return sRetrofit;
        }
    }
}