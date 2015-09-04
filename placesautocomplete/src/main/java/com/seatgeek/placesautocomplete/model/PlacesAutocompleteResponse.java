package com.seatgeek.placesautocomplete.model;

import java.util.List;

/**
 * Model representing the places api autocomplete response; complete with status codes and error messages
 */
public final class PlacesAutocompleteResponse extends PlacesApiResponse {

    /**
     * A list of predicted places from the api based on the given input
     */
    public final List<Place> predictions;

    public PlacesAutocompleteResponse(final Status status, final String error_message, final List<Place> predictions) {
        super(status, error_message);
        this.predictions = predictions;
    }
}
