package com.cemique.datum;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Super class for datum providers containing source classes
 * for each datum type (Integer, Boolean, ...)
 */
@SuppressLint("ApplySharedPref")
public abstract class Provider {

    /**
     * Its recommended to only call {@link android.content.Context#getSharedPreferences(String, int)}
     * inside the body of this method and not before, so it will be called by datum providers; this
     * will help async providers to handle this in a worker thread.
     *
     * @return {@link android.content.Context#getSharedPreferences(String, int)}
     */
    protected abstract SharedPreferences getSharedPreferences();

    /**
     * Base class for datum sources.
     */
    private abstract class Source<T> implements DatumSource<T> {
        @Override
        public boolean contains(String key) {
            return getSharedPreferences().contains(key);
        }

        @Override
        public void clear() {
            getSharedPreferences().edit().clear().commit();
        }
    }

    /**
     * To prevent {@link NullPointerException} when getting
     * value of primitive types.
     */
    private <T> T getValueInternal(boolean isPrimitive, String key, T defaultValue, OutputFunction<T> getValue) {
        if (isPrimitive) {
            if (!getSharedPreferences().contains(key))
                return defaultValue;
            else
                return getValue.apply();
        } else
            return getValue.apply();
    }

    public class IntegerSource extends Source<Integer> {
        @Override
        public Integer getValue(String key, Integer defaultValue) {
            return getValueInternal(true, key, defaultValue
                    , () -> getSharedPreferences().getInt(key, defaultValue));
        }

        @Override
        public void setValue(String key, Integer value) {
            getSharedPreferences().edit().putInt(key, value).commit();
        }
    }

    public class FloatSource extends Source<Float> {
        @Override
        public Float getValue(String key, Float defaultValue) {
            return getValueInternal(true, key, defaultValue
                    , () -> getSharedPreferences().getFloat(key, defaultValue));
        }

        @Override
        public void setValue(String key, Float value) {
            getSharedPreferences().edit().putFloat(key, value).commit();
        }
    }

    public class LongSource extends Source<Long> {
        @Override
        public Long getValue(String key, Long defaultValue) {
            return getValueInternal(true, key, defaultValue
                    , () -> getSharedPreferences().getLong(key, defaultValue));
        }

        @Override
        public void setValue(String key, Long value) {
            getSharedPreferences().edit().putLong(key, value).commit();
        }
    }

    public class BooleanSource extends Source<Boolean> {
        @Override
        public Boolean getValue(String key, Boolean defaultValue) {
            return getValueInternal(true, key, defaultValue
                    , () -> getSharedPreferences().getBoolean(key, defaultValue));
        }

        @Override
        public void setValue(String key, Boolean value) {
            getSharedPreferences().edit().putBoolean(key, value).commit();
        }
    }

    public class StringSource extends Source<String> {
        @Override
        public String getValue(String key, String defaultValue) {
            return getValueInternal(false, key, defaultValue
                    , () -> getSharedPreferences().getString(key, defaultValue));
        }

        @Override
        public void setValue(String key, String value) {
            getSharedPreferences().edit().putString(key, value).commit();
        }
    }

    public class StringSetSource extends Source<Set<String>> {
        @Override
        public Set<String> getValue(String key, Set<String> defaultValue) {
            return getValueInternal(false, key, defaultValue
                    , () -> getSharedPreferences().getStringSet(key, defaultValue));
        }

        @Override
        public void setValue(String key, Set<String> value) {
            getSharedPreferences().edit().putStringSet(key, value).commit();
        }
    }

}
