package com.cemique.datum.sync;

import androidx.annotation.NonNull;

import com.cemique.datum.Interaction;

/**
 * User interactions with synchronous datum.
 */
public interface SyncInteraction<T> extends Interaction<T> {

    T getValue();

    void setValue(@NonNull T t);

    void clear();

}
