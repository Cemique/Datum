package com.cemique.datum.secure;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.Provider;
import com.cemique.datum.secure.cipher.Cipher;
import com.cemique.datum.secure.cipher.DeterministicCipher;
import com.cemique.datum.secure.cipher.DefaultCipher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Set;

/**
 * Provider of secure datums.
 *
 * This class uses an instance of
 * {@link com.cemique.datum.Provider} to
 * save/load a secure datum in a
 * {@link com.cemique.datum.Provider.StringSource}.
 */
public abstract class SecureProvider {

    private final DeterministicCipher keyCipher;
    private final Cipher valueCipher;

    private final Provider provider = new Provider() {
        @Override
        protected SharedPreferences getSharedPreferences() {
            return SecureProvider.this.getSharedPreferences();
        }
    };


    /**
     * Not recommended for security!
     * Uses an instance of {@link DefaultCipher} to
     * cipher key-value of datums.
     */
    protected SecureProvider() {
        DefaultCipher defaultCipher = new DefaultCipher.Builder().build();
        this.valueCipher = this.keyCipher = defaultCipher;
    }

    /**
     * @param keyCipher Used to cipher datum keys (deterministic).
     * @param valueCipher Used to cipher datum values.
     */
    protected SecureProvider(DeterministicCipher keyCipher, Cipher valueCipher) {
        this.keyCipher = keyCipher;
        this.valueCipher = valueCipher;
    }

    /**
     * Its recommended to only call {@link android.content.Context#getSharedPreferences(String, int)}
     * inside the body of this method and not before, so it will be called by datum providers; this
     * will help async providers to handle this in a worker thread.
     *
     * @return {@link android.content.Context#getSharedPreferences(String, int)}
     */
    protected abstract SharedPreferences getSharedPreferences();


    public DeterministicCipher getKeyCipher() {
        return keyCipher;
    }

    public Cipher getValueCipher() {
        return valueCipher;
    }


    public class BooleanDatum extends SecureDatum<Boolean> {

        public BooleanDatum(@NonNull String key, @Nullable Boolean defaultValue) {
            super(provider.new StringSource(), keyCipher, valueCipher, key, defaultValue);
        }

        @Override
        @NonNull
        Boolean valueFromString(@NonNull String string) {
            return Boolean.valueOf(string);
        }

        @Override
        @NonNull
        String valueToString(@NonNull Boolean aBoolean) {
            return aBoolean.toString();
        }

    }

    public class FloatDatum extends SecureDatum<Float> {

        public FloatDatum(@NonNull String key, @Nullable Float defaultValue) {
            super(provider.new StringSource(), keyCipher, valueCipher, key, defaultValue);
        }

        @Override
        @NonNull
        Float valueFromString(@NonNull String string) {
            return Float.valueOf(string);
        }

        @Override
        @NonNull
        String valueToString(@NonNull Float aFloat) {
            return aFloat.toString();
        }

    }

    public class IntegerDatum extends SecureDatum<Integer> {

        public IntegerDatum(@NonNull String key, @Nullable Integer defaultValue) {
            super(provider.new StringSource(), keyCipher, valueCipher, key, defaultValue);
        }

        @Override
        @NonNull
        Integer valueFromString(@NonNull String string) {
            return Integer.valueOf(string);
        }

        @Override
        @NonNull
        String valueToString(@NonNull Integer integer) {
            return integer.toString();
        }

    }

    public class LongDatum extends SecureDatum<Long> {

        public LongDatum(@NonNull String key, @Nullable Long defaultValue) {
            super(provider.new StringSource(), keyCipher, valueCipher, key, defaultValue);
        }

        @Override
        @NonNull
        Long valueFromString(@NonNull String string) {
            return Long.valueOf(string);
        }

        @Override
        @NonNull
        String valueToString(@NonNull Long aLong) {
            return aLong.toString();
        }

    }

    public class StringDatum extends SecureDatum<String> {

        public StringDatum(@NonNull String key, @Nullable String defaultValue) {
            super(provider.new StringSource(), keyCipher, valueCipher, key, defaultValue);
        }

        @Override
        @NonNull
        String valueFromString(@NonNull String string) {
            return string;
        }

        @Override
        @NonNull
        String valueToString(@NonNull String s) {
            return s;
        }

    }

    public class StringSetDatum extends SecureDatum<Set<String>> {

        public StringSetDatum(@NonNull String key, @Nullable Set<String> defaultValue) {
            super(provider.new StringSource(), keyCipher, valueCipher, key, defaultValue);
        }

        @Override
        @NonNull
        Set<String> valueFromString(@NonNull String string) {
            return new Gson().fromJson(string, new TypeToken<Set<String>>() {
            }.getType());
        }

        @Override
        @NonNull
        String valueToString(@NonNull Set<String> strings) {
            return new Gson().toJson(strings, new TypeToken<Set<String>>() {
            }.getType());
        }

    }


}
