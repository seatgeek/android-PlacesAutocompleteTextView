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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressComponent)) return false;

        AddressComponent that = (AddressComponent) o;

        if (long_name != null ? !long_name.equals(that.long_name) : that.long_name != null) return false;
        if (short_name != null ? !short_name.equals(that.short_name) : that.short_name != null) return false;
        if (types != null ? !types.equals(that.types) : that.types != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = long_name != null ? long_name.hashCode() : 0;
        result = 31 * result + (short_name != null ? short_name.hashCode() : 0);
        result = 31 * result + (types != null ? types.hashCode() : 0);
        return result;
    }
}
