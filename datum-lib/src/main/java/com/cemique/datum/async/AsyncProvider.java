package com.cemique.datum.async;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.Provider;

import java.util.Set;

/**
 * Provider of async datums.
 *
 * Async datums are used to access {@link android.content.SharedPreferences}
 * in an asynchronous way (unlike {@link com.cemique.datum.sync.SyncProvider})
 * so that heavy tasks are processed in worker threads.
 */
@SuppressLint("ApplySharedPref")
public abstract class AsyncProvider extends Provider {

    public class IntegerDatum extends AsyncDatumWrapper<Integer> {
        public IntegerDatum(@NonNull String key, @Nullable Integer defaultValue) {
            super(new IntegerSource(), key, defaultValue);
        }
    }
    public class FloatDatum extends AsyncDatumWrapper<Float> {
        public FloatDatum(@NonNull String key, @Nullable Float defaultValue) {
            super(new FloatSource(), key, defaultValue);
        }
    }
    public class LongDatum extends AsyncDatumWrapper<Long> {
        public LongDatum(@NonNull String key, @Nullable Long defaultValue) {
            super(new LongSource(), key, defaultValue);
        }
    }
    public class BooleanDatum extends AsyncDatumWrapper<Boolean> {
        public BooleanDatum(@NonNull String key, @Nullable Boolean defaultValue) {
            super(new BooleanSource(), key, defaultValue);
        }
    }
    public class StringDatum extends AsyncDatumWrapper<String> {
        public StringDatum(@NonNull String key, @Nullable String defaultValue) {
            super(new StringSource(), key, defaultValue);
        }
    }
    public class StringSetDatum extends AsyncDatumWrapper<Set<String>> {
        public StringSetDatum(@NonNull String key, @Nullable Set<String> defaultValue) {
            super(new StringSetSource(), key, defaultValue);
        }
    }

}
