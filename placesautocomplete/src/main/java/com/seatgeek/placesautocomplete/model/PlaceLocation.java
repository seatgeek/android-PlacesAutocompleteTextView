package com.seatgeek.placesautocomplete.model;

public final class PlaceLocation {
    public final double lat;
    public final double lng;

    public PlaceLocation(final double lat, final double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceLocation)) return false;

        PlaceLocation that = (PlaceLocation) o;

        if (lat != that.lat) return false;
        if (lng != that.lng) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long latBits = Double.doubleToLongBits(lat);
        long lngBits = Double.doubleToLongBits(lng);
        int result = (int) (latBits ^ (latBits >>> 32));
        result = 31 * result + (int) (lngBits ^ (lngBits >>> 32));;
        return result;
    }
}
