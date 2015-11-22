package com.seatgeek.placesautocomplete.model;

public final class RatingAspect {
    public final int rating;
    public final String type;

    public RatingAspect(final int rating, final String type) {
        this.rating = rating;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingAspect)) return false;

        RatingAspect that = (RatingAspect) o;

        if (rating != that.rating) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rating;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
