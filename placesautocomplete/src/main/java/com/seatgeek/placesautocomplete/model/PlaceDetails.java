package com.seatgeek.placesautocomplete.model;

import java.util.Arrays;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceDetails)) return false;

        PlaceDetails that = (PlaceDetails) o;

        if (address_components != null ? !address_components.equals(that.address_components) : that.address_components != null) return false;
        if (formatted_address != null ? !formatted_address.equals(that.formatted_address) : that.formatted_address != null) return false;
        if (formatted_phone_number != null ? !formatted_phone_number.equals(that.formatted_phone_number) : that.formatted_phone_number != null) return false;
        if (international_phone_number != null ? !international_phone_number.equals(that.international_phone_number) : that.international_phone_number != null) return false;
        if (geometry != null ? !geometry.equals(that.geometry) : that.geometry != null) return false;
        if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (place_id != null ? !place_id.equals(that.place_id) : that.place_id != null) return false;
        if (opening_hours != null ? !opening_hours.equals(that.opening_hours) : that.opening_hours != null) return false;
        if (permanently_closed != that.permanently_closed) return false;
        if (photos != null ? !photos.equals(that.photos) : that.photos != null) return false;
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;
        if (alt_ids != null ? !alt_ids.equals(that.alt_ids) : that.alt_ids != null) return false;
        if (address_components != null ? !address_components.equals(that.address_components) : that.address_components != null) return false;
        if (price_level != that.price_level) return false;
        if (rating != rating) return false;
        if (reviews != null ? !reviews.equals(that.reviews) : that.reviews != null) return false;
        if (types != null ? !types.equals(that.types) : that.types != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (vicinity != null ? !vicinity.equals(that.vicinity) : that.vicinity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address_components != null ? address_components.hashCode() : 0;
        result = 31 * result + (formatted_address != null ? formatted_address.hashCode() : 0);
        result = 31 * result + (formatted_phone_number != null ? formatted_phone_number.hashCode() : 0);
        result = 31 * result + (international_phone_number != null ? international_phone_number.hashCode() : 0);
        result = 31 * result + (geometry != null ? geometry.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (place_id != null ? place_id.hashCode() : 0);
        result = 31 * result + (opening_hours != null ? opening_hours.hashCode() : 0);
        result = 31 * result + (permanently_closed ? 1 : 0);
        result = 31 * result + (photos != null ? photos.hashCode() : 0);
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        result = 31 * result + (alt_ids != null ? alt_ids.hashCode() : 0);
        result = 31 * result + price_level;
        long ratingBits = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (ratingBits ^ (ratingBits >>> 32));
        result = 31 * result + (reviews != null ? reviews.hashCode() : 0);
        result = 31 * result + (types != null ? types.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (vicinity != null ? vicinity.hashCode() : 0);
        return result;
    }
}
