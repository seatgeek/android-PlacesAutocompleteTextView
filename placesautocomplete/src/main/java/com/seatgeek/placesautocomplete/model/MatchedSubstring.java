package com.seatgeek.placesautocomplete.model;

/**
 * A class describing the matched substrings of the query text from the description
 */
public final class MatchedSubstring {

    /**
     * the length of the matched substring
     */
    public final int length;

    /**
     * the offset at which the matched substring occurs in the description
     */
    public final int offset;

    public MatchedSubstring(final int length, final int offset) {
        this.length = length;
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchedSubstring)) return false;

        MatchedSubstring that = (MatchedSubstring) o;

        if (length != that.length) return false;
        if (offset != that.offset) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + offset;
        return result;
    }
}
