package com.seatgeek.placesautocomplete.model;

import com.google.gson.annotations.SerializedName;

public enum PlaceScope {
    @SerializedName("APP")
    APP,
    @SerializedName("GOOGLE")
    GOOGLE;
}
