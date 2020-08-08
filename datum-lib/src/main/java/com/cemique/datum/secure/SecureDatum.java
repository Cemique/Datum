package com.cemique.datum.secure;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.Assertion;
import com.cemique.datum.DoneCallback;
import com.cemique.datum.GetCallback;
import com.cemique.datum.Provider;
import com.cemique.datum.async.AsyncInteraction;
import com.cemique.datum.secure.cipher.Cipher;
import com.cemique.datum.secure.cipher.DeterministicCipher;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Base class for different types of secure datums.
 * A {@link SecureDatum} works with {@link AsyncInteraction} in
 * order to do the resource consuming tasks like encrypt/decrypt
 * in worker threads.
 *
 * Secure datums are saved ciphered. For example a regular datum
 * is saved as '<string name="key">A string value</string>'
 * while the same value as a secure datum (depending on cipher
 * algorithm) is saved as '<string name="IuR3oe8EpQ1mae84UivqRQ=
 * =">WLeHKqi47DYP83kHu9Sb+Q==</string>'.
 */
abstract class SecureDatum<T> implements AsyncInteraction<T> {

    private SecureStringDatumWrapper stringDatum;
    private String key;
    private T defaultValue;
    private T value;

    SecureDatum(Provider.StringSource stringProvider, DeterministicCipher keyCipher, Cipher valueCipher
            , @NonNull String key, @Nullable T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        stringDatum = new SecureStringDatumWrapper(stringProvider, keyCipher, valueCipher, key);
    }

    /**
     * @param t Value to be saved as datum.
     * @return String conversion of the value.
     */
    @NonNull
    abstract String valueToString(@NonNull T t);

    /**
     * @param string String conversion of the value.
     * @return Original value that saved as datum.
     */
    @NonNull
    abstract T valueFromString(@NonNull String string);

    //

    @Override
    public final void getValue(GetCallback<T> onGetValue) {
        stringDatum.getValue(s -> {
            value = s == null ? defaultValue : valueFromString(s);
            onGetValue.onGetValue(value);
        });
    }

    @Override
    public final void setValue(@NonNull T t, @Nullable DoneCallback onSetValue) {
        Assertion.assertNonNull(t);

        stringDatum.setValue(valueToString(t), () -> {
            value = t;
            if (onSetValue != null)
                onSetValue.callback();
        });
    }

    @Override
    public final void clear(@Nullable DoneCallback onClear) {
        stringDatum.clear(() -> {
            if (onClear != null)
                onClear.callback();
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

    /**
     * Helper class for saving secure datums. All secure datums ready
     * to be saved are converted to {@link String} so that they can be
     * saved as a string datum. The string datum is encrypted before
     * being saved and decrypted before being loaded.
     */
    private static class SecureStringDatumWrapper implements AsyncInteraction<String> {

        private Provider.StringSource provider;
        private String key;
        private String encryptedKey;
        private String value;
        private final Object lock = "lock";

        private final DeterministicCipher keyCipher;
        private final Cipher valueCipher;

        private SecureStringDatumWrapper(Provider.StringSource provider, DeterministicCipher keyCipher, Cipher valueCipher, String key) {
            this.provider = provider;
            this.keyCipher = keyCipher;
            this.valueCipher = valueCipher;
            this.key = key;
        }

        @Override
        public void getValue(GetCallback<String> onGetValue) {
            Looper looper = Looper.myLooper();
            backgroundWork(() -> {
                synchronized (lock) {
                    String encryptedKey = getEncryptedDatumName();
                    if (!provider.contains(encryptedKey))
                        value = null;
                    else {
                        String encryptedValue = provider.getValue(encryptedKey, null);
                        if (encryptedValue == null)
                            value = null;
                        else {
                            String decryptedValue = valueCipher.decrypt(encryptedValue);
                            value = decryptedValue;
                        }
                    }
                    final String finalValue = value;
                    new Handler(looper != null ? looper : Looper.getMainLooper()).post(() -> onGetValue.onGetValue(finalValue));
                }
            });
        }

        @Override
        public void setValue(@NonNull String t, @Nullable DoneCallback onSetValue) {
            Looper looper = Looper.myLooper();
            backgroundWork(() -> {
                synchronized (lock) {
                    String encryptedKey = getEncryptedDatumName();
                    String encryptedValue;
                    encryptedValue = valueCipher.trimmedEncrypt(t);

                    provider.setValue(encryptedKey, encryptedValue);
                    value = t;
                    new Handler(looper != null ? looper : Looper.getMainLooper()).post(() -> {
                        if (onSetValue != null)
                            onSetValue.callback();
                    });
                }
            });

        }

        @Override
        public void clear(@Nullable DoneCallback onClear) {
            Looper looper = Looper.myLooper();
            backgroundWork(() -> {
                synchronized (lock) {
                    provider.clear();
                    new Handler(looper != null ? looper : Looper.getMainLooper()).post(() -> {
                        if (onClear != null)
                            onClear.callback();
                    });
                }
            });
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getDefaultValue() {
            return null;
        }


        //

        private Executor executor;

        private void backgroundWork(Runnable runnable) {
            if (executor == null)
                executor = Executors.newSingleThreadExecutor();
            executor.execute(runnable);
        }

        //

        String getEncryptedDatumName() {
            if (encryptedKey != null)
                return encryptedKey;
            return encryptedKey = keyCipher.trimmedEncrypt(key);
        }

    }

}