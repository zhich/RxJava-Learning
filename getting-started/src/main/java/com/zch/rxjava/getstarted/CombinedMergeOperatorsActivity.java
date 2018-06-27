package com.zch.rxjava.getstarted;

import android.util.Log;

import com.zch.libbase.base.BaseActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 组合 / 合并操作符
 * <p>
 * Created by zch on 2018/5/8.
 */
public class CombinedMergeOperatorsActivity extends BaseActivity {

    private static final String TAG = CombinedMergeOperatorsActivity.class.getSimpleName();

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


    Observer mObserverString = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) { // 默认最先调用复写的 onSubscribe()
            Log.e(TAG, "开始采用 subscribe 连接");
        }

        @Override
        public void onNext(String value) {
            Log.e(TAG, "接收到了事件 " + value);
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
//        concat_concatArray(); // 组合多个被观察者，合并后按发送顺序【串行执行】
//        merge_mergeArray(); // 组合多个被观察者，合并后按“时间线”【并行执行】
//        concatDelayError_mergeDelayError(); // 推迟发送 onError 事件
//        zip();
//        combineLatest();
//        combineLatestDelayError(); // 作用类似于 concatDelayError() / mergeDelayError() ，即错误处理
//        reduce(); // 把被观察者需要发送的事件聚合成 1 个事件 & 发送
//        collect(); // 将被观察者 Observable 发送的数据事件收集到一个数据结构里
//        startWith_startWithArray(); // 发送事件前追加发送事件
        count(); // 统计发送事件数量
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
                .subscribe(mObserverInteger);

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
                .subscribe(mObserverInteger);
    }

    private void merge_mergeArray() {
        /**
         * merge 与 mergeArray
         * 作用：
         * 组合多个被观察者一起发送数据，合并后按“时间线”【并行执行】
         * 区别：
         * merge() 组合被观察者数量 <= 4 个，mergeArray() 则 > 4 个
         */
        Observable.merge(
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 从 0 开始发送、共发送 3 个数据、第 1 次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(2, 3, 1, 1, TimeUnit.SECONDS) // 从 2 开始发送、共发送 3 个数据、第 1 次事件延迟发送时间 = 1s、间隔时间 = 1s
        ).subscribe(mObserverInteger);

        // 输出结果顺序：0，2，-> 1，3 -> 2，4
    }

    private void concatDelayError_mergeDelayError() {
        /**
         * concatDelayError 与 mergeDelayError
         * 作用：
         * 使用 concat() 或 merge() 时，若其中一个 Observable 发出 onError 事件，其它的 Observable 会马上终止继续发送事件。
         * 若希望 onError 事件推迟到其它 Observable 发送事件结束后才触发，则需要使用对应的 concatDelayError 或 mergeDelayError
         */

        // 没有使用 concatDelayError() 的情况
        Observable.concat(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onError(new NullPointerException());
                        emitter.onComplete();
                    }
                }),
                Observable.just(4, 5, 6))
                .subscribe(mObserverInteger);

        // 使用 concatDelayError() 的情况
        Observable.concatArrayDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onError(new NullPointerException());
                        emitter.onComplete();
                    }
                }),
                Observable.just(4, 5, 6))
                .subscribe(mObserverInteger);
    }

    private void zip() {
        /**
         * 作用：
         * 合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列），并最终发送
         * 注意：
         * 1、事件组合方式 = 严格按照原先事件序列 进行对位合并
         * 2、最终合并的事件数量 = 多个被观察者（Observable）中数量最少的数量
         */
        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                Log.e(TAG, "被观察者 1 发送了事件 1");
                emitter.onNext(1);
                Thread.sleep(1000);

                Log.e(TAG, "被观察者 1 发送了事件 2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.e(TAG, "被观察者 1 发送了事件 3");
                emitter.onNext(3);
                Thread.sleep(1000);

//                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()); // 设置 observable1 在工作线程 1 中工作

        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                Log.e(TAG, "被观察者 2 发送了事件 A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.e(TAG, "被观察者 2 发送了事件 B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.e(TAG, "被观察者 2 发送了事件 C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.e(TAG, "被观察者 2 发送了事件 D");
                emitter.onNext("D");
                Thread.sleep(1000);

//                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()); // 设置 observable2 在工作线程 2 中工作

        // 假设不作线程控制，则该两个被观察者会在同一个线程中工作，即发送事件存在先后顺序，而不是同时发送

        // 注：创建 BiFunction 对象传入的第 3 个参数 = 合并后数据的数据类型
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String string) throws Exception {
                return integer + string;
            }
        }).subscribe(mObserverString);

        /**
         * 被观察者 1 发送了事件 1
         * 被观察者 2 发送了事件 A
         * 接收到了事件 1A
         * 被观察者 1 发送了事件 2
         * 被观察者 2 发送了事件 B
         * 接收到了事件 2B
         * 被观察者 1 发送了事件 3
         * 被观察者 2 发送了事件 C
         * 接收到了事件 3C
         * 被观察者 2 发送了事件 D
         */

        /**
         * 特别注意：
         * 1、尽管 observable2 的事件 D 没有事件与其合并，但还是会继续发送
         * 2、若在 observable1 & observable2 的事件序列最后发送 onComplete() 事件，则 observable2 的事件 D 也不会发送
         */
    }

    private void combineLatest() {
        /**
         * 作用：
         * 当两个 Observables 中的任何一个发送了数据后，将先发送了数据的 Observables 的最新（最后）一个数据与
         * 另外一个 Observable 发送的每个数据结合，最终基于该函数的结果发送数据
         * 注意：
         * 与 zip() 的区别：zip() = 按个数合并，即 1 对 1 合并；CombineLatest() = 按时间合并，即在同一个时间点上合并
         */
        Observable.combineLatest(
                Observable.just(1L, 2L, 3L), // 第 1 个发送数据事件的 Observable
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 第 2 个发送数据事件的 Observable：从 0 开始发送、共发送 3 个数据、第 1 次事件延迟发送时间 = 1s、间隔时间 = 1s
                new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long o1, Long o2) throws Exception {
                        // o1 = 第 1 个 Observable 发送的最新（最后）1 个数据
                        // o2 = 第 2 个 Observable 发送的每 1 个数据
                        Log.e(TAG, "合并的数据是：" + o1 + " " + o2);
                        // 合并的逻辑 = 相加
                        // 即第 1 个 Observable 发送的最后 1 个数据与第 2 个 Observable 发送的每 1 个数据进行相加
                        return o1 + o2;
                    }
                }
        ).subscribe(mObserverLong);

        /**
         * 合并的数据是：3 0
         * 接收到了事件 3
         * 合并的数据是：3 1
         * 接收到了事件 4
         * 合并的数据是：3 2
         * 接收到了事件 5
         */
    }

    private void combineLatestDelayError() {

    }

    private void reduce() {
        /**
         * 作用：
         * 把被观察者需要发送的事件聚合成 1 个事件 & 发送
         * 注意：
         * 聚合的逻辑根据需求撰写，但本质都是前 2 个数据聚合，然后与后 1 个数据继续进行聚合，依次类推
         */

        Observable.just(1, 2, 3, 4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    // 在该复写方法中复写聚合的逻辑
                    @Override
                    public Integer apply(Integer s1, Integer s2) throws Exception {
                        Log.e(TAG, "本次计算的数据是： " + s1 + " 乘 " + s2);
                        // 本次聚合的逻辑是：全部数据相乘起来
                        // 原理：第 1 次取前 2 个数据相乘，之后每次获取到的数据 = 返回的数据 x 原始下 1 个数据
                        return s1 * s2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "最终计算的结果是： " + integer);
            }
        });

        /**
         * 本次计算的数据是： 1 乘 2
         * 本次计算的数据是： 2 乘 3
         * 本次计算的数据是： 6 乘 4
         * 最终计算的结果是： 24
         */
    }

    private void collect() {
        /**
         * 作用：
         * 将被观察者 Observable 发送的数据事件收集到一个数据结构里
         */
        Observable.just(1, 2, 3, 4, 5, 6)
                .collect(
                        // 1. 创建数据结构（容器），用于收集被观察者发送的数据
                        new Callable<ArrayList<Integer>>() {
                            @Override
                            public ArrayList<Integer> call() throws Exception {
                                return new ArrayList<>();
                            }
                            // 2. 对发送的数据进行收集
                        }, new BiConsumer<ArrayList<Integer>, Integer>() {
                            @Override
                            public void accept(ArrayList<Integer> list, Integer integer) throws Exception {
                                // 参数说明：list = 容器，integer = 后者数据
                                list.add(integer);
                                // 对发送的数据进行收集
                            }
                        }).subscribe(new Consumer<ArrayList<Integer>>() {
            @Override
            public void accept(ArrayList<Integer> list) throws Exception {
                Log.e(TAG, "本次发送的数据是： " + list);

            }
        });
    }

    private void startWith_startWithArray() {
        /**
         * 作用：
         * 在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
         */

        // 在一个被观察者发送事件前，追加发送一些数据
        // 注：追加数据顺序 = 后调用先追加
        Observable.just(4, 5, 6)
                .startWith(0) // 追加单个数据 = startWith()
                .startWithArray(1, 2, 3) // 追加多个数据 = startWithArray()
                .subscribe(mObserverInteger);

        /**
         * 接收到了事件 1
         * 接收到了事件 2
         * 接收到了事件 3
         * 接收到了事件 0
         * 接收到了事件 4
         * 接收到了事件 5
         * 接收到了事件 6
         */

        // 在一个被观察者发送事件前，追加发送被观察者 & 发送数据
        // 注：追加数据顺序 = 后调用先追加
        Observable.just(4, 5, 6)
                .startWith(Observable.just(1, 2, 3))
                .subscribe(mObserverInteger);

        /**
         * 接收到了事件 1
         * 接收到了事件 2
         * 接收到了事件 3
         * 接收到了事件 4
         * 接收到了事件 5
         * 接收到了事件 6
         */
    }

    private void count() {
        /**
         * 作用：
         * 统计被观察者发送事件的数量
         */
        Observable.just(1, 2, 3, 4)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "发送的事件数量 =  " + aLong); // 4
                    }
                });
    }
}
