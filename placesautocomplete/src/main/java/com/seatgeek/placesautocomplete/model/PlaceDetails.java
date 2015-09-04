package com.seatgeek.placesautocomplete.model;

import java.util.List;

/**
 * see {@link https://developers.google.com/places/documentation/details#PlaceDetailsResults} for
 * a breakdown of all the values in this object
 */
public final class PlaceDetails {

    public final List<AddressComponent> address_components;

    public final String formatted_address;

    public final String formatted_phone_number;

    public final String international_phone_number;

    public final PlaceGeometry geometry;

    public final String icon;

    public final String name;

    public final String place_id;

    public final OpenHours opening_hours;

    public final boolean permanently_closed;

    public final List<PlacePhoto> photos;

    public final PlaceScope scope;

    public final List<AlternativePlaceId> alt_ids;

    public final int price_level;

    public final double rating;

    public final List<PlaceReview> reviews;

    /**
     * see {@link https://developers.google.com/places/documentation/supported_types} for supported types
     */
    public final List<String> types;

    public final String url;

    public final String vicinity;

    public PlaceDetails(final List<AddressComponent> address_components, 
                        final String formatted_address, 
                        final String formatted_phone_number, 
                        final String international_phone_number, 
                        final PlaceGeometry geometry, 
                        final String icon, 
                        final String name, 
                        final String place_id, 
                        final OpenHours opening_hours,
                        final boolean permanently_closed,
                        final List<PlacePhoto> photos,
                        final PlaceScope scope,
                        final List<AlternativePlaceId> alt_ids,
                        final int price_level,
                        final double rating,
                        final List<PlaceReview> reviews,
                        final List<String> types,
                        final String url,
                        final String vicinity) {
        this.address_components = address_components;
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
        this.international_phone_number = international_phone_number;
        this.geometry = geometry;
        this.icon = icon;
        this.name = name;
        this.place_id = place_id;
        this.opening_hours = opening_hours;
        this.permanently_closed = permanently_closed;
        this.photos = photos;
        this.scope = scope;
        this.alt_ids = alt_ids;
        this.price_level = price_level;
        this.rating = rating;
        this.reviews = reviews;
        this.types = types;
        this.url = url;
        this.vicinity = vicinity;
    }
}
