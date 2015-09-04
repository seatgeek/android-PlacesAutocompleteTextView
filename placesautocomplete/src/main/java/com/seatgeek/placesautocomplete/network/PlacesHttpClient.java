package com.seatgeek.placesautocomplete.network;

import android.net.Uri;

import com.seatgeek.placesautocomplete.model.PlacesAutocompleteResponse;
import com.seatgeek.placesautocomplete.model.PlacesDetailsResponse;

import java.io.IOException;

public interface PlacesHttpClient {
    public PlacesAutocompleteResponse executeAutocompleteRequest(Uri uri) throws IOException;

    public PlacesDetailsResponse executeDetailsRequest(Uri uri) throws IOException;
}
