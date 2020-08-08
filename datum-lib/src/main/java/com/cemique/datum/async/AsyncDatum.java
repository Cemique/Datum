package com.cemique.datum.async;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.Assertion;
import com.cemique.datum.DoneCallback;
import com.cemique.datum.GetCallback;
import com.cemique.datum.DatumSource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Base class for different types of async datums.
 */
class AsyncDatum<T> implements AsyncInteraction<T> {

    private DatumSource<T> datumSource;
    private String key;
    private T defaultValue;
    private T value;
    private final Object lock = "lock";

    AsyncDatum(DatumSource<T> datumSource, @NonNull String key, @Nullable T defaultValue) {
        this.datumSource = datumSource;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public final void getValue(GetCallback<T> onGetValue) {
        Looper looper = Looper.myLooper();
        backgroundWork(() -> {
            synchronized (lock) {
                value = datumSource.getValue(key, defaultValue);
                final T finalValue = value;
                new Handler(looper != null ? looper : Looper.getMainLooper()).post(() -> onGetValue.onGetValue(finalValue));
            }
        });
    }

    @Override
    public final void setValue(@NonNull T t, @Nullable DoneCallback onSetValue) {
        Assertion.assertNonNull(t);

        Looper looper = Looper.myLooper();
        backgroundWork(() -> {
            synchronized (lock) {
                datumSource.setValue(key, t);
                value = t;
                new Handler(looper != null ? looper : Looper.getMainLooper()).post(() -> {
                    if (onSetValue != null)
                        onSetValue.callback();
                });
            }
        });
    }

    @Override
    public final void clear(@Nullable DoneCallback onClear) {
        Looper looper = Looper.myLooper();
        backgroundWork(() -> {
            synchronized (lock) {
                datumSource.clear();
                value = defaultValue;
                new Handler(looper != null ? looper : Looper.getMainLooper()).post(() -> {
                    if (onClear != null)
                        onClear.callback();
                });
            }
        });
    }

    @Override
    public final String getKey() {
        return key;
    }

    @Override
    public final T getDefaultValue() {
        return defaultValue;
    }


    //

    private Executor executor;

    private void backgroundWork(Runnable runnable) {
        if (executor == null)
            executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable);
    }

}
