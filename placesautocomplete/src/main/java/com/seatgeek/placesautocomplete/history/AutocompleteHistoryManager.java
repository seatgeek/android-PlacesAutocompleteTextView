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
import java.io.FileNotFoundException;
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
    private final AtomicFile savedFile;

    @NonNull
    private final PlacesApiJsonParser jsonParser;

    @NonNull
    private List<Place> places;

    @Nullable
    private OnHistoryUpdatedListener listener;

    private AutocompleteHistoryManager(@NonNull final File historyFile, @NonNull final PlacesApiJsonParser parser) {
        savedFile = new AtomicFile(historyFile);

        jsonParser = parser;

        places = new ArrayList<>();

        readPlaces();
    }

    private void readPlaces() {
        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<List<Place>>() {
            @Override
            public List<Place> executeInBackground() throws Exception {
                if (!savedFile.getBaseFile().exists()) {
                    throw new FileNotFoundException("History file does not exist");
                }

                InputStream is = null;
                try {
                    is = savedFile.openRead();
                    return jsonParser.readHistoryJson(is);
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
        this.listener = listener;
    }

    public void addItemToHistory(@NonNull final Place place) {
        internalAddItem(place);

        executeSave();
    }

    private void internalAddItem(@NonNull final Place place) {
        places.remove(place);
        places.add(0, place);

        trimPlaces();
    }

    private void trimPlaces() {
        if (places.size() > MAX_HISTORY_ITEM_COUNT) {
            places = new ArrayList<>(places.subList(0, MAX_HISTORY_ITEM_COUNT));
        }
    }

    private void executeSave() {
        final List<Place> finalPlaces = new ArrayList<>(places);

        BackgroundExecutorService.INSTANCE.enqueue(new BackgroundJob<Void>() {
            @Override
            public Void executeInBackground() throws Exception {
                FileOutputStream fos = null;
                try {
                    fos = savedFile.startWrite();
                    jsonParser.writeHistoryJson(fos, finalPlaces);
                } catch (IOException e) {
                    savedFile.failWrite(fos);
                    throw new IOException("Failed history file write", e);
                } finally {
                    savedFile.finishWrite(fos);
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
                places = new ArrayList<>();
                fireUpdatedListener();
                if (PlacesAutocompleteTextView.DEBUG) {
                    Log.e(Constants.LOG_TAG, "Failure to save the autocomplete history!", error);
                }
            }

            private void fireUpdatedListener() {
                if (listener != null) {
                    listener.onHistoryUpdated(places);
                }
            }
        });
    }

    @NonNull
    public List<Place> getPastSelections() {
        return places;
    }
}
