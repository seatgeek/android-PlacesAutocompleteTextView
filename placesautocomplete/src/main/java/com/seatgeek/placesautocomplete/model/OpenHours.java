package com.seatgeek.placesautocomplete.model;

import java.util.List;

public final class OpenHours {
    public final boolean open_now;
    public final List<OpenPeriod> periods;

    public OpenHours(final boolean open_now, final List<OpenPeriod> periods) {
        this.open_now = open_now;
        this.periods = periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenHours)) return false;

        OpenHours that = (OpenHours) o;

        if (open_now != that.open_now) return false;
        if (periods != null ? !periods.equals(that.periods) : that.periods != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = open_now ? 1 : 0;
        result = 31 * result + (periods != null ? periods.hashCode() : 0);
        return result;
    }
}
