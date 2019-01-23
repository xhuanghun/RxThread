package com.example.RxThread;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView tvContent;

    private void assignViews() {
        tvContent = (TextView) findViewById(R.id.tv_content);
    }


    private Observer<Long> observer = new Observer<Long>() {
        Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            Log.e("wyn", "onSubscribe");

            Log.e("wyn", "onSubscribe thread is " + Thread.currentThread().getName());

            disposable = d;
        }

        @Override
        public void onNext(Long s) {
            Log.d("wyn", "onNext is " + s);

            Log.e("wyn", "onNext thread is " + Thread.currentThread().getName());

            tvContent.setText(s + "");

            if (s == 10) {
                disposable.dispose();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e("wyn", "onError");

            Log.e("wyn", "onError thread is " + Thread.currentThread().getName());
        }

        @Override
        public void onComplete() {
            Log.e("wyn", "onComplete");

            Log.e("wyn", "onComplete thread is " + Thread.currentThread().getName());
        }
    };

    private Observer<String> observerString = new Observer<String>() {
        Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            Log.e("wyn", "onSubscribe");

            Log.e("wyn", "onSubscribe thread is " + Thread.currentThread().getName());

            disposable = d;
        }

        @Override
        public void onNext(String s) {
            Log.d("wyn", "onNext is " + s);

            Log.e("wyn", "onNext thread is " + Thread.currentThread().getName());

            tvContent.setText(s);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("wyn", "onError");

            Log.e("wyn", "onError thread is " + Thread.currentThread().getName());
        }

        @Override
        public void onComplete() {
            Log.e("wyn", "onComplete");

            Log.e("wyn", "onComplete thread is " + Thread.currentThread().getName());
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();


        //        testJust();

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                testCreate();
//            }
//        }.start();

//        testCreate();

//        testInterval();


//        testTimer();

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                testTimer();
//            }
//        }.start();

//        Log.e("wyn", "111");
//        postDelay();

        // 在子线程中执行会崩溃 Can't create handler inside thread that has not called Looper.prepare()
//        Log.e("wyn", "222");
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                postDelay();
//            }
//        }.start();


        postDelay();

    }





//    private void testJust() {
//        Observable.just("123", "222", "1243")
//            .subscribe(this.observer);
//    }
//
    private void testCreate() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.e("wyn", "ObservableEmitter");

                Log.e("wyn", "ObservableEmitter thread is " + Thread.currentThread().getName());


                long a = 1;
                for (int i = 0; i < 1000000000; i++) {
                    a = a + (a + 1);
                }

                Log.e("wyn", "a is " + a);

                emitter.onNext("wang" + a);
                emitter.onNext("yinan");

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.observerString);
    }

    private void testInterval() {
//        Log.e("wyn", "111");
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.observer);

//        Log.e("wyn", "222");
    }

    private void testTimer() {
//        Log.e("wyn", "333");

        Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.observer);

//        Log.e("wyn", "444");
    }


    private void postDelay() {
        Log.e("wyn", "postDelay thread is " + Thread.currentThread().getName());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("wyn", "postDelayed");

                Log.e("wyn", "postDelayed thread is " + Thread.currentThread().getName());

                tvContent.setText("好吧，我就是测试下");
            }
        }, 3000);
    }

}
