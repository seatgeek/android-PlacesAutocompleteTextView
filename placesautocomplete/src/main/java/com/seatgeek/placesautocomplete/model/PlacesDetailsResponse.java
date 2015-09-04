package com.seatgeek.placesautocomplete.model;

public final class PlacesDetailsResponse extends PlacesApiResponse {

    public final PlaceDetails result;

    public PlacesDetailsResponse(final Status status, final String error_message, final PlaceDetails result) {
        super(status, error_message);
        this.result = result;
    }
}
