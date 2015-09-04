package com.seatgeek.placesautocomplete.json;

import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlacesAutocompleteResponse;
import com.seatgeek.placesautocomplete.model.PlacesDetailsResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface PlacesApiJsonParser {
    public PlacesAutocompleteResponse autocompleteFromStream(InputStream is) throws JsonParsingException;

    public PlacesDetailsResponse detailsFromStream(InputStream is) throws JsonParsingException;

    public List<Place> readHistoryJson(InputStream in) throws JsonParsingException;

    public void writeHistoryJson(OutputStream os, List<Place> places) throws JsonWritingException;
}
