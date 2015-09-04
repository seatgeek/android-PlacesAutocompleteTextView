package com.seatgeek.placesautocomplete.model;

public class PlacesApiResponse {

    /**
     * API response status. Can and should be used to see if a request was successful
     */
    public final Status status;

    /**
     * If !status.isSuccessful(), should return an en error message about what went wrong
     */
    public final String error_message;

    public PlacesApiResponse(final Status status, final String error_message) {
        this.status = status;
        this.error_message = error_message;
    }
}
