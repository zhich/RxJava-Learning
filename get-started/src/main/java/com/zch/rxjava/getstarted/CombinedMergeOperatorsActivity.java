package com.zch.rxjava.getstarted;

import android.util.Log;

import com.zch.libbase.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 组合 / 合并操作符
 * <p>
 * Created by zch on 2018/5/8.
 */
public class CombinedMergeOperatorsActivity extends BaseActivity {

    private static final String TAG = CombinedMergeOperatorsActivity.class.getSimpleName();

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void init() {
        concat_concatArray(); // 组合多个被观察者
    }

    private void concat_concatArray() {
        /**
         * 作用：
         * 组合多个被观察者一起发送数据，合并后按发送顺序【串行执行】
         * 注意：
         * concat() 组合被观察者数量 ≤ 4 个
         */
        Observable.concat(Observable.just(1, 2, 3),
                Observable.just(4, 5, 6),
                Observable.just(7, 8, 9),
                Observable.just(10, 11, 12))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e(TAG, "接收到了事件 " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "对 Error 事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "对 Complete 事件作出响应-----------------------------------");
                    }
                });

        /**
         * 作用：
         * 组合多个被观察者一起发送数据，合并后按发送顺序【串行执行】
         * 注意：
         * concatArray() 组合被观察者数量 > 4 个
         */
        Observable.concatArray(Observable.just(1, 2, 3),
                Observable.just(4, 5, 6),
                Observable.just(7, 8, 9),
                Observable.just(10, 11, 12),
                Observable.just(13, 14, 15))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e(TAG, "接收到了事件 " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "对 Error 事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "对 Complete 事件作出响应-----------------------------------");
                    }
                });
    }
}
