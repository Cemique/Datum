package com.cemique.datum;

import java.util.Set;

/**
 * Interactions with {@link android.content.SharedPreferences} class
 * to fetch/update data.
 * @param <T>
 */
public interface DatumSource<T> {

    /**
     * Depending on type it is same as Either:
     * {@link android.content.SharedPreferences#getString(String, String)}
     * , {@link android.content.SharedPreferences#getStringSet(String, Set)}
     * , {@link android.content.SharedPreferences#getInt(String, int)}
     * , {@link android.content.SharedPreferences#getLong(String, long)}
     * , {@link android.content.SharedPreferences#getFloat(String, float)}
     * or {@link android.content.SharedPreferences#getBoolean(String, boolean)}
     */
    T getValue(String key, T defaultValue);

    /**
     * Depending on type it is same as Either:
     * {@link android.content.SharedPreferences.Editor#putString(String, String)}
     * , {@link android.content.SharedPreferences.Editor#putStringSet(String, Set)}
     * , {@link android.content.SharedPreferences.Editor#putInt(String, int)}
     * , {@link android.content.SharedPreferences.Editor#putLong(String, long)}
     * , {@link android.content.SharedPreferences.Editor#putFloat(String, float)}
     * or {@link android.content.SharedPreferences.Editor#putBoolean(String, boolean)}
     */
    void setValue(String key, T value);

    /**
     * Same as {@link android.content.SharedPreferences#contains(String)}
     */
    boolean contains(String key);

    /**
     * Same as {@link android.content.SharedPreferences.Editor#remove(String)}
     */
    void clear();

}
