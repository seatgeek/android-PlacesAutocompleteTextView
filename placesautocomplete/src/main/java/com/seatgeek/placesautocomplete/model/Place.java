package com.seatgeek.placesautocomplete.model;

import java.util.List;

/**
 * A class representing a matching place returned from the Places Autocomplete API. For full details
 * on the fields in this class, see https://developers.google.com/places/documentation/autocomplete
 */
public final class Place {

    /**
     * Human readable description of the place returned from the api. e.g. "235 Park Ave South, New York, NY, USA"
     */
    public final String description;

    /**
     * A unique place id used in the maps apu. this can be used to query for further information about
     * the location from the places api. See https://developers.google.com/places/documentation/place-id
     * for more information about place_id
     */
    public final String place_id;

    /**
     * The lengths and offsets of the substrings the query matched in the description
     */
    public final List<MatchedSubstring> matched_substrings;

    /**
     * The strings and offsets of the components that make up the description.
     */
    public final List<DescriptionTerm> terms;

    /**
     * The types of place that this corresponds to in the places api. See individual PlaceType's for
     * thier descriptions
     */
    public final List<PlaceType> types;

    public Place(final String description, final String place_id, final List<MatchedSubstring> matched_substrings, final List<DescriptionTerm> terms, final List<PlaceType> types) {
        this.description = description;
        this.place_id = place_id;
        this.matched_substrings = matched_substrings;
        this.terms = terms;
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;

        Place place = (Place) o;

        if (description != null ? !description.equals(place.description) : place.description != null) return false;
        if (matched_substrings != null ? !matched_substrings.equals(place.matched_substrings) : place.matched_substrings != null)
            return false;
        if (place_id != null ? !place_id.equals(place.place_id) : place.place_id != null) return false;
        if (terms != null ? !terms.equals(place.terms) : place.terms != null) return false;
        if (types != null ? !types.equals(place.types) : place.types != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (place_id != null ? place_id.hashCode() : 0);
        result = 31 * result + (matched_substrings != null ? matched_substrings.hashCode() : 0);
        result = 31 * result + (terms != null ? terms.hashCode() : 0);
        result = 31 * result + (types != null ? types.hashCode() : 0);
        return result;
    }
}
