package com.seatgeek.placesautocomplete.history;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.seatgeek.placesautocomplete.model.Place;

import java.util.List;

public interface AutocompleteHistoryManager {

    void setListener(@Nullable OnHistoryUpdatedListener listener);

    void addItemToHistory(@NonNull Place place);

    @NonNull
    List<Place> getPastSelections();
}
