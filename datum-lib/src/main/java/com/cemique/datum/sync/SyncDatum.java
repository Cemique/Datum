package com.cemique.datum.sync;

import androidx.annotation.NonNull;

import com.cemique.datum.Datum;

/**
 * User interactions with synchronous datum.
 */
public interface SyncDatum<T> extends Datum<T> {

    T getValue();

    void setValue(@NonNull T t);

    void clear();

}
