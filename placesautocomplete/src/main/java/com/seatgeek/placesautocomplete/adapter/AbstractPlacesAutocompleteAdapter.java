package com.seatgeek.placesautocomplete.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.seatgeek.placesautocomplete.PlacesApi;
import com.seatgeek.placesautocomplete.history.AutocompleteHistoryManager;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.util.ArrayAdapterDelegate;

import java.util.Collection;

public abstract class AbstractPlacesAutocompleteAdapter extends ArrayAdapter<Place> {

    @NonNull
    private final PlacesApiFilter mFilter;

    protected AbstractPlacesAutocompleteAdapter(@NonNull final Context context,
                                                @NonNull final PlacesApi api,
                                                @Nullable final AutocompleteResultType autocompleteResultType,
                                                @Nullable AutocompleteHistoryManager history) {
        super(context, 0);

        mFilter = new PlacesApiFilter(
                api,
                autocompleteResultType,
                history,
                new ArrayAdapterDelegate<Place>() {

                    @Override
                    public void setNotifyOnChange(final boolean notifyOnChange) {
                        AbstractPlacesAutocompleteAdapter.this.setNotifyOnChange(notifyOnChange);
                    }

                    @Override
                    public void clear() {
                        AbstractPlacesAutocompleteAdapter.this.clear();
                    }

                    @Override
                    public void addAll(final Collection<Place> values) {
                        if (values != null) {
                            AbstractPlacesAutocompleteAdapter.this.addAll(values);
                        }
                    }

                    @Override
                    public void notifyDataSetChanged() {
                        AbstractPlacesAutocompleteAdapter.this.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView == null ? newView(parent) : convertView;

        bindView(view, getItem(position));

        return view;
    }

    protected abstract View newView(final ViewGroup parent);

    protected abstract void bindView(final View view, final Place item);

    @Override
    @NonNull
    public Filter getFilter() {
        return mFilter;
    }

    public void setApi(@NonNull final PlacesApi api) {
        mFilter.setApi(api);
    }

    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        mFilter.setHistoryManager(historyManager);
    }

    public void setResultType(@Nullable final AutocompleteResultType resultType) {
        mFilter.setResultType(resultType);
    }

    @NonNull
    public PlacesApi getApi() {
        return mFilter.getApi();
    }

    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return mFilter.getHistoryManager();
    }

    @Nullable
    public AutocompleteResultType getResultType() {
        return mFilter.getResultType();
    }
}
