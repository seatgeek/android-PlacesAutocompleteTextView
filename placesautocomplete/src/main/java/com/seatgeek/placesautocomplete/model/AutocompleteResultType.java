package com.seatgeek.placesautocomplete.model;

/**
 * Corresponds to GoogleMapsPlacesAutoCompleteTextView attribute resultType
 */
public enum AutocompleteResultType {
    GEOCODE("geocode"),
    ADDRESS("address"),
    ESTABLISHMENT("establishment");

    private final String queryParam;

   AutocompleteResultType(final String queryParam) {
        this.queryParam = queryParam;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public static AutocompleteResultType fromEnum(int enumerated) {
        return AutocompleteResultType.values()[enumerated];
    }
}
