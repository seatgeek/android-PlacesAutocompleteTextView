package com.seatgeek.placesautocomplete.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public enum AddressComponentType implements Parcelable{
    @SerializedName("administrative_area_level_1")
    ADMINISTRATIVE_AREA_LEVEL_1,
    @SerializedName("administrative_area_level_2")
    ADMINISTRATIVE_AREA_LEVEL_2,
    @SerializedName("administrative_area_level_3")
    ADMINISTRATIVE_AREA_LEVEL_3,
    @SerializedName("administrative_area_level_4")
    ADMINISTRATIVE_AREA_LEVEL_4,
    @SerializedName("administrative_area_level_5")
    ADMINISTRATIVE_AREA_LEVEL_5,
    @SerializedName("colloquial_area")
    COLLOQUIAL_AREA,
    @SerializedName("country")
    COUNTRY,
    @SerializedName("floor")
    FLOOR,
    @SerializedName("geocode")
    GEOCODE,
    @SerializedName("intersection")
    INTERSECTION,
    @SerializedName("locality")
    LOCALITY,
    @SerializedName("natural_feature")
    NATURAL_FEATURE,
    @SerializedName("neighborhood")
    NEIGHBORHOOD,
    @SerializedName("political")
    POLITICAL,
    @SerializedName("point_of_interest")
    POINT_OF_INTEREST,
    @SerializedName("post_box")
    POST_BOX,
    @SerializedName("postal_code")
    POSTAL_CODE,
    @SerializedName("postal_code_prefix")
    POSTAL_CODE_PREFIX,
    @SerializedName("postal_code_suffix")
    POSTAL_CODE_SUFFIX,
    @SerializedName("postal_town")
    POSTAL_TOWN,
    @SerializedName("premise")
    PREMISE,
    @SerializedName("room")
    ROOM,
    @SerializedName("route")
    ROUTE,
    @SerializedName("street_address")
    STREET_ADDRESS,
    @SerializedName("street_number")
    STREET_NUMBER,
    @SerializedName("sublocality")
    SUBLOCALITY,
    @SerializedName("sublocality_level_4")
    SUBLOCALITY_LEVEL_4,
    @SerializedName("sublocality_level_5")
    SUBLOCALITY_LEVEL_5,
    @SerializedName("sublocality_level_3")
    SUBLOCALITY_LEVEL_3,
    @SerializedName("sublocality_level_2")
    SUBLOCALITY_LEVEL_2,
    @SerializedName("sublocality_level_1")
    SUBLOCALITY_LEVEL_1,
    @SerializedName("subpremise")
    SUBPREMISE,
    @SerializedName("transit_station")
    TRANSIT_STATION;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<AddressComponentType> CREATOR = new Creator<AddressComponentType>() {
        @Override
        public AddressComponentType createFromParcel(final Parcel source) {
            return AddressComponentType.values()[source.readInt()];
        }

        @Override
        public AddressComponentType[] newArray(final int size) {
            return new AddressComponentType[size];
        }
    };
}
