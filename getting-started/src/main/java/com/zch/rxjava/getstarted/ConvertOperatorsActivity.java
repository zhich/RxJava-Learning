package com.zch.rxjava.getstarted;

import android.util.Log;

import com.zch.libbase.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 变换操作符
 * <p>
 * Created by zch on 2018/5/4.
 */

public class ConvertOperatorsActivity extends BaseActivity {

    private static final String TAG = ConvertOperatorsActivity.class.getSimpleName();

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void init() {
//        map();
//        flatMap();
//        concatMap();
        buffer();
    }

    private void map() {
        /**
         * 作用：
         * 对被观察者发送的每 1 个事件都通过指定的函数处理，从而变换成另外 1 种事件（将被观察者发送的事件转换为任意的类型事件）
         * 应用场景：
         * 数据类型转换
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 1. 被观察者发送事件：参数为整型 1、2、3
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            // 2. 使用 map 变换操作符中的 Function 函数对被观察者发送的事件进行统一变换：整型变换成字符串类型
            @Override
            public String apply(Integer integer) throws Exception {
                return "使用 map 变换操作符将事件 " + integer + " 的参数从整型 " + integer + " 变换成字符串类型 " + integer;
            }
        }).subscribe(new Consumer<String>() {
            // 3. 观察者接收事件时，是接收到变换后的事件：字符串类型
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s);
            }
        });
    }

    private void flatMap() {
        /**
         * 作用：
         * 将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送
         * 原理：
         * 1、为事件序列中每个事件都创建一个 Observable 对象
         * 2、将对每个原始事件转换后的新事件都放入到对应 Observable 对象
         * 3、将新建的每个 Observable 都合并到一个新建的、总的 Observable 对象
         * 4、新建的、总的 Observable 对象将新合并的事件序列发送给观察者（Observer）
         * 应用场景：
         * 无序的将被观察者发送的整个事件序列进行变换
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
            // 采用 flatMap() 变换操作符
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("我是事件 " + integer + " 拆分后的子事件 " + i);
                    // 通过 flatMap 将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送 3 个 String 的事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s);
            }
        });
    }

    private void concatMap() {
        /**
         * 作用：
         * 类似 flatMap() 操作符。与 flatMap() 的区别在于：拆分 & 重新合并生成的事件序列的顺序 = 被观察者旧序列生产的顺序
         * 原理：
         * 1、为事件序列中每个事件都创建一个 Observable 对象
         * 2、将对每个原始事件转换后的新事件都放入到对应 Observable 对象
         * 3、将新建的每个 Observable 都合并到一个新建的、总的 Observable 对象
         * 4、新建的、总的 Observable 对象将新合并的事件序列发送给观察者（Observer）
         * 应用场景：
         * 有序的将被观察者发送的整个事件序列进行变换
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
            // 采用 concatMap() 变换操作符
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("我是事件 " + integer + " 拆分后的子事件 " + i);
                    // 通过 concatMap 将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送 3 个 String 的事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s);
            }
        });
    }

    private void buffer() {
        /**
         * 作用：
         * 定期从被观察者（Obervable）需要发送的事件中获取一定数量的事件 & 放到缓存区中，最终发送
         * 应用场景：
         * 缓存被观察者发送的事件
         */
        Observable.just(1, 2, 3, 4, 5)
                // 设置缓存区大小 & 步长
                // 缓存区大小 = 每次从被观察者中获取的事件数量
                // 步长 = 每次获取新事件的数量
                .buffer(3, 1)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.e(TAG, "缓存区里的事件数量 = " + integers.size());
                        for (Integer value : integers) {
                            Log.e(TAG, "事件 = " + value);
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
