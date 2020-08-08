package com.cemique.datum.sync;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.Assertion;
import com.cemique.datum.DatumSource;

/**
 * Base class for different types of sync datums.
 */
class SyncDatum<T> implements SyncInteraction<T> {

    private DatumSource<T> datumSource;
    private String key;
    private T defaultValue;
    private T value;

    SyncDatum(DatumSource<T> datumSource, @NonNull String key, @Nullable T defaultValue) {
        this.datumSource = datumSource;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public final T getValue() {
        if(value == null)
            value = datumSource.getValue(key, defaultValue);
        return value;
    }

    @Override
    public final void setValue(@NonNull T t) {
        Assertion.assertNonNull(t);

        datumSource.setValue(key, t);
        value = t;
    }

    @Override
    public final void clear() {
        datumSource.clear();
        value = defaultValue;
    }

    @Override
    public final String getKey() {
        return key;
    }

    @Override
    public final T getDefaultValue() {
        return defaultValue;
    }

}
