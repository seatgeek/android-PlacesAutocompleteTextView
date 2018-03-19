package com.seatgeek.placesautocompletedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.Filters;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PlacesAutocompleteActivity extends AppCompatActivity {

    @InjectView(R.id.autocomplete)
    PlacesAutocompleteTextView mAutocomplete;

    @InjectView(R.id.street)
    TextView mStreet;

    @InjectView(R.id.city)
    TextView mCity;

    @InjectView(R.id.state)
    TextView mState;

    @InjectView(R.id.zip)
    TextView mZip;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_places_autocomplete);
        ButterKnife.inject(this);

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        Log.d("test", "details " + details);
                        mStreet.setText(details.name);
                        for (AddressComponent component : details.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {
                                    case STREET_NUMBER:
                                        break;
                                    case ROUTE:
                                        break;
                                    case NEIGHBORHOOD:
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        break;
                                    case SUBLOCALITY:
                                        break;
                                    case LOCALITY:
                                        mCity.setText(component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        mState.setText(component.short_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        break;
                                    case POSTAL_CODE:
                                        mZip.setText(component.long_name);
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });
            }
        });
        // Filter result by country.
        // Currently, you can use components to filter by up to 5 countries.
        // Countries must be passed as a two character, ISO 3166-1 Alpha-2 compatible country code.
        // https://developers.google.com/places/web-service/autocomplete
        Filters f = new Filters();
        f.setCountry("US");
        f.setCountry("SE");
        f.setCountry("UK");
        mAutocomplete.getAutocompleteAdapter().setSessionFilter(f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_hide_x) {
            mAutocomplete.showClearButton(false);
        }
        if (id == R.id.action_show_x) {
            mAutocomplete.showClearButton(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
