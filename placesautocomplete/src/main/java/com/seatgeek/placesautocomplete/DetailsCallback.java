package com.seatgeek.placesautocomplete;

import com.seatgeek.placesautocomplete.model.PlaceDetails;

public interface DetailsCallback {
    public void onSuccess(PlaceDetails details);
    public void onFailure(Throwable failure);
}
