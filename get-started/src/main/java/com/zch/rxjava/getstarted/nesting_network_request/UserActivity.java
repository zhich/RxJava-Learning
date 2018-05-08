package com.zch.rxjava.getstarted.nesting_network_request;

import android.annotation.SuppressLint;
import android.util.Log;

import com.zch.libbase.base.BaseActivity;
import com.zch.libbase.utils.JsonUtils;
import com.zch.libbase.utils.NetWorkManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络请求嵌套回调
 * <p>
 * Created by zch on 2018/5/5.
 */
public class UserActivity extends BaseActivity {

    private static final String TAG = UserActivity.class.getSimpleName();

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init() {
        NetWorkManager.base_url = "http://fy.iciba.com/"; // 设置网络请求 url
        IUserService request = NetWorkManager.getRetrofit().create(IUserService.class);

        final Observable<RegisterBean> registerObservable = request.register(); // 模拟注册
        final Observable<LoginBean> loginObservable = request.login(); // 模拟登录

        registerObservable.subscribeOn(Schedulers.io()) // 【注册 Observable 】 切换到 IO 线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 【注册 Observer 】切换到主线程处理网络请求结果
                .doOnNext(new Consumer<RegisterBean>() {
                    @Override
                    public void accept(RegisterBean registerBean) throws Exception {
//                        registerBean.status = 0; // 测试
                        Log.e(TAG, "注册成功：" + registerBean.content.out);
                    }
                })
                .observeOn(Schedulers.io()) // 【注册 Observable 】切换到 IO 线程，准备发起登录网络请求
                // 因为 flatMap 是对【注册 Observable 】作变换，所以对于【注册 Observable 】，它是【登录 Observer】，所以通过 observeOn 切换线程
                .flatMap(new Function<RegisterBean, ObservableSource<LoginBean>>() { // 作变换，即作嵌套网络请求
                    @Override
                    public ObservableSource<LoginBean> apply(RegisterBean registerBean) throws Exception {
                        Log.e(TAG, "执行了 flatMap，" + JsonUtils.toJson(registerBean));
                        if (registerBean.status == 1) { // 注册成功了，将注册网络请求转换为登录网络请求
                            return loginObservable;
                        } else { // 注册失败，返回 Observable.error();
                            return Observable.error(new RuntimeException("注册失败啦xxx"));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//【登录 Observer】切换到主线程处理登录网络请求的结果
                .subscribe(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean translationBean2) throws Exception {
                        Log.e(TAG, "登录成功：" + translationBean2.content.out);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "操作失败-------" + throwable.getMessage());
                    }
                });
    }
}