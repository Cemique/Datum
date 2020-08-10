package com.cemique.datum;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cemique.datum.async.AsyncDatum;
import com.cemique.datum.async.AsyncProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AsyncProviderTest {

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
    public void asyncDatum() {
        for (int i = 0; i < 5; i++) {
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            SharedPreferences sharedPreferences = appContext.getSharedPreferences("async_datums_"+i, 0);
            AsyncProvider asyncProvider = new AsyncProvider() {
                @Override
                protected SharedPreferences getSharedPreferences() {
                    return sharedPreferences;
                }
            };

            sharedPreferences.edit().clear().commit();
            for (String key : defaultValues.keySet())
                testASyncDatum(asyncProvider, sharedPreferences, key);
        }
    }

    private void testASyncDatum(AsyncProvider asyncProvider, SharedPreferences sharedPreferences, String key) {

        Object initValue = defaultValues.get(key);
        AsyncDatum datum = null;

        if (initValue instanceof String)
            datum = asyncProvider.new StringDatum(key, (String) initValue);
        else if (initValue instanceof Integer)
            datum = asyncProvider.new IntegerDatum(key, (Integer) initValue);
        else if (initValue instanceof Float)
            datum = asyncProvider.new FloatDatum(key, (Float) initValue);
        else if (initValue instanceof Long)
            datum = asyncProvider.new LongDatum(key, (Long) initValue);
        else if (initValue instanceof Boolean)
            datum = asyncProvider.new BooleanDatum(key, (Boolean) initValue);

        assertNotNull(datum);

        datum.getValue(value -> {
            assertEquals("Key:" + key, value, initValue);
        });
        datum.setValue(newValues.get(key));
        datum.getValue(value -> {
            assertTrue(sharedPreferences.contains(key));
            assertEquals("Key:" + key, value, sharedPreferences.getAll().get(key));
            assertEquals("Key:" + key, value, newValues.get(key));
        });

    }


}
