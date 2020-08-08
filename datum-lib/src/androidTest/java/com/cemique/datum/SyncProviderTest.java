package com.cemique.datum;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cemique.datum.sync.SyncInteraction;
import com.cemique.datum.sync.SyncProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SyncProviderTest {

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
    public void syncDatum() {
        for (int i = 0; i < 5; i++) {
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            SharedPreferences sharedPreferences = appContext.getSharedPreferences("sync_datums_"+i, 0);
            SyncProvider datumProvider = new SyncProvider() {
                @Override
                protected SharedPreferences getSharedPreferences() {
                    return sharedPreferences;
                }
            };

            sharedPreferences.edit().clear().commit();
            for (String key : defaultValues.keySet())
                testSyncDatum(datumProvider,sharedPreferences,key);
        }
    }

    private void testSyncDatum(SyncProvider syncProvider, SharedPreferences sharedPreferences, String key) {

        Object initValue = defaultValues.get(key);
        SyncInteraction datum = null;

        if (initValue instanceof String)
            datum = syncProvider.new StringDatum(key, (String) initValue);
        else if (initValue instanceof Integer)
            datum = syncProvider.new IntegerDatum(key, (Integer) initValue);
        else if (initValue instanceof Float)
            datum = syncProvider.new FloatDatum(key, (Float) initValue);
        else if (initValue instanceof Long)
            datum = syncProvider.new LongDatum(key, (Long) initValue);
        else if (initValue instanceof Boolean)
            datum = syncProvider.new BooleanDatum(key, (Boolean) initValue);

        assertNotNull(datum);
        assertEquals(datum.getValue(), defaultValues.get(key));

        datum.setValue(newValues.get(key));

        assertTrue(sharedPreferences.contains(key));
        assertEquals(datum.getValue(), sharedPreferences.getAll().get(key));
        assertEquals(datum.getValue(), newValues.get(key));
    }

}
