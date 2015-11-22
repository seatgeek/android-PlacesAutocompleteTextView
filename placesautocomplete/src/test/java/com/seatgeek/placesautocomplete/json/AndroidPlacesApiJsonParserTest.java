package com.seatgeek.placesautocomplete.json;

import android.util.JsonReader;

import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.AlternativePlaceId;
import com.seatgeek.placesautocomplete.model.DateTimePair;
import com.seatgeek.placesautocomplete.model.DescriptionTerm;
import com.seatgeek.placesautocomplete.model.MatchedSubstring;
import com.seatgeek.placesautocomplete.model.OpenHours;
import com.seatgeek.placesautocomplete.model.OpenPeriod;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.seatgeek.placesautocomplete.model.PlaceGeometry;
import com.seatgeek.placesautocomplete.model.PlaceLocation;
import com.seatgeek.placesautocomplete.model.PlacePhoto;
import com.seatgeek.placesautocomplete.model.PlaceReview;
import com.seatgeek.placesautocomplete.model.PlaceScope;
import com.seatgeek.placesautocomplete.model.PlaceType;
import com.seatgeek.placesautocomplete.model.RatingAspect;
import com.seatgeek.placesautocomplete.model.Status;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndroidPlacesApiJsonParserTest {
    private AndroidPlacesApiJsonParser parser;
    private JsonReader reader;

    @Before
    public void setUp() {
        parser = mock(AndroidPlacesApiJsonParser.class);
        reader = mock(JsonReader.class);
    }

    @Test
    public void readPlaceTypesArrayTest() throws IOException {
        when(parser.readPlaceTypesArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, false);
        when(reader.nextString()).thenReturn("route", "geocode", "skip");
        List<PlaceType> expected = Arrays.asList(PlaceType.ROUTE, PlaceType.GEOCODE);
        List<PlaceType> actual = parser.readPlaceTypesArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readDescriptionTermsArrayTest() throws IOException {
        when(parser.readDescriptionTermsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, false, true, false, false);
        when(reader.nextName()).thenReturn("offset", "value", "skip");
        when(reader.nextInt()).thenReturn(1);
        when(reader.nextString()).thenReturn("one");
        List<DescriptionTerm> expected = Arrays.asList(new DescriptionTerm(1, "one"), new DescriptionTerm(-1, null));
        List<DescriptionTerm> actual = parser.readDescriptionTermsArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readMatchedSubstringsArrayTest() throws IOException {
        when(parser.readMatchedSubstringsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, false, true, false, false);
        when(reader.nextName()).thenReturn("length", "offset", "skip");
        when(reader.nextInt()).thenReturn(1, 2);
        List<MatchedSubstring> expected = Arrays.asList(new MatchedSubstring(1, 2), new MatchedSubstring(-1, -1));
        List<MatchedSubstring> actual = parser.readMatchedSubstringsArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readPlaceTest() throws IOException {
        when(parser.readPlace(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, true, true, false);
        when(reader.nextName()).thenReturn("description", "place_id", "matched_substrings", "terms", "types", "skip");
        when(reader.nextString()).thenReturn("desc", "id");
        List<MatchedSubstring> substrings = Collections.singletonList(new MatchedSubstring(1, 2));
        when(parser.readMatchedSubstringsArray(reader)).thenReturn(substrings);
        List<DescriptionTerm> terms = Collections.singletonList(new DescriptionTerm(3, "three"));
        when(parser.readDescriptionTermsArray(reader)).thenReturn(terms);
        List<PlaceType> types = Arrays.asList(PlaceType.ROUTE, PlaceType.GEOCODE);
        when(parser.readPlaceTypesArray(reader)).thenReturn(types);
        Place expected = new Place("desc", "id", substrings, terms, types);
        Place actual = parser.readPlace(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readStatusTest() throws IOException {
        when(parser.readStatus(reader)).thenCallRealMethod();
        when(reader.nextString()).thenReturn("OK", "ZERO_RESULTS", "OVER_QUERY_LIMIT", "REQUEST_DENIED", "INVALID_REQUEST", "skip");
        assertEquals(Status.OK, parser.readStatus(reader));
        assertEquals(Status.ZERO_RESULTS, parser.readStatus(reader));
        assertEquals(Status.OVER_QUERY_LIMIT, parser.readStatus(reader));
        assertEquals(Status.REQUEST_DENIED, parser.readStatus(reader));
        assertEquals(Status.INVALID_REQUEST, parser.readStatus(reader));
        assertNull(parser.readStatus(reader));
    }

    @Test
    public void readTypesArrayTest() throws IOException {
        when(parser.readTypesArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, false);
        when(reader.nextString()).thenReturn("one", "two", "three");
        List<String> expected = Arrays.asList("one", "two", "three");
        List<String> actual = parser.readTypesArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readAspectsArrayTest() throws IOException {
        when(parser.readAspectsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, false, true, false, false);
        when(reader.nextName()).thenReturn("rating", "type", "skip");
        when(reader.nextInt()).thenReturn(1);
        when(reader.nextString()).thenReturn("one");
        List<RatingAspect> expected = Arrays.asList(new RatingAspect(1, "one"), new RatingAspect(-1, null));
        List<RatingAspect> actual = parser.readAspectsArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readReviewsArrayTest() throws IOException {
        when(parser.readReviewsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, true, true, true, true, true, true, false, true, false, false);
        when(reader.nextName()).thenReturn("aspects", "author_name", "author_url", "language", "rating", "text", "time", "skip");
        List<RatingAspect> aspects = Arrays.asList(new RatingAspect(1, "one"), new RatingAspect(-1, null));
        when(parser.readAspectsArray(reader)).thenReturn(aspects);
        when(reader.nextString()).thenReturn("name", "url", "lang", "txt");
        when(reader.nextInt()).thenReturn(1);
        when(reader.nextLong()).thenReturn(10L);
        List<PlaceReview> expected = Arrays.asList(new PlaceReview(aspects, "name", "url", "lang", 1, "txt", 10L), new PlaceReview(null, null, null, null, -1, null, -1L));
        List<PlaceReview> actual = parser.readReviewsArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readAltIdsArrayTest() throws IOException {
        when(parser.readAltIdsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, false, true, false, false);
        when(reader.nextName()).thenReturn("place_id", "scope", "skip");
        when(reader.nextString()).thenReturn("id");
        when(parser.readScope(reader)).thenReturn(PlaceScope.GOOGLE);
        List<AlternativePlaceId> expected = Arrays.asList(new AlternativePlaceId("id", PlaceScope.GOOGLE), new AlternativePlaceId(null, null));
        List<AlternativePlaceId> actual = parser.readAltIdsArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readScopeTest() throws IOException {
        when(parser.readScope(reader)).thenCallRealMethod();
        when(reader.nextString()).thenReturn("APP", "GOOGLE", "skip");
        assertEquals(PlaceScope.APP, parser.readScope(reader));
        assertEquals(PlaceScope.GOOGLE, parser.readScope(reader));
        assertNull(parser.readScope(reader));
    }

    @Test
    public void readPhotosArrayTest() throws IOException {
        when(parser.readPhotosArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, true, false, true, false, false);
        when((reader.nextName())).thenReturn("height", "width", "photo_reference", "skip");
        when(reader.nextInt()).thenReturn(1, 2);
        when(reader.nextString()).thenReturn("photo");
        List<PlacePhoto> expected = Arrays.asList(new PlacePhoto(1, 2, "photo"), new PlacePhoto(-1, -1, null));
        List<PlacePhoto> actual = parser.readPhotosArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readDateTimePairTest() throws IOException {
        when(parser.readDateTimePair(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, false);
        when((reader.nextName())).thenReturn("day", "time", "skip");
        when((reader.nextString())).thenReturn("one", "two");
        DateTimePair expected = new DateTimePair("one", "two");
        DateTimePair actual = parser.readDateTimePair(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readOpenPeriodsArrayTest() throws IOException {
        when(parser.readOpenPeriodsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, false, true, false, false);
        when(reader.nextName()).thenReturn("open", "close", "skip");
        when(parser.readDateTimePair(reader)).thenReturn(new DateTimePair("one", "two"), new DateTimePair("three", "four"));
        List<OpenPeriod> expected = Arrays.asList(new OpenPeriod(new DateTimePair("one", "two"), new DateTimePair("three", "four")), new OpenPeriod(null, null));
        List<OpenPeriod> actual = parser.readOpenPeriodsArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readOpeningHoursTest() throws IOException {
        when(parser.readOpeningHours(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, false);
        when(reader.nextName()).thenReturn("open_now", "periods", "skip");
        when(reader.nextBoolean()).thenReturn(true);
        List<OpenPeriod> periods = Collections.singletonList(new OpenPeriod(new DateTimePair("one", "two"), new DateTimePair("three", "four")));
        when(parser.readOpenPeriodsArray(reader)).thenReturn(periods);
        OpenHours expected = new OpenHours(true, periods);
        OpenHours actual = parser.readOpeningHours(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readGeometryTest() throws IOException {
        when(parser.readGeometry(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, false);
        when(reader.nextName()).thenReturn("lat", "lng", "skip");
        when(reader.nextDouble()).thenReturn(1.0, 2.0);
        PlaceGeometry expected = new PlaceGeometry(new PlaceLocation(1.0, 2.0));
        PlaceGeometry actual = parser.readGeometry(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readAddressComponentTypesArrayTest() throws IOException {
        when(parser.readAddressComponentTypesArray(reader)).thenCallRealMethod();

        // hasNext() should return true N times, where N is the number of AddressComponentTypes
        int numOfTypes = AddressComponentType.values().length;
        Boolean[] hasNextValues = new Boolean[numOfTypes + 1];
        Arrays.fill(hasNextValues, 0, numOfTypes, Boolean.TRUE);
        hasNextValues[numOfTypes] = Boolean.FALSE; // indicate stop
        when(reader.hasNext()).thenReturn(true, hasNextValues); // initial true is for "skip"

        String[] types = new String[] {
                "administrative_area_level_1",
                "administrative_area_level_2",
                "administrative_area_level_3",
                "administrative_area_level_4",
                "administrative_area_level_5",
                "colloquial_area",
                "country",
                "floor",
                "geocode",
                "intersection",
                "locality",
                "natural_feature",
                "neighborhood",
                "political",
                "point_of_interest",
                "post_box",
                "postal_code",
                "postal_code_prefix",
                "postal_code_suffix",
                "postal_town",
                "premise",
                "room",
                "route",
                "street_address",
                "street_number",
                "sublocality",
                "sublocality_level_1",
                "sublocality_level_2",
                "sublocality_level_3",
                "sublocality_level_4",
                "sublocality_level_5",
                "subpremise",
                "transit_station"
        };
        when(reader.nextString()).thenReturn("skip", types);

        List<AddressComponentType> expected = new ArrayList<>();
        for (String type : types) {
            expected.add(AddressComponentType.valueOf(type.toUpperCase()));
        }
        List<AddressComponentType> actual = parser.readAddressComponentTypesArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readAddressComponentsArrayTest() throws IOException {
        when(parser.readAddressComponentsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, true, true, true, true, false, true, false, false);
        when(reader.nextName()).thenReturn("long_name", "short_name", "types", "skip");
        when(reader.nextString()).thenReturn("long", "short");
        List<AddressComponentType> types = Arrays.asList(AddressComponentType.ROOM, AddressComponentType.FLOOR);
        when(parser.readAddressComponentTypesArray(reader)).thenReturn(types);
        List<AddressComponent> expected = Arrays.asList(new AddressComponent("long", "short", types), new AddressComponent(null, null, null));
        List<AddressComponent> actual = parser.readAddressComponentsArray(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readPlaceDetailsTest() throws IOException {
        when(parser.readPlaceDetails(reader)).thenCallRealMethod();

        Boolean[] hasNextValues = new Boolean[20];
        Arrays.fill(hasNextValues, 0, 19, Boolean.TRUE);
        hasNextValues[19] = Boolean.FALSE;
        when(reader.hasNext()).thenReturn(true, hasNextValues);

        String[] names = new String[] {
                "address_components",
                "formatted_address",
                "formatted_phone_number",
                "international_phone_number",
                "geometry",
                "icon",
                "name",
                "place_id",
                "opening_hours",
                "permanently_closed",
                "photos",
                "scope",
                "alt_ids",
                "price_level",
                "rating",
                "reviews",
                "types",
                "url",
                "vicinity"
        };
        when(reader.nextName()).thenReturn("skip", names);

        when(parser.readAddressComponentsArray(reader)).thenReturn(Collections.singletonList(new AddressComponent("long", "short", Collections.singletonList(AddressComponentType.ROOM))));
        when(reader.nextString()).thenReturn("addr", "phone", "intl_phone", "icon", "name", "id", "url", "vic");
        when(parser.readGeometry(reader)).thenReturn(new PlaceGeometry(new PlaceLocation(1.0, 2.0)));
        when(parser.readOpeningHours(reader)).thenReturn(new OpenHours(true, Collections.singletonList(new OpenPeriod(new DateTimePair("one", "two"), new DateTimePair("three", "four")))));
        when(reader.nextBoolean()).thenReturn(true);
        when(parser.readPhotosArray(reader)).thenReturn(Collections.singletonList(new PlacePhoto(1, 2, "ref")));
        when(parser.readScope(reader)).thenReturn(PlaceScope.GOOGLE);
        when(parser.readAltIdsArray(reader)).thenReturn(Collections.singletonList(new AlternativePlaceId("id", PlaceScope.GOOGLE)));
        when(reader.nextInt()).thenReturn(5);
        when(reader.nextDouble()).thenReturn(5.0);
        when(parser.readReviewsArray(reader)).thenReturn(Collections.singletonList(new PlaceReview(Collections.singletonList(new RatingAspect(5, "quality")), "name", "url", "en", 4, "txt", 10L)));
        when(parser.readTypesArray(reader)).thenReturn(Collections.singletonList("type"));

        PlaceDetails expected = new PlaceDetails(Collections.singletonList(new AddressComponent("long", "short", Collections.singletonList(AddressComponentType.ROOM))),
                "addr",
                "phone",
                "intl_phone",
                new PlaceGeometry(new PlaceLocation(1.0, 2.0)),
                "icon",
                "name",
                "id",
                new OpenHours(true, Collections.singletonList(new OpenPeriod(new DateTimePair("one", "two"), new DateTimePair("three", "four")))),
                true,
                Collections.singletonList(new PlacePhoto(1, 2, "ref")),
                PlaceScope.GOOGLE,
                Collections.singletonList(new AlternativePlaceId("id", PlaceScope.GOOGLE)),
                5,
                5.0,
                Collections.singletonList(new PlaceReview(Collections.singletonList(new RatingAspect(5, "quality")), "name", "url", "en", 4, "txt", 10L)),
                Collections.singletonList("type"),
                "url",
                "vic");
        PlaceDetails actual = parser.readPlaceDetails(reader);
        assertEquals(expected, actual);
    }

    @Test
    public void readPredictionsArrayTest() throws IOException {
        when(parser.readPredictionsArray(reader)).thenCallRealMethod();
        when(reader.hasNext()).thenReturn(true, false);
        Place place = new Place("desc", "id", Collections.singletonList(new MatchedSubstring(1, 2)), Collections.singletonList(new DescriptionTerm(3, "three")), Collections.singletonList(PlaceType.GEOCODE));
        when(parser.readPlace(reader)).thenReturn(place);
        List<Place> expected = Collections.singletonList(place);
        List<Place> actual = parser.readPredictionsArray(reader);
        assertEquals(expected, actual);
    }
}
