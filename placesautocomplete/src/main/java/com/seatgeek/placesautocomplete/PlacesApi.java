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
    private final PlacesHttpClient mHttpClient;

    @NonNull
    private final String mGoogleApiKey;

    @Nullable
    private Location mCurrentLocation;

    @Nullable
    private Long mRadiusM;

    private boolean mLocationBiasEnabled = true;

    public PlacesApi(@NonNull final PlacesHttpClient httpClient, @NonNull final String googleApiKey) {
        mHttpClient = httpClient;
        mGoogleApiKey = googleApiKey;
    }

    public boolean isLocationBiasEnabled() {
        return mLocationBiasEnabled;
    }

    public void setLocationBiasEnabled(boolean enabled) {
        mLocationBiasEnabled = enabled;
    }

    public Long getRadiusMeters() {
        return mRadiusM;
    }

    public void setRadiusMeters(final Long radiusM) {
        mRadiusM = radiusM;
    }

    @Nullable
    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(@Nullable final Location currentLocation) {
        mCurrentLocation = currentLocation;
    }

    public PlacesAutocompleteResponse autocomplete(final String input, final AutocompleteResultType type) throws IOException {
        final String finalInput = input == null ? "" : input;

        final AutocompleteResultType finalType = type == null ? DEFAULT_RESULT_TYPE : type;

        Uri.Builder uriBuilder = Uri.parse(PLACES_API_BASE)
                .buildUpon()
                .appendPath(PATH_AUTOCOMPLETE)
                .appendPath(PATH_JSON)
                .appendQueryParameter(PARAMETER_TYPE, finalType.getQueryParam())
                .appendQueryParameter(PARAMETER_KEY, mGoogleApiKey)
                .appendQueryParameter(PARAMETER_INPUT, finalInput);

        if (mLocationBiasEnabled && mCurrentLocation != null) {
            uriBuilder.appendQueryParameter(PARAMETER_LOCATION, LocationUtils.toLatLngString(mCurrentLocation));
        }

        if (mLocationBiasEnabled && mRadiusM != null) {
            uriBuilder.appendQueryParameter(PARAMETER_RADIUS, mRadiusM.toString());
        }

        if (!mLocationBiasEnabled) {
            uriBuilder.appendQueryParameter(PARAMETER_LOCATION, LocationUtils.toLatLngString(NO_BIAS_LOCATION));
            uriBuilder.appendQueryParameter(PARAMETER_RADIUS, NO_BIAS_RADIUS.toString());
        }

        return mHttpClient.executeAutocompleteRequest(uriBuilder.build());
    }

    public PlacesDetailsResponse details(final String placeId) throws IOException {
        Uri.Builder uriBuilder = Uri.parse(PLACES_API_BASE)
                .buildUpon()
                .appendPath(PATH_DETAILS)
                .appendPath(PATH_JSON)
                .appendQueryParameter(PARAMETER_KEY, mGoogleApiKey)
                .appendQueryParameter(PARAMETER_PLACE_ID, placeId);

        return mHttpClient.executeDetailsRequest(uriBuilder.build());
    }
}
