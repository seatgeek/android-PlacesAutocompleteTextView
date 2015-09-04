package com.seatgeek.placesautocomplete.network;

import com.seatgeek.placesautocomplete.json.JsonParsingException;

import java.io.InputStream;

interface ResponseHandler<T> {
    T handleStreamResult(InputStream is) throws JsonParsingException;
}
