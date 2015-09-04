package com.seatgeek.placesautocomplete.model;

/**
 * A broken-down part of the terms in the description of the Place. For instance:
 * "Park Avenue South, New York, NY, United States" has the array:
 *        terms: [
 *          {
 *            offset: 0,
 *            value: "Park Avenue South"
 *          },
 *          {
 *            offset: 19,
 *            value: "New York"
 *          },
 *          {
 *            offset: 29,
 *            value: "NY"
 *          },
 *          {
 *            offset: 33,
 *            value: "United States"
 *          }
 *        ]
 */
public final class DescriptionTerm {

    /**
     * the offset index at which this term appears in the description
     */
    public final int offset;

    /**
     * the value of the term
     */
    public final String value;

    public DescriptionTerm(final int offset, final String value) {
        this.offset = offset;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DescriptionTerm)) return false;

        DescriptionTerm that = (DescriptionTerm) o;

        if (offset != that.offset) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = offset;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
