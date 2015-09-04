package com.seatgeek.placesautocomplete.json;

public final class JsonParserResolver {
    public static final PlacesApiJsonParser JSON_PARSER;

    static {
        boolean hasGson;
        try {
            Class.forName("com.google.gson.Gson");
            hasGson = true;
        } catch (ClassNotFoundException e) {
            hasGson = false;
        }

        JSON_PARSER = hasGson ? new GsonPlacesApiJsonParser() : new AndroidPlacesApiJsonParser();
    }

    private JsonParserResolver() {
        throw new RuntimeException("No instances");
    }
}
