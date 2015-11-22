package com.seatgeek.placesautocomplete.model;

public final class AlternativePlaceId {
    public final String place_id;
    public final PlaceScope scope;

    public AlternativePlaceId(final String place_id, final PlaceScope scope) {
        this.place_id = place_id;
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlternativePlaceId)) return false;

        AlternativePlaceId that = (AlternativePlaceId) o;

        if (place_id != null ? !place_id.equals(that.place_id) : that.place_id != null) return false;
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (place_id != null ? place_id.hashCode() : 0);
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        return result;
    }
}
