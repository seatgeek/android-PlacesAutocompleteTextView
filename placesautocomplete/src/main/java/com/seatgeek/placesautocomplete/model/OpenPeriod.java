package com.seatgeek.placesautocomplete.model;

public final class OpenPeriod {
    public final DateTimePair open;
    public final DateTimePair close;

    public OpenPeriod(final DateTimePair open, final DateTimePair close) {
        this.open = open;
        this.close = close;
    }
}
