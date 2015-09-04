package com.seatgeek.placesautocomplete;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;

import com.seatgeek.placesautocomplete.adapter.AbstractPlacesAutocompleteAdapter;
import com.seatgeek.placesautocomplete.adapter.DefaultAutocompleteAdapter;
import com.seatgeek.placesautocomplete.async.BackgroundExecutorService;
import com.seatgeek.placesautocomplete.async.BackgroundJob;
import com.seatgeek.placesautocomplete.history.AutocompleteHistoryManager;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.seatgeek.placesautocomplete.network.PlacesHttpClientResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PlacesAutocompleteTextView extends AutoCompleteTextView {

    public static final boolean DEBUG = false;

    @Nullable
    private AutocompleteResultType resultType;

    @NonNull
    private PlacesApi api;

    @Nullable
    private OnPlaceSelectedListener listener;

    @Nullable
    private AutocompleteHistoryManager historyManager;

    @NonNull
    private AbstractPlacesAutocompleteAdapter adapter;

    private boolean completionEnabled = true;

    public PlacesAutocompleteTextView(@NonNull final Context context, @NonNull final String googleApiKey) {
        super(context);

        init(context, null, R.attr.placesAutoCompleteTextViewStyle, R.style.PACV_Widget_PlacesAutoCompleteTextView, googleApiKey, context.getString(R.string.pac_default_history_file_name));
    }


    public PlacesAutocompleteTextView(@NonNull final Context context, @NonNull final String googleApiKey, @NonNull final String historyFileName) {
        super(context);

        init(context, null, R.attr.placesAutoCompleteTextViewStyle, R.style.PACV_Widget_PlacesAutoCompleteTextView, googleApiKey, historyFileName);
    }

    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.placesAutoCompleteTextViewStyle, R.style.PACV_Widget_PlacesAutoCompleteTextView, null, context.getString(R.string.pac_default_history_file_name));
    }

    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs, final int defAttr) {
        super(context, attrs, defAttr);

        init(context, attrs, defAttr, R.style.PACV_Widget_PlacesAutoCompleteTextView, null, context.getString(R.string.pac_default_history_file_name));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes, null, context.getString(R.string.pac_default_history_file_name));
    }

    private void init(@NonNull final Context context, final AttributeSet attrs, final int defAttr, final int defStyle, final String googleApiKey, final String historyFileName) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlacesAutocompleteTextView, defAttr, defStyle);
        String layoutApiKey = typedArray.getString(R.styleable.PlacesAutocompleteTextView_googleMapsApiKey);
        String layoutAdapterClass = typedArray.getString(R.styleable.PlacesAutocompleteTextView_adapterClass);
        String layoutHistoryFile = typedArray.getString(R.styleable.PlacesAutocompleteTextView_historyFile);
        resultType = AutocompleteResultType.fromEnum(typedArray.getInt(R.styleable.PlacesAutocompleteTextView_resultType, PlacesApi.DEFAULT_RESULT_TYPE.ordinal()));
        typedArray.recycle();

        final String finalHistoryFileName = historyFileName != null ? historyFileName : layoutHistoryFile;

        if (!TextUtils.isEmpty(finalHistoryFileName)) {
            historyManager = AutocompleteHistoryManager.fromPath(context, finalHistoryFileName);
        }

        final String finalApiKey = googleApiKey != null ? googleApiKey : layoutApiKey;

        if (TextUtils.isEmpty(finalApiKey)) {
            throw new InflateException("Did not specify googleApiKey!");
        }

        api = new PlacesApiBuilder()
                .setApiClient(PlacesHttpClientResolver.PLACES_HTTP_CLIENT)
                .setGoogleApiKey(finalApiKey)
                .build();

        if (layoutAdapterClass != null) {
            adapter = adapterForClass(context, layoutAdapterClass);
        } else {
            adapter = new DefaultAutocompleteAdapter(context, api, resultType, historyManager);
        }

        super.setAdapter(adapter);

        super.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Place place = adapter.getItem(position);

                if (listener != null) {
                    listener.onPlaceSelected(place);
                }

                if (historyManager != null) {
                    historyManager.addItemToHistory(place);
                }
            }
        });

        super.setDropDownBackgroundResource(R.drawable.pacv_popup_background_white);
    }

    @Override
    public final void setOnItemSelectedListener(final AdapterView.OnItemSelectedListener l) {
        throw new UnsupportedOperationException("Use set" + OnPlaceSelectedListener.class.getSimpleName() + "() instead");
    }

    @Override
    public final void setOnItemClickListener(final AdapterView.OnItemClickListener l) {
        throw new UnsupportedOperationException("Use set" + OnPlaceSelectedListener.class.getSimpleName() + "() instead");
    }

    @NonNull
    public AbstractPlacesAutocompleteAdapter getAutocompleteAdapter() {
        return adapter;
    }

    public void setOnPlaceSelectedListener(@Nullable OnPlaceSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public final <T extends ListAdapter & Filterable> void setAdapter(@NonNull final T adapter) {
        if (!(adapter instanceof AbstractPlacesAutocompleteAdapter)) {
            throw new IllegalArgumentException("Custom adapters must inherit from " + AbstractPlacesAutocompleteAdapter.class.getSimpleName());
        }

        this.adapter = (AbstractPlacesAutocompleteAdapter) adapter;

        historyManager = this.adapter.getHistoryManager();
        resultType = this.adapter.getResultType();
        api = this.adapter.getApi();

        super.setAdapter(adapter);
    }

    private AbstractPlacesAutocompleteAdapter adapterForClass(final Context context, final String adapterClass) {
        Class<AbstractPlacesAutocompleteAdapter> adapterClazz;
        try {
            adapterClazz = (Class<AbstractPlacesAutocompleteAdapter>) Class.forName(adapterClass);
        } catch (ClassNotFoundException e) {
            throw new InflateException("Unable to find class for specified adapterClass: " + adapterClass, e);
        } catch (ClassCastException e) {
            throw new InflateException(adapterClass + " must inherit from " + AbstractPlacesAutocompleteAdapter.class.getSimpleName(), e);
        }

        Constructor<AbstractPlacesAutocompleteAdapter> adapterConstructor = null;
        try {
            adapterConstructor = adapterClazz.getConstructor(Context.class, PlacesApi.class, AutocompleteResultType.class, AutocompleteHistoryManager.class);
        } catch (NoSuchMethodException e) {
            throw new InflateException("Unable to find valid constructor with params " +
                    Context.class.getSimpleName() +
                    ", " +
                    PlacesApi.class.getSimpleName() +
                    ", " +
                    AutocompleteResultType.class.getSimpleName() +
                    ", and " +
                    AutocompleteHistoryManager.class.getSimpleName() +
                    " for specified adapterClass: " + adapterClass, e);
        }

        try {
            return adapterConstructor.newInstance(context, api, resultType, historyManager);
        } catch (InstantiationException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        } catch (IllegalAccessException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        } catch (InvocationTargetException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        }
    }

    public void setCompletionEnabled(boolean isEnabled) {
        completionEnabled = isEnabled;
    }

    @Override
    public boolean enoughToFilter() {
        return completionEnabled && (historyManager != null || super.enoughToFilter());
    }

    @Override
    public void performCompletion() {
        if (!completionEnabled) {
            return;
        }

        super.performCompletion();
    }

    @Override
    protected void performFiltering(CharSequence text, final int keyCode) {
        if (text == null || text.length() <= getThreshold()) {
            text = text == null ? "" : text;
            super.performFiltering(Constants.MAGIC_HISTORY_VALUE_PRE + text, keyCode);
        } else {
            super.performFiltering(text, keyCode);
        }
    }

    @Override
    protected CharSequence convertSelectionToString(final Object selectedItem) {
        return ((Place) selectedItem).description;
    }

    @Nullable
    public Location getCurrentLocation() {
        return api.getCurrentLocation();
    }

    public void setCurrentLocation(@Nullable final Location currentLocation) {
        api.setCurrentLocation(currentLocation);
    }

    @Nullable
    public Long getRadiusMeters() {
        return api.getRadiusMeters();
    }

    public void setRadiusMeters(final Long radiusMeters) {
        api.setRadiusMeters(radiusMeters);
    }

    public void setLocationBiasEnabled(boolean enabled) {
        api.setLocationBiasEnabled(enabled);
    }

    public boolean isLocationBiasEnabled() {
        return api.isLocationBiasEnabled();
    }

    public void getDetailsFor(final Place place, final DetailsCallback callback) {
        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<PlaceDetails>() {
            @Override
            public PlaceDetails executeInBackground() throws Exception {
                return api.details(place.place_id).result;
            }

            @Override
            public void onSuccess(final PlaceDetails result) {
                if (result != null) {
                    callback.onSuccess(result);
                } else {
                    callback.onFailure(new PlaceDetailsLoadingFailure(place));
                }
            }

            @Override
            public void onFailure(final Throwable error) {
                callback.onFailure(new PlaceDetailsLoadingFailure(place, error));
            }
        });
    }

    @NonNull
    public PlacesApi getApi() {
        return api;
    }

    public void setApi(@NonNull PlacesApi api) {
        this.api = api;

        adapter.setApi(api);
    }

    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return historyManager;
    }

    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        this.historyManager = historyManager;


        adapter.setHistoryManager(historyManager);
    }

    @Nullable
    public AutocompleteResultType getResultType() {
        return resultType;
    }

    public void setResultType(@Nullable AutocompleteResultType resultType) {
        this.resultType = resultType;

        adapter.setResultType(resultType);
    }
}
