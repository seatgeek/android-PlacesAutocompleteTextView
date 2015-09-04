package com.seatgeek.placesautocomplete.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.AtomicFile;
import android.text.TextUtils;
import android.util.Log;

import com.seatgeek.placesautocomplete.Constants;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.async.BackgroundExecutorService;
import com.seatgeek.placesautocomplete.async.BackgroundJob;
import com.seatgeek.placesautocomplete.json.JsonParserResolver;
import com.seatgeek.placesautocomplete.json.PlacesApiJsonParser;
import com.seatgeek.placesautocomplete.model.Place;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutocompleteHistoryManager {
    private static final String BASE_AUTOCOMPLETE_HISTORY_DIR = "autocomplete/";
    private static final int MAX_HISTORY_ITEM_COUNT = 5;

    public static AutocompleteHistoryManager fromPath(@NonNull Context context, @NonNull String historyFileName) {
        if (TextUtils.isEmpty(historyFileName)) {
            throw new IllegalArgumentException("Cannot have an empty historyFile name");
        }

        File historyFile = new File(context.getCacheDir(), BASE_AUTOCOMPLETE_HISTORY_DIR + historyFileName);

        return new AutocompleteHistoryManager(historyFile, JsonParserResolver.JSON_PARSER);
    }

    @NonNull
    private final AtomicFile mSavedFile;

    @NonNull
    private final PlacesApiJsonParser mJsonParser;

    @NonNull
    private List<Place> mPlaces;

    @Nullable
    private OnHistoryUpdatedListener mListener;

    private AutocompleteHistoryManager(@NonNull final File historyFile, @NonNull final PlacesApiJsonParser parser) {
        mSavedFile = new AtomicFile(historyFile);

        mJsonParser = parser;

        mPlaces = new ArrayList<Place>();

        readPlaces();
    }

    private void readPlaces() {
        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<List<Place>>() {
            @Override
            public List<Place> executeInBackground() throws Exception {
                InputStream is = null;
                try {
                    is = mSavedFile.openRead();
                    return mJsonParser.readHistoryJson(is);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }

            @Override
            public void onSuccess(final List<Place> result) {
                Collections.reverse(result);

                for (Place place : result) {
                    internalAddItem(place);
                }
            }

            @Override
            public void onFailure(final Throwable error) {
                if (PlacesAutocompleteTextView.DEBUG) {
                    Log.e(Constants.LOG_TAG, "Unable to load history from history file", error);
                }
            }
        });
    }

    public void setListener(@Nullable final OnHistoryUpdatedListener listener) {
        mListener = listener;
    }

    public void addItemToHistory(@NonNull final Place place) {
        internalAddItem(place);

        executeSave();
    }

    private void internalAddItem(@NonNull final Place place) {
        mPlaces.remove(place);
        mPlaces.add(0, place);

        trimPlaces();
    }

    private void trimPlaces() {
        if (mPlaces.size() > MAX_HISTORY_ITEM_COUNT) {
            mPlaces = new ArrayList<>(mPlaces.subList(0, MAX_HISTORY_ITEM_COUNT));
        }
    }

    private void executeSave() {
        final List<Place> finalPlaces = new ArrayList<>(mPlaces);

        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<Void>() {
            @Override
            public Void executeInBackground() throws Exception {
                FileOutputStream fos = null;
                try {
                    fos = mSavedFile.startWrite();
                    mJsonParser.writeHistoryJson(fos, finalPlaces);
                } catch (IOException e) {
                    mSavedFile.failWrite(fos);
                    throw new IOException("Failed history file write", e);
                } finally {
                    mSavedFile.finishWrite(fos);
                }
                return null;
            }

            @Override
            public void onSuccess(Void result) {
                fireUpdatedListener();
                if (PlacesAutocompleteTextView.DEBUG) {
                    Log.i(Constants.LOG_TAG, "Successfully wrote autocomplete history.");
                }
            }

            @Override
            public void onFailure(final Throwable error) {
                mPlaces = new ArrayList<Place>();
                fireUpdatedListener();
                if (PlacesAutocompleteTextView.DEBUG) {
                    Log.e(Constants.LOG_TAG, "Failure to save the autocomplete history!", error);
                }
            }

            private void fireUpdatedListener() {
                if (mListener != null) {
                    mListener.onHistoryUpdated(mPlaces);
                }
            }
        });
    }

    @NonNull
    public List<Place> getPastSelections() {
        return mPlaces;
    }
}
