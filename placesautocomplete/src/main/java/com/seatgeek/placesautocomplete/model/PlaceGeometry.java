package com.seatgeek.placesautocomplete.model;

public final class PlaceGeometry {
    public final PlaceLocation location;

    public PlaceGeometry(final PlaceLocation location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof  PlaceGeometry)) return false;

        PlaceGeometry that = (PlaceGeometry) o;

        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }
}
