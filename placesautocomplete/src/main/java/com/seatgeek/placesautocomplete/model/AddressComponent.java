package com.seatgeek.placesautocomplete.model;

import java.util.List;

public final class AddressComponent {
    public final String long_name;
    public final String short_name;
    public final List<AddressComponentType> types;

    public AddressComponent(final String long_name, final String short_name, final List<AddressComponentType> types) {
        this.long_name = long_name;
        this.short_name = short_name;
        this.types = types;
    }

    @Override
    public String toString() {
        return "AddressComponent{" +
                "long_name='" + long_name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", types=" + types +
                '}';
    }
}
