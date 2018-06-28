package com.zch.rxjava.getstarted;

import android.util.Log;

import com.zch.libbase.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * 从磁盘 / 内存缓存中获取缓存数据
 * <p>
 * Created by zch on 2018/6/28.
 */
public class GetDataFromCacheActivity extends BaseActivity {

    private static final String TAG = GetDataFromCacheActivity.class.getSimpleName();

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void init() {
        final String memoryCache = null;
        final String diskCache = "从磁盘缓存中获取数据";

        /*
         * 设置第 1 个 Observable：检查内存缓存是否有该数据的缓存
         **/
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 先判断内存缓存有无数据
                if (memoryCache != null) {
                    emitter.onNext(memoryCache); // 若有该数据，则发送
                } else {
                    emitter.onComplete(); // 若无该数据，则直接发送结束事件
                }
            }
        });

         /*
         * 设置第 2 个 Observable：检查磁盘缓存是否有该数据的缓存
         **/
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 先判断磁盘缓存有无数据
                if (diskCache != null) {
                    emitter.onNext(diskCache); // 若有该数据，则发送
                } else {
                    emitter.onComplete(); // 若无该数据，则直接发送结束事件
                }
            }
        });

          /*
         * 设置第 3 个 Observable：通过网络获取数据
         **/
        Observable<String> network = Observable.just("从网络中获取数据");

        /*
         * 通过 concat() 和 firstElement() 操作符实现缓存功能
         **/
        /**
         * 1. 通过 concat() 合并 memory、disk、network 3 个被观察者的事件（即检查内存缓存、磁盘缓存 & 发送网络请求）并将它们按顺序串联成队列
         */
        Observable.concat(memory, disk, network)
                // 2. 通过 firstElement()，从串联队列中取出并发送第 1 个有效事件（Next 事件），即依次判断检查 memory、disk、network
                .firstElement()
                // 即本例的逻辑为：
                // a. firstElement() 取出第 1 个事件 = memory，即先判断内存缓存中有无数据缓存；由于  memoryCache = null，即内存缓存中无数据，所以发送结束事件（视为无效事件）
                // b. firstElement() 继续取出第 2 个事件 = disk，即判断磁盘缓存中有无数据缓存：由于 diskCache ≠ null，即磁盘缓存中有数据，所以发送 Next 事件（有效事件）
                // c. 即 firstElement() 已发出第 1 个有效事件（disk 事件），所以停止判断。

                // 3. 观察者订阅
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "最终获取的数据来源 =  " + s);
                    }
                });
    }
}
