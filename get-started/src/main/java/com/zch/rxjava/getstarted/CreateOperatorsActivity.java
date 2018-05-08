package com.zch.rxjava.getstarted;

import android.util.Log;

import com.zch.libbase.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <p>
 * 创建操作符（创建被观察者对象 & 发送事件）
 * Created by zch on 2018/5/3.
 */

public class CreateOperatorsActivity extends BaseActivity {

    private static final String TAG = CreateOperatorsActivity.class.getSimpleName();

    Observer mObserverInteger = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) { // 默认最先调用复写的 onSubscribe()
            Log.e(TAG, "开始采用 subscribe 连接");
        }

        @Override
        public void onNext(Integer integer) {
            Log.e(TAG, "接收到了事件 " + integer);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "对 Error 事件作出响应---" + e.getMessage());
        }

        @Override
        public void onComplete() {
            Log.e(TAG, "对 Complete 事件作出响应");
            Log.e(TAG, "----------------------------------------------------------------------------------------");
        }
    };

    Observer mObserverLong = new Observer<Long>() {
        @Override
        public void onSubscribe(Disposable d) { // 默认最先调用复写的 onSubscribe()
            Log.e(TAG, "开始采用 subscribe 连接");
        }

        @Override
        public void onNext(Long l) {
            Log.e(TAG, "接收到了事件 " + l);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "对 Error 事件作出响应---" + e.getMessage());
        }

        @Override
        public void onComplete() {
            Log.e(TAG, "对 Complete 事件作出响应");
            Log.e(TAG, "----------------------------------------------------------------------------------------");
        }
    };

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void init() {
//        create(); // 基本创建
//        quicklyCreateAndSendEvent(); // 快速创建 & 发送事件 （just、fromArray、fromIterable）
//        useForTestOperators(); // 一般用于测试使用的操作符（empty、error、never）
        delayOperators(); // 延迟创建。defer、timer、interval、intervalRange、range、rangeLong
    }

    private void create() {
        // 1. 通过 create() 创建被观察者对象
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            // 2. 在复写的 subscribe() 里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);

                emitter.onComplete();
            } // 至此，一个被观察者对象（Observable）就创建完毕
        });

        observable.subscribe(mObserverInteger);
    }

    private void quicklyCreateAndSendEvent() {
        /**
         * 作用：
         * 1、快速创建 1 个被观察者对象（Observable）
         * 2、发送事件的特点：直接发送 传入的事件（最多只能发送 10 个事件）
         * 应用场景：
         * 1、快速创建 被观察者对象（Observable） & 发送 10 个以下事件
         */
        // 创建时传入整型1、2、3、4，在创建后就会发送这些对象，相当于执行了 onNext(1)、onNext(2)、onNext(3)、onNext(4)
        Observable.just(1, 2, 3, 4)
                .subscribe(mObserverInteger);

        /**
         * 作用：
         * 1、快速创建 1 个被观察者对象（Observable）
         * 2、发送事件的特点：直接发送 传入的数组数据（会将数组中的数据转换为 Observable 对象）
         * 应用场景：
         * 1、快速创建 被观察者对象（Observable） & 发送 10 个以上事件（数组形式）
         * 2、数组元素遍历
         */
        Integer[] numbers = {0, 1, 2, 3, 4};
        Observable.fromArray(numbers)
                .subscribe(mObserverInteger);


        /**
         * 作用：
         * 1、快速创建 1 个被观察者对象（Observable）
         * 2、直接发送 传入的集合 List 数据（会将集合中的数据转换为 Observable 对象）
         * 应用场景：
         * 1、快速创建 被观察者对象（Observable） & 发送 10 个以上事件（集合形式）
         * 2、集合元素遍历
         */
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(11);
        list.add(12);
        Observable.fromIterable(list)
                .subscribe(mObserverInteger);
    }

    private void useForTestOperators() {
        /**
         * empty()
         * 该方法创建的被观察者对象发送事件的特点：仅发送 Complete 事件，直接通知完成
         * 即观察者接收后会直接调用 onCompleted()
         */
        Observable observable1 = Observable.empty();
        observable1.subscribe(mObserverInteger);

        /**
         * error()
         * 该方法创建的被观察者对象发送事件的特点：仅发送 Error 事件，直接通知异常
         * 即观察者接收后会直接调用 onError()
         * 可自定义异常
         */
        Observable observable2 = Observable.error(new RuntimeException("运行时出错啦"));
        observable2.subscribe(mObserverInteger);

        /**
         * never()
         * 该方法创建的被观察者对象发送事件的特点：不发送任何事件
         * 即观察者接收后什么都不调用
         */
        Observable observable3 = Observable.never();
        observable3.subscribe(mObserverInteger);
    }

    private void delayOperators() {
//        defer();
//        timer();
//        interval();
//        intervalRange();
        range();
    }

    Integer x = 10; // 第 1 次对 x 赋值

    private void defer() {
        /**
         * 作用：
         * 直到有观察者（Observer）订阅时，才动态创建被观察者对象（Observable） & 发送事件
         * ①、通过 Observable 工厂方法创建被观察者对象（Observable）
         * ②、每次订阅后，都会得到一个刚创建的最新的 Observable 对象，这可以确保 Observable 对象里的数据是最新的
         * 应用场景：
         * 动态创建被观察者对象（Observable） & 获取最新的 Observable 对象数据
         */
        // 通过 defer 定义被观察者对象（注：此时被观察者 observable 对象还没创建）
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(x);
            }
        });
        x = 15; // 第 2 次对 x 赋值

        // 观察者开始订阅（注：此时，才会调用 defer 创建被观察者对象 Observable）
        observable.subscribe(mObserverInteger);
    }

    private void timer() {
        /**
         * 作用：
         * 1、快速创建 1 个被观察者对象（Observable）
         * 2、发送事件的特点：延迟指定时间后，发送 1 个数值 0（Long 类型）。
         * 本质：
         * 延迟指定时间后，调用一次 onNext(0)
         * 应用场景：
         * 延迟指定事件，发送一个 0，一般用于检测
         */
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(mObserverLong);

        // 注：timer 操作符默认在 computation 调度器上执行
        // 也可自定义线程调度器（第 3 个参数）：timer(long,TimeUnit,Scheduler)
    }

    private void interval() {
        /**
         * 作用：
         * 1、快速创建 1 个被观察者对象（Observable）
         * 2、发送事件的特点：每隔指定时间就发送事件
         * 本质：
         * 发送的事件序列 = 从 0 开始、无限递增 1 的整数序列
         * 应用场景：
         * 定时器
         */
        /**
         * 参数说明：
         * 参数 1 = 第 1 次延迟时间
         * 参数 2 = 间隔时间数字
         * 参数 3 = 时间单位
         */
        Observable.interval(3, 1, TimeUnit.SECONDS)
                .subscribe(mObserverLong);

        // 注：interval 默认在 computation 调度器上执行
        // 也可自定义指定线程调度器（第 3 个参数）：interval(long,TimeUnit,Scheduler)
    }

    private void intervalRange() {
        /**
         * 作用：
         * 1、快速创建 1 个被观察者对象（Observable）
         * 2、发送事件的特点：每隔指定时间就发送事件，可指定发送的数据的数量
         * 本质：
         * 1、发送的事件序列 = 从 0 开始、无限递增 1 的整数序列
         * 2、作用类似于 interval()，但可指定发送的数据的数量
         */
        /**
         * 参数说明：
         * 参数 1 = 事件序列起始点
         * 参数 2 = 事件数量
         * 参数 3 = 第 1 次事件延迟发送时间
         * 参数 4 = 间隔时间
         * 参数 5 = 时间单位
         */
        Observable.intervalRange(2, 10, 3, 1, TimeUnit.SECONDS)
                .subscribe(mObserverLong);
    }

    private void range() {
        /**
         * 作用：
         * 1、快速创建 1 个被观察者对象（Observable）
         * 2、发送事件的特点：连续发送 1 个事件序列，可指定范围
         * 本质：
         * 1、发送的事件序列 = 从 0 开始、无限递增 1 的整数序列
         * 2、作用类似于 intervalRange()，但区别在于：无延迟发送事件
         */
        /**
         * 参数说明：
         * 参数 1 = 事件序列起始点
         * 参数 2 = 事件数量
         * 注：若设置为负数，则会抛出异常
         */
        Observable.range(3, 10)
                .subscribe(mObserverInteger);

        /**
         * rangeLong
         * 作用：类似于range()，区别在于该方法支持数据类型 = Long
         */
    }
}
