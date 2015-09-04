package com.seatgeek.placesautocomplete.model;

/**
 * Corresponds to GoogleMapsPlacesAutoCompleteTextView attribute resultType
 */
public enum AutocompleteResultType {
    GEOCODE("geocode"),
    ADDRESS("address"),
    ESTABLISHMENT("establishment");

    private final String mQueryParam;

   AutocompleteResultType(final String queryParam) {
        mQueryParam = queryParam;
    }

    public String getQueryParam() {
        return mQueryParam;
    }

    public static AutocompleteResultType fromEnum(int enumerated) {
        return AutocompleteResultType.values()[enumerated];
    }
}
