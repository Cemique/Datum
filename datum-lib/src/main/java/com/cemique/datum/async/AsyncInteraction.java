package com.cemique.datum.async;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.DoneCallback;
import com.cemique.datum.GetCallback;
import com.cemique.datum.Interaction;

/**
 * User interactions with asynchronous datum.
 */
public interface AsyncInteraction<T> extends Interaction<T> {

    void getValue(GetCallback<T> onGetValue);

    default void setValue(@NonNull T t){
        setValue(t, null);
    }

    void setValue(@NonNull T t, @Nullable DoneCallback onSetValue);

    void clear(@Nullable DoneCallback onClear);

}
