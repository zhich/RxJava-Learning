package com.zch.rxjava.getstarted.unconditional_network_request_polling;

import android.util.Log;

import com.zch.libbase.base.BaseActivity;
import com.zch.libbase.utils.NetWorkManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 采用 Get 方法对金山词霸 API 按规定时间重复发送网络请求，从而模拟轮询需求实现
 * <p>
 * Created by zch on 2018/5/3.
 */
public class GetTranslationActivity extends BaseActivity {

    private static final String TAG = GetTranslationActivity.class.getSimpleName();

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void init() {
        /*
         * 采用 interval() 延迟发送
         * 注：此处主要展示无限次轮询，若要实现有限次轮询，仅需将 interval() 改成 intervalRange() 即可
         **/
        Observable.interval(2, 3, TimeUnit.SECONDS)
                // 每次发送数字前发送 1 次网络请求，doOnNext() 在执行 Next 事件前调用
                // 即每隔 1 秒产生 1 个数字前，就发送 1 次网络请求，从而实现轮询需求
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "第 " + aLong + " 次轮询");

                        NetWorkManager.base_url = "http://fy.iciba.com/"; // 设置网络请求 url
                        // 创建网络请求接口的实例
                        IGetTranslation request = NetWorkManager.getRetrofit().create(IGetTranslation.class);
                        // 采用 Observable<...> 形式对网络请求进行封装
                        Observable<TranslationBean> observable = request.getCall();
                        // 通过线程切换发送网络请求
                        observable.subscribeOn(Schedulers.io())  // 切换到 IO 线程进行网络请求
                                .observeOn(AndroidSchedulers.mainThread()) // 切换回到主线程处理请求结果
                                .subscribe(new Observer<TranslationBean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(TranslationBean translationBean) {
                                        Log.e(TAG, translationBean.content.out);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, "Error ---" + e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
//                Log.e(TAG, "接收到了事件 " + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "对 Error 事件作出响应---" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, " 对 Complete 事件作出响应");
            }
        });
    }

}