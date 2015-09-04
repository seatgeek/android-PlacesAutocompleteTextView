package com.seatgeek.placesautocomplete.async;

import android.os.Handler;
import android.os.Looper;

import com.seatgeek.placesautocomplete.Constants;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum BackgroundExecutorService {
    INSTANCE;

    /*
     * Max single thread ExecutorService that will spin down thread after use
     */
    private final Executor mExecutor;
    {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
                5L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(final Runnable r) {
                        return new Thread(r, Constants.LOG_TAG + "Thread");
                    }
                });
        executor.allowCoreThreadTimeOut(true);
        mExecutor = executor;
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public <R> void enqueue(final BackgroundJob<R> job) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final R result = job.executeInBackground();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            job.onSuccess(result);
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            job.onFailure(e);
                        }
                    });
                }
            }
        });
    }
}
