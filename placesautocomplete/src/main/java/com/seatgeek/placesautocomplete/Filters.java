package com.seatgeek.placesautocomplete;

import java.util.HashSet;
import java.util.Set;

/**
 * Object for passing filters around.
 */
public class Filters {

    private final Set<String> countries = new HashSet<>();

    public Filters() {
    }

    public static Filters getDefault() {
        return new Filters();
    }

    public boolean hasCountry() {
        return !(countries.size() == 0);
    }

    public Set<String> getCountries() {
        return countries;
    }

    public void setCountry(String country) {
        if(country != null) {
            this.countries.add(country);
        }else{
            this.countries.clear();
        }
    }

    public void removeCountry(String title) {
        this.countries.remove(title);
    }
}