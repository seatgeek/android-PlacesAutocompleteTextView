package com.seatgeek.placesautocomplete.model;

import java.util.List;

public final class OpenHours {
    public final boolean open_now;
    public final List<OpenPeriod> periods;

    public OpenHours(final boolean open_now, final List<OpenPeriod> periods) {
        this.open_now = open_now;
        this.periods = periods;
    }
}
