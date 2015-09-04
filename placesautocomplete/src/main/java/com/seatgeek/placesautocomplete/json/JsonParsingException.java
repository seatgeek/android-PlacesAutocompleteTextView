package com.seatgeek.placesautocomplete.json;

import java.io.IOException;

public class JsonParsingException extends IOException {

    public JsonParsingException() {
    }

    public JsonParsingException(final String detailMessage) {
        super(detailMessage);
    }

    public JsonParsingException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public JsonParsingException(final Throwable throwable) {
        super(throwable);
    }
}
