package com.zch.rxjava.getstarted.nesting_network_request;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by zch on 2018/5/5.
 */
public interface IUserService {

    /**
     * 网络请求 1
     *
     * @return
     */
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20register")
    Observable<RegisterBean> register();

    /**
     * 网络请求 2
     *
     * @return
     */
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20login")
    Observable<LoginBean> login();
}