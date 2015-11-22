package com.seatgeek.placesautocomplete.util;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public final class ResourceUtils {

    public static void closeResourceQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(ResourceUtils.class.getName(), e.getMessage(), e);
            }
        }
    }

    private ResourceUtils() {
    }
}
