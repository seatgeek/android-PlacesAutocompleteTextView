package com.seatgeek.placesautocomplete;

import com.seatgeek.placesautocomplete.model.PlaceDetails;

public interface DetailsCallback {
    void onSuccess(PlaceDetails details);
    void onFailure(Throwable failure);
}
