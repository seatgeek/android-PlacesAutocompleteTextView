package com.seatgeek.placesautocomplete.history;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.seatgeek.placesautocomplete.model.Place;

import java.util.List;

/**
 * An interface used by the PlacesAutocompleteTextView to first filter with results matching the
 * historical selections by the user
 */
public interface AutocompleteHistoryManager {

    /**
     * @param listener a listener to register to this instance of an AutocompleteHistoryManager that
     *                 will report changes to the sotred history
     */
    void setListener(@Nullable OnHistoryUpdatedListener listener);

    /**
     * @param place a selected place that should be added to the history file
     */
    void addItemToHistory(@NonNull Place place);

    /**
     * @return a list of the past selections. Should be ordered in some logical manner. The default
     * is most recently selected order.
     */
    @NonNull
    List<Place> getPastSelections();
}
