package com.seatgeek.placesautocomplete.model;

import com.google.gson.annotations.SerializedName;

public enum Status {
    @SerializedName("OK")
    OK(true),
    @SerializedName("ZERO_RESULTS")
    ZERO_RESULTS(true),
    @SerializedName("OVER_QUERY_LIMIT")
    OVER_QUERY_LIMIT(false),
    @SerializedName("REQUEST_DENIED")
    REQUEST_DENIED(false),
    @SerializedName("INVALID_REQUEST")
    INVALID_REQUEST(false);

    private final boolean successful;

    Status(final boolean successfulResponse) {
        successful = successfulResponse;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
