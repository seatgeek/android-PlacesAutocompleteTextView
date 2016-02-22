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
import com.seatgeek.placesautocomplete.history.DefaultAutocompleteHistoryManager;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.seatgeek.placesautocomplete.network.PlacesHttpClientResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PlacesAutocompleteTextView extends AutoCompleteTextView {

    public static final boolean DEBUG = true;

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

    /**
     * Creates a new PlacesAutocompleteTextView with the provided API key and the default history file
     */
    public PlacesAutocompleteTextView(@NonNull final Context context, @NonNull final String googleApiKey) {
        super(context);

        init(context, null, R.attr.pacv_placesAutoCompleteTextViewStyle, R.style.PACV_Widget_PlacesAutoCompleteTextView, googleApiKey, context.getString(R.string.pacv_default_history_file_name));
    }

    /**
     * Creates a new PlacesAutocompleteTextView with the provided API key and the provided history file
     */
    public PlacesAutocompleteTextView(@NonNull final Context context, @NonNull final String googleApiKey, @NonNull final String historyFileName) {
        super(context);

        init(context, null, R.attr.pacv_placesAutoCompleteTextViewStyle, R.style.PACV_Widget_PlacesAutoCompleteTextView, googleApiKey, historyFileName);
    }

    /**
     * Constructor for layout inflation
     */
    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.pacv_placesAutoCompleteTextViewStyle, R.style.PACV_Widget_PlacesAutoCompleteTextView, null, context.getString(R.string.pacv_default_history_file_name));
    }

    /**
     * Constructor for layout inflation
     */
    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs, final int defAttr) {
        super(context, attrs, defAttr);

        init(context, attrs, defAttr, R.style.PACV_Widget_PlacesAutoCompleteTextView, null, context.getString(R.string.pacv_default_history_file_name));
    }

    /**
     * Constructor for layout inflation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes, null, context.getString(R.string.pacv_default_history_file_name));
    }

    // perform basic initialization of the view by fetching layout attributes and creating the api, etc.
    private void init(@NonNull final Context context, final AttributeSet attrs, final int defAttr, final int defStyle, final String googleApiKey, final String historyFileName) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlacesAutocompleteTextView, defAttr, defStyle);
        String layoutApiKey = typedArray.getString(R.styleable.PlacesAutocompleteTextView_pacv_googleMapsApiKey);
        String layoutAdapterClass = typedArray.getString(R.styleable.PlacesAutocompleteTextView_pacv_adapterClass);
        String layoutHistoryFile = typedArray.getString(R.styleable.PlacesAutocompleteTextView_pacv_historyFile);
        String languageCode = typedArray.getString(R.styleable.PlacesAutocompleteTextView_pacv_languageCode);
        resultType = AutocompleteResultType.fromEnum(typedArray.getInt(R.styleable.PlacesAutocompleteTextView_pacv_resultType, PlacesApi.DEFAULT_RESULT_TYPE.ordinal()));
        typedArray.recycle();

        final String finalHistoryFileName = historyFileName != null ? historyFileName : layoutHistoryFile;

        if (!TextUtils.isEmpty(finalHistoryFileName)) {
            historyManager = DefaultAutocompleteHistoryManager.fromPath(context, finalHistoryFileName);
        }

        final String finalApiKey = googleApiKey != null ? googleApiKey : layoutApiKey;

        if (TextUtils.isEmpty(finalApiKey)) {
            throw new InflateException("Did not specify googleApiKey!");
        }

        api = new PlacesApiBuilder()
                .setApiClient(PlacesHttpClientResolver.PLACES_HTTP_CLIENT)
                .setGoogleApiKey(finalApiKey)
                .build();
        if (languageCode != null) {
            api.setLanguageCode(languageCode);
        }

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

    /**
     * DO NOT USE. Prefer {@link #setOnPlaceSelectedListener} instead
     */
    @Override
    public final void setOnItemSelectedListener(final AdapterView.OnItemSelectedListener l) {
        throw new UnsupportedOperationException("Use set" + OnPlaceSelectedListener.class.getSimpleName() + "() instead");
    }

    /**
     * DO NOT USE. Prefer {@link #setOnPlaceSelectedListener} instead
     */
    @Override
    public final void setOnItemClickListener(final AdapterView.OnItemClickListener l) {
        throw new UnsupportedOperationException("Use set" + OnPlaceSelectedListener.class.getSimpleName() + "() instead");
    }

    /**
     * Registers a listener for callbacks when a new {@link Place} is selected from the autocomplete
     * popup
     */
    public void setOnPlaceSelectedListener(@Nullable OnPlaceSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * @return the current adapter for displaying the list of results in the popup window
     */
    @NonNull
    public AbstractPlacesAutocompleteAdapter getAutocompleteAdapter() {
        return adapter;
    }

    /**
     * @param adapter the adapter for displaying the list of results in the popup window, must
     *                extend {@link AbstractPlacesAutocompleteAdapter} to maintain certain logic
     */
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

    // fun way to set adapters as layout attributes
    private AbstractPlacesAutocompleteAdapter adapterForClass(final Context context, final String adapterClass) {
        Class<AbstractPlacesAutocompleteAdapter> adapterClazz;
        try {
            adapterClazz = (Class<AbstractPlacesAutocompleteAdapter>) Class.forName(adapterClass);
        } catch (ClassNotFoundException e) {
            throw new InflateException("Unable to find class for specified adapterClass: " + adapterClass, e);
        } catch (ClassCastException e) {
            throw new InflateException(adapterClass + " must inherit from " + AbstractPlacesAutocompleteAdapter.class.getSimpleName(), e);
        }

        Constructor<AbstractPlacesAutocompleteAdapter> adapterConstructor;
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
                    DefaultAutocompleteHistoryManager.class.getSimpleName() +
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

    /**
     * Controls the autocompletion feature.
     * @param isEnabled if false, no autocompletion will occur. Default is true
     */
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

    /**
     * @return the current location in use for location biasing. By default, biasing uses geoip
     */
    @Nullable
    public Location getCurrentLocation() {
        return api.getCurrentLocation();
    }

    /**
     * Sets the location that will be used for biasing the Place results. The API will favor Places
     * close to the set location when producing results
     * @param currentLocation the Location to bias results towards
     */
    public void setCurrentLocation(@Nullable final Location currentLocation) {
        api.setCurrentLocation(currentLocation);
    }

    /**
     * @return the radius, in meters.
     */
    @Nullable
    public Long getRadiusMeters() {
        return api.getRadiusMeters();
    }

    /**
     * @param radiusMeters  The radius from the provided location to bias results with. By default,
     *                      the Places API biases with x meters. To disable
     *                      the bias radius but maintain the biasing, use the
     *                      {@link PlacesApi#NO_BIAS_RADIUS}
     */
    public void setRadiusMeters(final Long radiusMeters) {
        api.setRadiusMeters(radiusMeters);
    }

    /**
     * Allows for enabling and disabling location biasing in the Places api.
     * @param enabled is biasing should be enabled. true by default.
     */
    public void setLocationBiasEnabled(boolean enabled) {
        api.setLocationBiasEnabled(enabled);
    }

    /**
     * @return if the Places API is currently going to return results biased to the device's current
     * location
     */
    public boolean isLocationBiasEnabled() {
        return api.isLocationBiasEnabled();
    }

    /**
     * A helper method for fetching the {@link PlaceDetails} from the PlacesApi
     * @param place the place to get details for
     * @param callback a callback that will be invoked on the main thread when the place details
     *                 has been fetched from the Places API
     */
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

    /**
     * @return the {@link PlacesApi} that the Autocomplete view is using to fetch results from the
     * Google Maps Places API. You can use this to make custom requests to the API, if you so choose.
     */
    @NonNull
    public PlacesApi getApi() {
        return api;
    }

    /**
     * A setter for the {@link PlacesApi} that the Autocomplete view will use to fetch results from the
     * Google Maps Places API. You can provide your own customizations build creating your own API.
     * @param api the API to use for autocompletion and place details requests
     */
    public void setApi(@NonNull PlacesApi api) {
        this.api = api;

        adapter.setApi(api);
    }

    /**
     * @return the current AutocompleteHistoryManager that stores and provides the selection history
     * for Places in the Autocomplete view
     */
    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
     * Allows for passing your own implementation of the AutocompleteHistoryManager. This would be
     * if you wanted to provide your own storage mechanism (e.g. sqlite, shared prefs, etc.) for
     * whatever reasoning you'd want.
     * @param historyManager The new history manager for managing the storage of selected Places for
     *                       later autocompletion use. Setting this to null will disable history
     */
    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        this.historyManager = historyManager;

        adapter.setHistoryManager(historyManager);
    }

    /**
     * @return the current result type for autocompletion results
     */
    @Nullable
    public AutocompleteResultType getResultType() {
        return resultType;
    }

    /**
     * @param resultType the result type to determine the types of Places returned by the Places API
     */
    public void setResultType(@Nullable AutocompleteResultType resultType) {
        this.resultType = resultType;

        adapter.setResultType(resultType);
    }
}
