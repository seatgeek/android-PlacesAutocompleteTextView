package com.seatgeek.placesautocomplete;

import android.support.annotation.NonNull;

import com.seatgeek.placesautocomplete.model.Place;

/**
 * A listener for place selected events that fire when an item is selected from the autocomplete
 * popup
 */
public interface OnPlaceSelectedListener {

    /**
     * Called when a new Place has been selected from the autocomplete popup
     * @param place the selected Place, can be used to then fetch details about the place using
     *              {@link PlacesAutocompleteTextView#getDetailsFor(Place, DetailsCallback)}
     */
    void onPlaceSelected(@NonNull Place place);
}
