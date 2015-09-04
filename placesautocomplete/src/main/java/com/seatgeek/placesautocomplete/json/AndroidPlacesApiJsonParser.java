package com.seatgeek.placesautocomplete.json;

import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlacesAutocompleteResponse;
import com.seatgeek.placesautocomplete.model.PlacesDetailsResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

class AndroidPlacesApiJsonParser implements PlacesApiJsonParser {

    @Override
    public PlacesAutocompleteResponse autocompleteFromStream(final InputStream is) {
        throw new UnsupportedOperationException("Not yet implemented, please have Gson on your classpath");
    }

    @Override
    public PlacesDetailsResponse detailsFromStream(final InputStream is) throws JsonParsingException {
        throw new UnsupportedOperationException("Not yet implemented, please have Gson on your classpath");
    }

    @Override
    public List<Place> readHistoryJson(final InputStream in) throws JsonParsingException {
        throw new UnsupportedOperationException("Not yet implemented, please have Gson on your classpath");
    }

    @Override
    public void writeHistoryJson(final OutputStream os, final List<Place> places) throws JsonWritingException {
        throw new UnsupportedOperationException("Not yet implemented, please have Gson on your classpath");
    }
}
