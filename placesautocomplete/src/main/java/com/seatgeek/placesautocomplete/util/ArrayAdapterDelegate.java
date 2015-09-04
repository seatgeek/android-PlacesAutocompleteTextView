package com.seatgeek.placesautocomplete.util;

import java.util.Collection;

public interface ArrayAdapterDelegate<T> {
    void setNotifyOnChange(boolean notifyOnChange);

    void clear();

    void addAll(Collection<T> values);

    void notifyDataSetChanged();
}
