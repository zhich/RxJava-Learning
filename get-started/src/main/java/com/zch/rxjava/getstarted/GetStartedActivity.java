package com.zch.rxjava.getstarted;

import android.util.Log;

import com.zch.libbase.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 入门介绍
 * <p>
 * Created by zch on 2018/5/2.
 */

public class GetStartedActivity extends BaseActivity {

    private static final String TAG = GetStartedActivity.class.getSimpleName();

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void init() {
//        first(); // 基础使用 & 原理
//        second(); // just、from 使用 & 原理
//        third(); // 被观察者 Observable 的 subscribe() 具备多个重载的方法
        fourth(); // 可采用 Disposable.dispose() 切断观察者与被观察者之间的连接。即观察者无法继续接收被观察者的事件，但被观察者还是可以继续发送事件
    }

    private void first() {
        // 1. 创建被观察者 & 生产事件
        // 2. 通过通过订阅（subscribe）连接观察者和被观察者
        // 3. 创建观察者 & 定义响应事件的行为

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            // 在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 通过 ObservableEmitter类对象产生事件并通知观察者
                // ObservableEmitter类介绍
                // a. 定义：事件发射器
                // b. 作用：定义需要发送的事件 & 向观察者发送事件

                Log.e(TAG, "subscribe1");
                emitter.onNext(1);
                Log.e(TAG, "subscribe2");
                emitter.onNext(2);
                Log.e(TAG, "subscribe3");
                emitter.onComplete();
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) { // 默认最先调用复写的 onSubscribe（）
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext_" + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        };

        observable.subscribe(observer);

        // 注：整体方法调用顺序：观察者.onSubscribe（）> 被观察者.subscribe（）> 观察者.onNext（）>观察者.onComplete()
    }

    private void second() {
        Observable observable = Observable.just("A", "B", "C");
        // just(T...)：直接将传入的参数依次发送出来
        // 将会依次调用：
        // onNext("A");
        // onNext("B");
        // onNext("C");
        // onCompleted();

        String[] words = {"A", "B", "C"};
        Observable observable2 = Observable.fromArray(words);
        // from(T[]) / from(Iterable<? extends T>) : 将传入的数组 / Iterable 拆分成具体对象后，依次发送出来
        // 将会依次调用：
        // onNext("A");
        // onNext("B");
        // onNext("C");
        // onCompleted();

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                Log.e(TAG, "onNext_" + o);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        };

//        observable.subscribe(observer);
        observable2.subscribe(observer);
    }

    private void third() {
        // 表示观察者只对被观察者发送的 Next 事件作出响应
        Observable.just("hello", "world")
                .subscribe(new Consumer<String>() {
                    // 每次接收到 Observable 的事件都会调用 Consumer.accept()
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept---" + s);
                    }
                });

        // 表示观察者只对被观察者发送的 Next 事件 & Error 事件作出响应
        Observable.just("xx")
                .subscribe(new Consumer<String>() {
                    // 每次接收到 Observable 的事件都会调用 Consumer.accept()
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept---" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "onError");
                    }
                });

        // 表示观察者只对被观察者发送的 Next 事件、Error 事件 & Complete 事件作出响应
        Observable.just("yy")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept---" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "onError");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "onComplete");
                    }
                });

        // 表示观察者只对被观察者发送的 Next 事件、Error 事件 、Complete 事件 & onSubscribe 事件作出响应
        Observable.just("zch")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept---" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "onError");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "onComplete");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e(TAG, "onSubscribe");
                    }
                });

        // 表示观察者对被观察者发送的任何事件都作出响应
        Observable.just("any")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "onNext---" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }

    private void fourth() {
        Integer[] numbers = {1, 2, 3, 4};
        Observable.fromArray(numbers)
                .subscribe(new Observer<Integer>() {

                    // 1. 定义 Disposable 类变量
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "开始采用 subscribe 连接");
                        // 2. 对Disposable类变量赋值
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "对 Next 事件 " + integer + " 作出响应");
                        if (integer == 2) {
                            // 设置在接收到第二个事件后切断观察者和被观察者的连接
                            mDisposable.dispose();
                            Log.e(TAG, "已经切断了连接：" + mDisposable.isDisposed());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "对 Error 事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "对 Complete 事件作出响应");
                    }
                });
    }
}
