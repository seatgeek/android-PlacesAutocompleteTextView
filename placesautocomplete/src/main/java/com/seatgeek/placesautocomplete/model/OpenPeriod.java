package com.seatgeek.placesautocomplete.model;

public final class OpenPeriod {
    public final DateTimePair open;
    public final DateTimePair close;

    public OpenPeriod(final DateTimePair open, final DateTimePair close) {
        this.open = open;
        this.close = close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenPeriod)) return false;

        OpenPeriod that = (OpenPeriod) o;

        if (open != null ? !open.equals(that.open) : that.open != null) return false;
        if (close != null ? !close.equals(that.close) : that.close != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = open != null ? open.hashCode() : 0;
        result = 31 * result + (close != null ? close.hashCode() : 0);
        return result;
    }
}
