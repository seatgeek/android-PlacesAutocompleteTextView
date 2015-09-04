package com.seatgeek.placesautocomplete.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Filter;

import com.seatgeek.placesautocomplete.Constants;
import com.seatgeek.placesautocomplete.PlacesApi;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.history.AutocompleteHistoryManager;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlacesAutocompleteResponse;
import com.seatgeek.placesautocomplete.util.ArrayAdapterDelegate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlacesApiFilter extends Filter {
    @NonNull
    private PlacesApi api;

    @Nullable
    private AutocompleteHistoryManager historyManager;

    @Nullable
    private AutocompleteResultType resultType;

    @NonNull
    private final ArrayAdapterDelegate<Place> adapterDelegate;

    public PlacesApiFilter(@NonNull final PlacesApi api,
                           @Nullable final AutocompleteResultType resultType,
                           @Nullable final AutocompleteHistoryManager historyManager,
                           @NonNull final ArrayAdapterDelegate<Place> adapterDelegate) {
        this.api = api;
        this.resultType = resultType;
        this.historyManager = historyManager;
        this.adapterDelegate = adapterDelegate;
    }

    @Override
    protected FilterResults performFiltering(final CharSequence constraint) {
        final FilterResults filterResults = new FilterResults();

        String stringConstraint = constraint != null ? constraint.toString() : "";
        boolean history = stringConstraint.startsWith(Constants.MAGIC_HISTORY_VALUE_PRE);

        if (history) {
            stringConstraint = stringConstraint.substring(Constants.MAGIC_HISTORY_VALUE_PRE.length());
        }

        final String finalStringConstraint = stringConstraint;

        if (TextUtils.isEmpty(finalStringConstraint) && historyManager == null) {
            if (PlacesAutocompleteTextView.DEBUG) {
                Log.w(Constants.LOG_TAG, "Autocomplete called with an empty string...");
            }
            filterResults.values = new ArrayList<Place>(0);
            filterResults.count = 0;
        } else if ((TextUtils.isEmpty(finalStringConstraint) || history) && historyManager != null) {
            final List<Place> pastSelections = historyManager.getPastSelections();

            sortHistory(finalStringConstraint, pastSelections, false);

            filterResults.values = pastSelections;
            filterResults.count = pastSelections.size();
        } else {
            try {
                final PlacesAutocompleteResponse response = api.autocomplete(finalStringConstraint, resultType);
                filterResults.values = response.predictions;
            } catch (final IOException e) {
                if (PlacesAutocompleteTextView.DEBUG) {
                    Log.e(Constants.LOG_TAG, "Unable to fetch autocomplete results from the api", e);
                }
                filterResults.values = new ArrayList<Place>(0);
                filterResults.count = 0;
            }

            final List<Place> pastSelections = historyManager != null ? historyManager.getPastSelections() : null;
            if (pastSelections != null && !pastSelections.isEmpty()) {
                sortHistory(finalStringConstraint, pastSelections, true);

                for (final Place pastSelection : pastSelections) {
                    if (pastSelection.description.startsWith(finalStringConstraint)) {
                        // remove the item if it was already returned from the api
                        ((List<Place>) filterResults.values).remove(pastSelection);
                        // insert into top
                        ((List<Place>)filterResults.values).add(0, pastSelection);
                    }
                }
            }
            filterResults.count = ((List<Place>) filterResults.values).size();
        }

        return filterResults;
    }

    private static void sortHistory(final String finalStringConstraint, final List<Place> pastSelections, final boolean asc) {
        if (!pastSelections.isEmpty()) {
            Collections.sort(pastSelections, new Comparator<Place>() {
                @Override
                public int compare(final Place lhs, final Place rhs) {
                    final boolean lhsStarts = lhs.description.startsWith(finalStringConstraint);
                    final boolean rhsStarts = rhs.description.startsWith(finalStringConstraint);
                    return lhsStarts && rhsStarts ? 0 : lhsStarts ? (asc ? 1 : -1) : (asc ? -1 : 1);
                }
            });
        }
    }

    @Override
    protected void publishResults(final CharSequence constraint, final FilterResults results) {
        adapterDelegate.setNotifyOnChange(false);
        adapterDelegate.clear();
        adapterDelegate.addAll((Collection<Place>) results.values);
        adapterDelegate.notifyDataSetChanged();
    }

    public void setApi(@NonNull final PlacesApi api) {
        this.api = api;
    }

    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void setResultType(@Nullable final AutocompleteResultType resultType) {
        this.resultType = resultType;
    }

    @NonNull
    public PlacesApi getApi() {
        return api;
    }

    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return historyManager;
    }

    @Nullable
    public AutocompleteResultType getResultType() {
        return resultType;
    }
}
