package com.seatgeek.placesautocomplete.json;

import android.util.JsonWriter;

import com.seatgeek.placesautocomplete.model.DescriptionTerm;
import com.seatgeek.placesautocomplete.model.MatchedSubstring;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndroidPlacesApiJsonWriterTest {
    AndroidPlacesApiJsonParser parser;
    private JsonWriter writer;
    private List<String> actual;

    @Before
    public void setUp() throws IOException {
        parser = new AndroidPlacesApiJsonParser();
        writer = mock(JsonWriter.class);
        actual = new ArrayList<>();

        when(writer.beginArray()).then(new CompositeValueAnswer(BEGIN_ARRAY));
        when(writer.endArray()).then(new CompositeValueAnswer(END_ARRAY));
        when(writer.beginObject()).then(new CompositeValueAnswer(BEGIN_OBJECT));
        when(writer.endObject()).then(new CompositeValueAnswer(END_OBJECT));
        when(writer.value(anyInt())).then(new ValueAnswer());
        when(writer.value(anyBoolean())).then(new ValueAnswer());
        when(writer.value(anyString())).then(new ValueAnswer());
        when(writer.name(anyString())).then(new ValueAnswer());
    }

    @Test
    public void writePlaceTypesArrayTest() throws IOException {
        List<PlaceType> types = Arrays.asList(PlaceType.ROUTE, PlaceType.GEOCODE);
        parser.writePlaceTypesArray(writer, types);
        List<String> expected = Arrays.asList(BEGIN_ARRAY, "route", "geocode", END_ARRAY);
        assertEquals(expected, actual);
    }

    @Test
    public void writeDescriptionTermsArrayTest() throws IOException {
        List<DescriptionTerm> terms = Arrays.asList(new DescriptionTerm(1, "one"), new DescriptionTerm(2, "two"));
        parser.writeDescriptionTermsArray(writer, terms);
        List<String> expected = Arrays.asList(BEGIN_ARRAY, BEGIN_OBJECT, "offset", "1", "value", "one", END_OBJECT, BEGIN_OBJECT, "offset", "2", "value", "two", END_OBJECT, END_ARRAY);
        assertEquals(expected, actual);
    }

    @Test
    public void writeMatchedSubstringsArrayTest() throws IOException {
        List<MatchedSubstring> substrings = Arrays.asList(new MatchedSubstring(1, 2), new MatchedSubstring(3, 4));
        parser.writeMatchedSubstringsArray(writer, substrings);
        List<String> expected = Arrays.asList(BEGIN_ARRAY, BEGIN_OBJECT, "length", "1", "offset", "2", END_OBJECT, BEGIN_OBJECT, "length", "3", "offset", "4", END_OBJECT, END_ARRAY);
        assertEquals(expected, actual);
    }

    @Test
    public void writePlaceTest() throws IOException {
        Place place = new Place("desc",
                "id",
                Collections.singletonList(new MatchedSubstring(1, 2)),
                Collections.singletonList(new DescriptionTerm(3, "three")),
                Collections.singletonList(PlaceType.GEOCODE));
        parser.writePlace(writer, place);
        List<String> expected = Arrays.asList(BEGIN_OBJECT,
                "description", "desc",
                "place_id", "id",
                "matched_substrings", BEGIN_ARRAY, BEGIN_OBJECT, "length", "1", "offset", "2", END_OBJECT, END_ARRAY,
                "terms", BEGIN_ARRAY, BEGIN_OBJECT, "offset", "3", "value", "three", END_OBJECT, END_ARRAY,
                "types", BEGIN_ARRAY, "geocode", END_ARRAY,
                END_OBJECT);
        assertEquals(expected, actual);
    }

    private static String BEGIN_ARRAY = "[";
    private static String END_ARRAY = "]";
    private static String BEGIN_OBJECT = "{";
    private static String END_OBJECT = "}";

    private class CompositeValueAnswer implements Answer<JsonWriter> {
        private String elem;

        public CompositeValueAnswer(String elem) {
            this.elem = elem;
        }

        @Override
        public JsonWriter answer(InvocationOnMock invocation) throws Throwable {
            actual.add(elem);
            return writer;
        }
    }

    private class ValueAnswer implements Answer<JsonWriter> {
        @Override
        public JsonWriter answer(InvocationOnMock invocation) throws Throwable {
            actual.add(invocation.getArguments()[0].toString());
            return writer;
        }
    }
}
