package com.seatgeek.placesautocomplete.model;

public final class AlternativePlaceId {
    public final String place_id;
    public final PlaceScope scope;

    public AlternativePlaceId(final String place_id, final PlaceScope scope) {
        this.place_id = place_id;
        this.scope = scope;
    }
}
