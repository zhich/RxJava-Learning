package com.zch.rxjava.getstarted.unconditional_network_request_polling;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by zch on 2018/5/3.
 */
public interface IGetTranslation {

    /**
     * 注解里传入网络请求的部分 url 地址
     * Retrofit 把网络请求的 url 分成了两部分：一部分放在 Retrofit 对象里，另一部分放在网络请求接口里
     * 如果接口里的 url 是一个完整的网址，那么放在 Retrofit 对象里的 url 可以忽略
     * 采用 Observable<...> 接口
     * getCall() 是接受网络请求数据的方法
     *
     * @return
     */
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<TranslationBean> getCall();
}