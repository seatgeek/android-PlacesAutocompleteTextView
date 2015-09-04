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
}
