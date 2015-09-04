package com.seatgeek.placesautocomplete;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.seatgeek.placesautocomplete.network.PlacesHttpClient;
import com.seatgeek.placesautocomplete.network.PlacesHttpClientResolver;

public class PlacesApiBuilder {

    @Nullable
    private PlacesHttpClient mApiClient;

    @Nullable
    private String mGoogleApiKey;

    public PlacesApiBuilder setApiClient(@NonNull final PlacesHttpClient apiClient) {
        mApiClient = apiClient;
        return this;
    }

    public PlacesApiBuilder setGoogleApiKey(@NonNull final String googleApiKey) {
        if (TextUtils.isEmpty(googleApiKey)) {
            throw new IllegalArgumentException("googleApiKey cannot be null or empty!");
        }

        mGoogleApiKey = googleApiKey;
        return this;
    }

    @NonNull
    public PlacesApi build() {
        if (mApiClient == null) {
            mApiClient = PlacesHttpClientResolver.PLACES_HTTP_CLIENT;
        }

        if (mGoogleApiKey == null) {
            throw new IllegalArgumentException("googleApiKey cannot be null when building " + PlacesApi.class.getSimpleName());
        }

        return new PlacesApi(mApiClient, mGoogleApiKey);
    }
}
