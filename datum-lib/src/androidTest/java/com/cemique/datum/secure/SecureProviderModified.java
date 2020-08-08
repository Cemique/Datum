package com.cemique.datum.secure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cemique.datum.secure.cipher.Cipher;
import com.cemique.datum.secure.cipher.DeterministicCipher;

import java.util.Set;

/**
 * Some modifications added to {@link SecureProvider}
 * in order to provide some test and debug functionality.
 */
public abstract class SecureProviderModified extends SecureProvider {

    public SecureProviderModified() {
    }

    public SecureProviderModified(DeterministicCipher keyCipher, Cipher valueCipher) {
        super(keyCipher, valueCipher);
    }


    /**
     * Provided only for test and debug. Blocks ui thread.
     *
     * @param key The key to be ciphered.
     * @return Ciphered key.
     */
    public String encryptKey(String key) {
        return getKeyCipher().trimmedEncrypt(key);
    }

    /**
     * Provided only for test and debug. Blocks ui thread.
     *
     * @param encryptedValue The value to be deciphered.
     * @return Deciphered value.
     */
    public String decryptValue(String encryptedValue){
        return getValueCipher().decrypt(encryptedValue);
    }



    public class BooleanDatumModified extends BooleanDatum {
        public BooleanDatumModified(@NonNull String key, @Nullable Boolean defaultValue) {
            super(key, defaultValue);
        }

        @NonNull
        @Override
        public Boolean valueFromString(@NonNull String string) {
            return super.valueFromString(string);
        }

        @NonNull
        @Override
        public String valueToString(@NonNull Boolean aBoolean) {
            return super.valueToString(aBoolean);
        }
    }

    public class FloatDatumModified extends FloatDatum {
        public FloatDatumModified(@NonNull String key, @Nullable Float defaultValue) {
            super(key, defaultValue);
        }

        @NonNull
        @Override
        public Float valueFromString(@NonNull String string) {
            return super.valueFromString(string);
        }

        @NonNull
        @Override
        public String valueToString(@NonNull Float aFloat) {
            return super.valueToString(aFloat);
        }
    }

    public class IntegerDatumModified extends IntegerDatum {
        public IntegerDatumModified(@NonNull String key, @Nullable Integer defaultValue) {
            super(key, defaultValue);
        }

        @NonNull
        @Override
        public Integer valueFromString(@NonNull String string) {
            return super.valueFromString(string);
        }

        @NonNull
        @Override
        public String valueToString(@NonNull Integer integer) {
            return super.valueToString(integer);
        }
    }

    public class LongDatumModified extends LongDatum {
        public LongDatumModified(@NonNull String key, @Nullable Long defaultValue) {
            super(key, defaultValue);
        }

        @NonNull
        @Override
        public Long valueFromString(@NonNull String string) {
            return super.valueFromString(string);
        }

        @NonNull
        @Override
        public String valueToString(@NonNull Long aLong) {
            return super.valueToString(aLong);
        }
    }

    public class StringDatumModified extends StringDatum {
        public StringDatumModified(@NonNull String key, @Nullable String defaultValue) {
            super(key, defaultValue);
        }

        @NonNull
        @Override
        public String valueFromString(@NonNull String string) {
            return super.valueFromString(string);
        }

        @NonNull
        @Override
        public String valueToString(@NonNull String s) {
            return super.valueToString(s);
        }
    }

    public class StringSetDatumModified extends StringSetDatum {
        public StringSetDatumModified(@NonNull String key, @Nullable Set<String> defaultValue) {
            super(key, defaultValue);
        }

        @NonNull
        @Override
        public Set<String> valueFromString(@NonNull String string) {
            return super.valueFromString(string);
        }

        @NonNull
        @Override
        public String valueToString(@NonNull Set<String> strings) {
            return super.valueToString(strings);
        }
    }

}
