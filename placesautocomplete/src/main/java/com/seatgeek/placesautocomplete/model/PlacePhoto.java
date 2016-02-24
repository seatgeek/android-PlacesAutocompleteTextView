package com.seatgeek.placesautocomplete.model;

public final class PlacePhoto {
    public final int height;
    public final int width;
    public final String photo_reference;

    public PlacePhoto(final int height, final int width, final String photo_reference) {
        this.height = height;
        this.width = width;
        this.photo_reference = photo_reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlacePhoto)) return false;

        PlacePhoto that = (PlacePhoto) o;

        if (height != that.height) return false;
        if (width != that.width) return false;
        if (photo_reference != null ? !photo_reference.equals(that.photo_reference) : that.photo_reference != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = height;
        result = 31 * result + width;
        result = 31 * result + (photo_reference != null ? photo_reference.hashCode() : 0);
        return result;
    }
}
