package com.seatgeek.placesautocomplete;

import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.PlacesAutocompleteResponse;
import com.seatgeek.placesautocomplete.model.PlacesDetailsResponse;
import com.seatgeek.placesautocomplete.network.PlacesHttpClient;
import com.seatgeek.placesautocomplete.util.LocationUtils;

import java.io.IOException;

public class PlacesApi {
    public static final AutocompleteResultType DEFAULT_RESULT_TYPE = AutocompleteResultType.ADDRESS;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String PATH_AUTOCOMPLETE = "autocomplete";
    private static final String PATH_DETAILS = "details";
    private static final String PATH_JSON = "json";
    private static final String PARAMETER_LOCATION = "location";
    private static final String PARAMETER_RADIUS = "radius";
    private static final String PARAMETER_INPUT = "input";
    private static final String PARAMETER_KEY = "key";
    private static final String PARAMETER_TYPE = "types";
    private static final String PARAMETER_PLACE_ID = "placeid";

    private static final Long NO_BIAS_RADIUS = 20000000L;
    private static final Location NO_BIAS_LOCATION;

    static {
        NO_BIAS_LOCATION = new Location("none");
        NO_BIAS_LOCATION.setLatitude(0.0d);
        NO_BIAS_LOCATION.setLongitude(0.0d);
    }

    @NonNull
    private final PlacesHttpClient httpClient;

    @NonNull
    private final String googleApiKey;

    @Nullable
    private Location currentLocation;

    @Nullable
    private Long radiusM;

    private boolean locationBiasEnabled = true;

    public PlacesApi(@NonNull final PlacesHttpClient httpClient, @NonNull final String googleApiKey) {
        this.httpClient = httpClient;
        this.googleApiKey = googleApiKey;
    }

    public boolean isLocationBiasEnabled() {
        return locationBiasEnabled;
    }

    public void setLocationBiasEnabled(boolean enabled) {
        locationBiasEnabled = enabled;
    }

    public Long getRadiusMeters() {
        return radiusM;
    }

    public void setRadiusMeters(final Long radiusM) {
        this.radiusM = radiusM;
    }

    @Nullable
    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(@Nullable final Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public PlacesAutocompleteResponse autocomplete(final String input, final AutocompleteResultType type) throws IOException {
        final String finalInput = input == null ? "" : input;

        final AutocompleteResultType finalType = type == null ? DEFAULT_RESULT_TYPE : type;

        Uri.Builder uriBuilder = Uri.parse(PLACES_API_BASE)
                .buildUpon()
                .appendPath(PATH_AUTOCOMPLETE)
                .appendPath(PATH_JSON)
                .appendQueryParameter(PARAMETER_TYPE, finalType.getQueryParam())
                .appendQueryParameter(PARAMETER_KEY, googleApiKey)
                .appendQueryParameter(PARAMETER_INPUT, finalInput);

        if (locationBiasEnabled && currentLocation != null) {
            uriBuilder.appendQueryParameter(PARAMETER_LOCATION, LocationUtils.toLatLngString(currentLocation));
        }

        if (locationBiasEnabled && radiusM != null) {
            uriBuilder.appendQueryParameter(PARAMETER_RADIUS, radiusM.toString());
        }

        if (!locationBiasEnabled) {
            uriBuilder.appendQueryParameter(PARAMETER_LOCATION, LocationUtils.toLatLngString(NO_BIAS_LOCATION));
            uriBuilder.appendQueryParameter(PARAMETER_RADIUS, NO_BIAS_RADIUS.toString());
        }

        return httpClient.executeAutocompleteRequest(uriBuilder.build());
    }

    public PlacesDetailsResponse details(final String placeId) throws IOException {
        Uri.Builder uriBuilder = Uri.parse(PLACES_API_BASE)
                .buildUpon()
                .appendPath(PATH_DETAILS)
                .appendPath(PATH_JSON)
                .appendQueryParameter(PARAMETER_KEY, googleApiKey)
                .appendQueryParameter(PARAMETER_PLACE_ID, placeId);

        return httpClient.executeDetailsRequest(uriBuilder.build());
    }
}
