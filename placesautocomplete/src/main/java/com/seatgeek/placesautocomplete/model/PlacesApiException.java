package com.seatgeek.placesautocomplete.model;

import java.io.IOException;

public class PlacesApiException extends IOException {

    public PlacesApiException() {
    }

    public PlacesApiException(final String detailMessage) {
        super(detailMessage);
    }

    public PlacesApiException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PlacesApiException(final Throwable throwable) {
        super(throwable);
    }
}
