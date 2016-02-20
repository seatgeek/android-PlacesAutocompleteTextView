package com.seatgeek.placesautocomplete.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public final class AddressComponent implements Parcelable{
    public String long_name;
    public String short_name;
    public List<AddressComponentType> types;

    public AddressComponent(final String long_name, final String short_name, final List<AddressComponentType> types) {
        this.long_name = long_name;
        this.short_name = short_name;
        this.types = types;
    }

  public AddressComponent(Parcel in) {
    this.long_name = in.readString();
    this.short_name = in.readString();
    this.types = in.readArrayList(AddressComponentType.class.getClassLoader());
  }

  @Override
    public String toString() {
        return "AddressComponent{" +
                "long_name='" + long_name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", types=" + types +
                '}';
    }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.long_name);
    dest.writeString(this.short_name);
    dest.writeList(this.types);
  }
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public AddressComponent createFromParcel(Parcel in) {
      return new AddressComponent(in);
    }

    public AddressComponent[] newArray(int size) {
      return new AddressComponent[size];
    }
  };
}
