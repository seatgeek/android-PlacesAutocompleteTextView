package com.seatgeek.placesautocomplete.model;

public final class DateTimePair {

    /**
     * "a number from 0–6, corresponding to the days of the week, starting on Sunday. For example, 2 means Tuesday"
     */
    public final String day;
    /**
     * may contain a time of day in 24-hour hhmm format. Values are in the range 0000–2359. The time will be reported in the place’s time zone.
     */
    public final String time;

    public DateTimePair(final String day, final String time) {
        this.day = day;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateTimePair)) return false;

        DateTimePair that = (DateTimePair) o;

        if (day != null ? !day.equals(that.day) : that.day != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
