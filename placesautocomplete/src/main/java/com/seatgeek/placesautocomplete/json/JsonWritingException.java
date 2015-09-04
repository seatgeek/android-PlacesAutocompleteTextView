package com.seatgeek.placesautocomplete.json;

import java.io.IOException;

public class JsonWritingException extends IOException {

    public JsonWritingException() {
    }

    public JsonWritingException(final String detailMessage) {
        super(detailMessage);
    }

    public JsonWritingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonWritingException(final Throwable cause) {
        super(cause);
    }
}
