package com.seatgeek.placesautocomplete;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.seatgeek.placesautocomplete.network.PlacesHttpClient;
import com.seatgeek.placesautocomplete.network.PlacesHttpClientResolver;

public class PlacesApiBuilder {

    @Nullable
    private PlacesHttpClient apiClient;

    @Nullable
    private String googleApiKey;

    public PlacesApiBuilder setApiClient(@NonNull final PlacesHttpClient apiClient) {
        this.apiClient = apiClient;
        return this;
    }

    public PlacesApiBuilder setGoogleApiKey(@NonNull final String googleApiKey) {
        if (TextUtils.isEmpty(googleApiKey)) {
            throw new IllegalArgumentException("googleApiKey cannot be null or empty!");
        }

        this.googleApiKey = googleApiKey;
        return this;
    }

    @NonNull
    public PlacesApi build() {
        if (apiClient == null) {
            apiClient = PlacesHttpClientResolver.PLACES_HTTP_CLIENT;
        }

        if (googleApiKey == null) {
            throw new IllegalArgumentException("googleApiKey cannot be null when building " + PlacesApi.class.getSimpleName());
        }

        return new PlacesApi(apiClient, googleApiKey);
    }
}
