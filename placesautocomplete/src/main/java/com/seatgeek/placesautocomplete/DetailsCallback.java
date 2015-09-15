package com.seatgeek.placesautocomplete;

import com.seatgeek.placesautocomplete.model.PlaceDetails;

/**
 * A callback used when fetching the PlaceDetails of a Place
 */
public interface DetailsCallback {

    /**
     * Success callback when a PlaceDetails is fetched for the given Place
     */
    void onSuccess(PlaceDetails details);

    /**
     * Failure callback when a PlaceDetails was failed to be fetched or parsed from the Places API
     */
    void onFailure(Throwable failure);
}
