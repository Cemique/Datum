package com.cemique.datum.sync;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.Provider;

import java.util.Set;

/**
 * Provider of sync datums.
 *
 * Sync datums are used to access {@link android.content.SharedPreferences}
 * in a synchronous way (unlike {@link com.cemique.datum.async.AsyncProvider}).
 */
@SuppressLint("ApplySharedPref")
public abstract class SyncProvider extends Provider {

    public class IntegerDatum extends SyncDatumWrapper<Integer> {
        public IntegerDatum(@NonNull String key, @Nullable Integer defaultValue) {
            super(new IntegerSource(), key, defaultValue);
        }
    }
    public class FloatDatum extends SyncDatumWrapper<Float> {
        public FloatDatum(@NonNull String key, @Nullable Float defaultValue) {
            super(new FloatSource(), key, defaultValue);
        }
    }
    public class LongDatum extends SyncDatumWrapper<Long> {
        public LongDatum(@NonNull String key, @Nullable Long defaultValue) {
            super(new LongSource(), key, defaultValue);
        }
    }
    public class BooleanDatum extends SyncDatumWrapper<Boolean> {
        public BooleanDatum(@NonNull String key, @Nullable Boolean defaultValue) {
            super(new BooleanSource(), key, defaultValue);
        }
    }
    public class StringDatum extends SyncDatumWrapper<String> {
        public StringDatum(@NonNull String key, @Nullable String defaultValue) {
            super(new StringSource(), key, defaultValue);
        }
    }
    public class StringSetDatum extends SyncDatumWrapper<Set<String>> {
        public StringSetDatum(@NonNull String key, @Nullable Set<String> defaultValue) {
            super(new StringSetSource(), key, defaultValue);
        }
    }

}
