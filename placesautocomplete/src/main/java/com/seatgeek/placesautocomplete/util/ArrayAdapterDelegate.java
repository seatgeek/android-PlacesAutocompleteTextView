package com.seatgeek.placesautocomplete.util;

import java.util.Collection;

public interface ArrayAdapterDelegate<T> {
    public void setNotifyOnChange(boolean notifyOnChange);

    public void clear();

    public void addAll(Collection<T> values);

    public void notifyDataSetChanged();
}
