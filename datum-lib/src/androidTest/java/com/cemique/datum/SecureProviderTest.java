package com.cemique.datum;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.arch.core.util.Function;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cemique.datum.async.AsyncInteraction;
import com.cemique.datum.secure.SecureProviderModified;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This test may run concurrently with other tests,
 * because it contains asynchronous processes. So
 * take care of resource coincidences when running
 * all test at the same time.
 * For instance if {@link SharedPreferences} used in
 * this test be same as the one used in
 * {@link SyncProviderTest} then in the middle of
 * async calls here the {@link SharedPreferences}
 * may be cleared and it results failure; so we
 * are not using the same {@link SharedPreferences}
 * for all tests.
 */
@RunWith(AndroidJUnit4.class)
public class SecureProviderTest {

    private final Map<String, Object> defaultValues = new HashMap<>();

    {
        defaultValues.put("stringDatumKey", "stringDatumDefault");
        defaultValues.put("integerDatumKey", 463);
        defaultValues.put("booleanDatumKey", true);
        defaultValues.put("longDatumKey", 159L);
        defaultValues.put("floatDatumKey", 1603.2f);
    }

    private final Map<String, Object> newValues = new HashMap<>();

    {
        newValues.put("stringDatumKey", "stringDatumAnother");
        newValues.put("integerDatumKey", -463);
        newValues.put("booleanDatumKey", false);
        newValues.put("longDatumKey", -159L);
        newValues.put("floatDatumKey", -1603.2f);
    }

    @Test
    public void secureDatum() {
        for (int i = 0; i < 5; i++) {
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            SharedPreferences sharedPreferences = appContext.getSharedPreferences("secure_datums_"+i, 0);
            SecureProviderModified datumProvider = new SecureProviderModified() {
                @Override
                protected SharedPreferences getSharedPreferences() {
                    return sharedPreferences;
                }
            };

            sharedPreferences.edit().clear().commit();
            for (String key : defaultValues.keySet())
                testSecureDatum(datumProvider, sharedPreferences, key);

        }
    }

    private void testSecureDatum(SecureProviderModified secureProvider, SharedPreferences sharedPreferences, String key) {

        Object initValue = defaultValues.get(key);
        AsyncInteraction datum = null;
        Function<String, Object> datum_stringToValue;

        if (initValue instanceof String) {
            SecureProviderModified.StringDatumModified m
                    =secureProvider.new StringDatumModified(key, (String) initValue);
            datum_stringToValue = m::valueFromString;
            datum = m;
        } else if (initValue instanceof Integer) {
            SecureProviderModified.IntegerDatumModified m
                    =secureProvider.new IntegerDatumModified(key, (Integer) initValue);
            datum_stringToValue = m::valueFromString;
            datum = m;
        } else if (initValue instanceof Float) {
            SecureProviderModified.FloatDatumModified m
                    =secureProvider.new FloatDatumModified(key, (Float) initValue);
            datum_stringToValue = m::valueFromString;
            datum = m;
        } else if (initValue instanceof Long) {
            SecureProviderModified.LongDatumModified m
                    =secureProvider.new LongDatumModified(key, (Long) initValue);
            datum_stringToValue = m::valueFromString;
            datum = m;
        } else if (initValue instanceof Boolean) {
            SecureProviderModified.BooleanDatumModified m
                    =secureProvider.new BooleanDatumModified(key, (Boolean) initValue);
            datum_stringToValue = m::valueFromString;
            datum = m;
        }else//bypass ide error
            datum_stringToValue = null;

        assertNotNull(datum);

        datum.getValue(value -> {
            assertEquals("Key:" + key, value, initValue);
        });
        datum.setValue(newValues.get(key));
        datum.getValue(value -> {
            AsyncInteraction datum2 = null;
            if (initValue instanceof String)
                datum2 = secureProvider.new StringDatumModified(key, (String) initValue);
            else if (initValue instanceof Integer)
                datum2 = secureProvider.new IntegerDatumModified(key, (Integer) initValue);
            else if (initValue instanceof Float)
                datum2 = secureProvider.new FloatDatumModified(key, (Float) initValue);
            else if (initValue instanceof Long)
                datum2 = secureProvider.new LongDatumModified(key, (Long) initValue);
            else if (initValue instanceof Boolean)
                datum2 = secureProvider.new BooleanDatumModified(key, (Boolean) initValue);

            datum2.getValue(value2 -> {
                assertEquals("Key:" + key, value2, newValues.get(key));
            });

            assertTrue(sharedPreferences.contains(secureProvider.encryptKey(key)));

            assertEquals("Key:" + key
                    , value
                    , datum_stringToValue.apply(secureProvider.decryptValue(sharedPreferences.getString(secureProvider.encryptKey(key), null))));
            assertEquals("Key:" + key, value, newValues.get(key));

        });

    }

}
