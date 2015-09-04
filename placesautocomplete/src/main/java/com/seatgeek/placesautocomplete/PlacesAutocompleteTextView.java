package com.seatgeek.placesautocomplete;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
    private AutocompleteResultType mResultType;

    @NonNull
    private PlacesApi mApi;

    @Nullable
    private OnPlaceSelectedListener mListener;

    @Nullable
    private AutocompleteHistoryManager mHistoryManager;

    @NonNull
    private AbstractPlacesAutocompleteAdapter mAdapter;

    private boolean mIsCompletionEnabled = true;

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

        init(context, attrs, R.attr.placesAutoCompleteTextViewStyle, R.style.PACV_Widget_PlacesAutoCompleteTextView, null, null);
    }

    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs, final int defAttr) {
        super(context, attrs, defAttr);

        init(context, attrs, defAttr, R.style.PACV_Widget_PlacesAutoCompleteTextView, null, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlacesAutocompleteTextView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes, null, null);
    }

    private void init(@NonNull final Context context, final AttributeSet attrs, final int defAttr, final int defStyle, final String googleApiKey, final String historyFileName) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlacesAutocompleteTextView, defAttr, defStyle);
        String layoutApiKey = typedArray.getString(R.styleable.PlacesAutocompleteTextView_googleMapsApiKey);
        String layoutAdapterClass = typedArray.getString(R.styleable.PlacesAutocompleteTextView_adapterClass);
        String layoutHistoryFile = typedArray.getString(R.styleable.PlacesAutocompleteTextView_historyFile);
        mResultType = AutocompleteResultType.fromEnum(typedArray.getInt(R.styleable.PlacesAutocompleteTextView_resultType, PlacesApi.DEFAULT_RESULT_TYPE.ordinal()));
        typedArray.recycle();

        final String finalHistoryFileName = historyFileName != null ? historyFileName : layoutHistoryFile;

        if (!TextUtils.isEmpty(finalHistoryFileName)) {
            mHistoryManager = AutocompleteHistoryManager.fromPath(context, finalHistoryFileName);
        }

        final String finalApiKey = googleApiKey != null ? googleApiKey : layoutApiKey;

        if (TextUtils.isEmpty(finalApiKey)) {
            throw new InflateException("Did not specify googleApiKey!");
        }

        mApi = new PlacesApiBuilder()
                .setApiClient(PlacesHttpClientResolver.PLACES_HTTP_CLIENT)
                .setGoogleApiKey(finalApiKey)
                .build();

        if (layoutAdapterClass != null) {
            mAdapter = adapterForClass(context, layoutAdapterClass);
        } else {
            mAdapter = new DefaultAutocompleteAdapter(context, mApi, mResultType, mHistoryManager);
        }

        super.setAdapter(mAdapter);

        super.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Place place = mAdapter.getItem(position);

                if (mListener != null) {
                    mListener.onPlaceSelected(place);
                }

                if (mHistoryManager != null) {
                    mHistoryManager.addItemToHistory(place);
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
        return mAdapter;
    }

    public void setOnPlaceSelectedListener(@Nullable OnPlaceSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public final <T extends ListAdapter & Filterable> void setAdapter(@NonNull final T adapter) {
        if (!(adapter instanceof AbstractPlacesAutocompleteAdapter)) {
            throw new IllegalArgumentException("Custom adapters must inherit from " + AbstractPlacesAutocompleteAdapter.class.getSimpleName());
        }

        mAdapter = (AbstractPlacesAutocompleteAdapter) adapter;

        mHistoryManager = mAdapter.getHistoryManager();
        mResultType = mAdapter.getResultType();
        mApi = mAdapter.getApi();

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
            return adapterConstructor.newInstance(context, mApi, mResultType, mHistoryManager);
        } catch (InstantiationException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        } catch (IllegalAccessException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        } catch (InvocationTargetException e) {
            throw new InflateException("Unable to instantiate adapter with name " + adapterClass, e);
        }
    }

    public void setCompletionEnabled(boolean isEnabled) {
        mIsCompletionEnabled = isEnabled;
    }

    @Override
    public boolean enoughToFilter() {
        return mIsCompletionEnabled && (mHistoryManager != null || super.enoughToFilter());
    }

    @Override
    public void performCompletion() {
        if (!mIsCompletionEnabled) {
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
        return mApi.getCurrentLocation();
    }

    public void setCurrentLocation(@Nullable final Location currentLocation) {
        mApi.setCurrentLocation(currentLocation);
    }

    @Nullable
    public Long getRadiusMeters() {
        return mApi.getRadiusMeters();
    }

    public void setRadiusMeters(final Long radiusMeters) {
        mApi.setRadiusMeters(radiusMeters);
    }

    public void setLocationBiasEnabled(boolean enabled) {
        mApi.setLocationBiasEnabled(enabled);
    }

    public boolean isLocationBiasEnabled() {
        return mApi.isLocationBiasEnabled();
    }

    public void getDetailsFor(final Place place, final DetailsCallback callback) {
        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<PlaceDetails>() {
            @Override
            public PlaceDetails executeInBackground() throws Exception {
                return mApi.details(place.place_id).result;
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
        return mApi;
    }

    public void setApi(@NonNull PlacesApi api) {
        mApi = api;

        mAdapter.setApi(api);
    }

    @Nullable
    public AutocompleteHistoryManager getHistoryManager() {
        return mHistoryManager;
    }

    public void setHistoryManager(@Nullable final AutocompleteHistoryManager historyManager) {
        mHistoryManager = historyManager;


        mAdapter.setHistoryManager(historyManager);
    }

    @Nullable
    public AutocompleteResultType getResultType() {
        return mResultType;
    }

    public void setResultType(@Nullable AutocompleteResultType resultType) {
        mResultType = resultType;

        mAdapter.setResultType(resultType);
    }
}
