package com.seatgeek.placesautocomplete.json;

import android.util.JsonReader;
import android.util.JsonWriter;

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
import com.seatgeek.placesautocomplete.model.PlacesAutocompleteResponse;
import com.seatgeek.placesautocomplete.model.PlacesDetailsResponse;
import com.seatgeek.placesautocomplete.model.RatingAspect;
import com.seatgeek.placesautocomplete.model.Status;
import com.seatgeek.placesautocomplete.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

class AndroidPlacesApiJsonParser implements PlacesApiJsonParser {

    @Override
    public PlacesAutocompleteResponse autocompleteFromStream(final InputStream is) throws JsonParsingException {
        JsonReader reader = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            reader = new JsonReader(bufferedReader);

            List<Place> predictions = null;
            Status status = null;
            String errorMessage = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "predictions":
                        predictions = readPredictionsArray(reader);
                        break;
                    case "status":
                        status = readStatus(reader);
                        break;
                    case "error_message":
                        errorMessage = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return new PlacesAutocompleteResponse(status, errorMessage, predictions);
        } catch (Exception e) {
            throw new JsonParsingException(e);
        } finally {
            ResourceUtils.closeResourceQuietly(reader);
        }
    }

    @Override
    public PlacesDetailsResponse detailsFromStream(final InputStream is) throws JsonParsingException {
        JsonReader reader = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            reader = new JsonReader(bufferedReader);

            PlaceDetails result = null;
            Status status = null;
            String errorMessage = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "result":
                        result = readPlaceDetails(reader);
                        break;
                    case "status":
                        status = readStatus(reader);
                        break;
                    case "error_message":
                        errorMessage = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return new PlacesDetailsResponse(status, errorMessage, result);
        } catch (Exception e) {
            throw new JsonParsingException(e);
        } finally {
            ResourceUtils.closeResourceQuietly(reader);
        }
    }

    @Override
    public List<Place> readHistoryJson(final InputStream in) throws JsonParsingException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            List<Place> places = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                places.add(readPlace(reader));
            }
            reader.endArray();
            reader.close();
            return places;
        } catch (Exception e) {
            throw new JsonParsingException(e);
        } finally {
            ResourceUtils.closeResourceQuietly(reader);
        }
    }

    @Override
    public void writeHistoryJson(final OutputStream os, final List<Place> places) throws JsonWritingException {
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.setIndent("  ");
            writer.beginArray();
            for (Place place : places) {
                writePlace(writer, place);
            }
            writer.endArray();
            writer.close();
        } catch (Exception e) {
            throw new JsonWritingException(e);
        } finally {
            ResourceUtils.closeResourceQuietly(writer);
        }
    }

    void writePlace(JsonWriter writer, Place place) throws IOException {
        writer.beginObject();
        writer.name("description").value(place.description);
        writer.name("place_id").value(place.place_id);
        writer.name("matched_substrings");
        writeMatchedSubstringsArray(writer, place.matched_substrings);
        writer.name("terms");
        writeDescriptionTermsArray(writer, place.terms);
        writer.name("types");
        writePlaceTypesArray(writer, place.types);
        writer.endObject();
    }

    void writeMatchedSubstringsArray(JsonWriter writer, List<MatchedSubstring> matchedSubstrings) throws IOException {
        writer.beginArray();
        for (MatchedSubstring matchedSubstring : matchedSubstrings) {
            writer.beginObject();
            writer.name("length").value(matchedSubstring.length);
            writer.name("offset").value(matchedSubstring.offset);
            writer.endObject();
        }
        writer.endArray();
    }

    void writeDescriptionTermsArray(JsonWriter writer, List<DescriptionTerm> descriptionTerms) throws IOException {
        writer.beginArray();
        for (DescriptionTerm term : descriptionTerms) {
            writer.beginObject();
            writer.name("offset").value(term.offset);
            writer.name("value").value(term.value);
            writer.endObject();
        }
        writer.endArray();
    }

    void writePlaceTypesArray(JsonWriter writer, List<PlaceType> placeTypes) throws IOException {
        writer.beginArray();
        for (PlaceType type : placeTypes) {
            switch (type) {
                case ROUTE:
                    writer.value("route");
                    break;
                case GEOCODE:
                    writer.value("geocode");
                    break;
            }
        }
        writer.endArray();
    }

    List<Place> readPredictionsArray(JsonReader reader) throws IOException {
        List<Place> predictions = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            predictions.add(readPlace(reader));
        }
        reader.endArray();
        return predictions;
    }

    @SuppressWarnings("ConstantConditions")
    PlaceDetails readPlaceDetails(JsonReader reader) throws IOException {
        List<AddressComponent> addressComponents = null;
        String formattedAddress = null;
        String formattedPhoneNumber = null;
        String internationalPhoneNumber = null;
        PlaceGeometry geometry = null;
        String icon = null;
        String name = null;
        String placeId = null;
        OpenHours openingHours = null;
        boolean permanentlyClosed = false;
        List<PlacePhoto> photos = null;
        PlaceScope scope = null;
        List<AlternativePlaceId> altIds = null;
        int priceLevel = -1;
        double rating = -1.0;
        List<PlaceReview> reviews = null;
        List<String> types = null;
        String url = null;
        String vicinity = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "address_components":
                    addressComponents = readAddressComponentsArray(reader);
                    break;
                case "formatted_address":
                    formattedAddress = reader.nextString();
                    break;
                case "formatted_phone_number":
                    formattedPhoneNumber = reader.nextString();
                    break;
                case "international_phone_number":
                    internationalPhoneNumber = reader.nextString();
                    break;
                case "geometry":
                    geometry = readGeometry(reader);
                    break;
                case "icon":
                    icon = reader.nextString();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "place_id":
                    placeId = reader.nextString();
                    break;
                case "opening_hours":
                    openingHours = readOpeningHours(reader);
                    break;
                case "permanently_closed":
                    permanentlyClosed = reader.nextBoolean();
                    break;
                case "photos":
                    photos = readPhotosArray(reader);
                    break;
                case "scope":
                    scope = readScope(reader);
                    break;
                case "alt_ids":
                    altIds = readAltIdsArray(reader);
                    break;
                case "price_level":
                    priceLevel = reader.nextInt();
                    break;
                case "rating":
                    rating = reader.nextDouble();
                    break;
                case "reviews":
                    reviews = readReviewsArray(reader);
                    break;
                case "types":
                    types = readTypesArray(reader);
                    break;
                case "url":
                    url = reader.nextString();
                    break;
                case "vicinity":
                    vicinity = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new PlaceDetails(addressComponents,
                formattedAddress,
                formattedPhoneNumber,
                internationalPhoneNumber,
                geometry,
                icon,
                name,
                placeId,
                openingHours,
                permanentlyClosed,
                photos,
                scope,
                altIds,
                priceLevel,
                rating,
                reviews,
                types,
                url,
                vicinity);
    }

    List<AddressComponent> readAddressComponentsArray(JsonReader reader) throws IOException {
        List<AddressComponent> addressComponents = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            String longName = null;
            String shortName = null;
            List<AddressComponentType> types = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "long_name":
                        longName = reader.nextString();
                        break;
                    case "short_name":
                        shortName = reader.nextString();
                        break;
                    case "types":
                        types = readAddressComponentTypesArray(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            addressComponents.add(new AddressComponent(longName, shortName, types));
        }
        reader.endArray();
        return addressComponents;
    }

    List<AddressComponentType> readAddressComponentTypesArray(JsonReader reader) throws IOException {
        List<AddressComponentType> types = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            switch (reader.nextString()) {
                case "administrative_area_level_1":
                    types.add(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
                    break;
                case "administrative_area_level_2":
                    types.add(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2);
                    break;
                case "administrative_area_level_3":
                    types.add(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_3);
                    break;
                case "administrative_area_level_4":
                    types.add(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_4);
                    break;
                case "administrative_area_level_5":
                    types.add(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_5);
                    break;
                case "colloquial_area":
                    types.add(AddressComponentType.COLLOQUIAL_AREA);
                    break;
                case "country":
                    types.add(AddressComponentType.COUNTRY);
                    break;
                case "floor":
                    types.add(AddressComponentType.FLOOR);
                    break;
                case "geocode":
                    types.add(AddressComponentType.GEOCODE);
                    break;
                case "intersection":
                    types.add(AddressComponentType.INTERSECTION);
                    break;
                case "locality":
                    types.add(AddressComponentType.LOCALITY);
                    break;
                case "natural_feature":
                    types.add(AddressComponentType.NATURAL_FEATURE);
                    break;
                case "neighborhood":
                    types.add(AddressComponentType.NEIGHBORHOOD);
                    break;
                case "political":
                    types.add(AddressComponentType.POLITICAL);
                    break;
                case "point_of_interest":
                    types.add(AddressComponentType.POINT_OF_INTEREST);
                    break;
                case "post_box":
                    types.add(AddressComponentType.POST_BOX);
                    break;
                case "postal_code":
                    types.add(AddressComponentType.POSTAL_CODE);
                    break;
                case "postal_code_prefix":
                    types.add(AddressComponentType.POSTAL_CODE_PREFIX);
                    break;
                case "postal_code_suffix":
                    types.add(AddressComponentType.POSTAL_CODE_SUFFIX);
                    break;
                case "postal_town":
                    types.add(AddressComponentType.POSTAL_TOWN);
                    break;
                case "premise":
                    types.add(AddressComponentType.PREMISE);
                    break;
                case "room":
                    types.add(AddressComponentType.ROOM);
                    break;
                case "route":
                    types.add(AddressComponentType.ROUTE);
                    break;
                case "street_address":
                    types.add(AddressComponentType.STREET_ADDRESS);
                    break;
                case "street_number":
                    types.add(AddressComponentType.STREET_NUMBER);
                    break;
                case "sublocality":
                    types.add(AddressComponentType.SUBLOCALITY);
                    break;
                case "sublocality_level_1":
                    types.add(AddressComponentType.SUBLOCALITY_LEVEL_1);
                    break;
                case "sublocality_level_2":
                    types.add(AddressComponentType.SUBLOCALITY_LEVEL_2);
                    break;
                case "sublocality_level_3":
                    types.add(AddressComponentType.SUBLOCALITY_LEVEL_3);
                    break;
                case "sublocality_level_4":
                    types.add(AddressComponentType.SUBLOCALITY_LEVEL_4);
                    break;
                case "sublocality_level_5":
                    types.add(AddressComponentType.SUBLOCALITY_LEVEL_5);
                    break;
                case "subpremise":
                    types.add(AddressComponentType.SUBPREMISE);
                    break;
                case "transit_station":
                    types.add(AddressComponentType.TRANSIT_STATION);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endArray();
        return types;
    }

    PlaceGeometry readGeometry(JsonReader reader) throws IOException {
        double lat = -1.0;
        double lng = -1.0;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "lat":
                    lat = reader.nextDouble();
                    break;
                case "lng":
                    lng = reader.nextDouble();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new PlaceGeometry(new PlaceLocation(lat, lng));
    }

    OpenHours readOpeningHours(JsonReader reader) throws IOException {
        boolean openNow = false;
        List<OpenPeriod> periods = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "open_now":
                    openNow = reader.nextBoolean();
                    break;
                case "periods":
                    periods = readOpenPeriodsArray(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new OpenHours(openNow, periods);
    }

    List<OpenPeriod> readOpenPeriodsArray(JsonReader reader) throws IOException {
        List<OpenPeriod> periods = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            DateTimePair open = null;
            DateTimePair close = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "open":
                        open = readDateTimePair(reader);
                        break;
                    case "close":
                        close = readDateTimePair(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            periods.add(new OpenPeriod(open, close));
        }
        reader.endArray();
        return periods;
    }

    DateTimePair readDateTimePair(JsonReader reader) throws IOException {
        String day = null;
        String time = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "day":
                    day = reader.nextString();
                    break;
                case "time":
                    time = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new DateTimePair(day, time);
    }

    List<PlacePhoto> readPhotosArray(JsonReader reader) throws IOException {
        List<PlacePhoto> photos = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            int height = -1;
            int width = -1;
            String photoReference = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "height":
                        height = reader.nextInt();
                        break;
                    case "width":
                        width = reader.nextInt();
                        break;
                    case "photo_reference":
                        photoReference = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            photos.add(new PlacePhoto(height, width, photoReference));
        }
        reader.endArray();
        return photos;
    }

    PlaceScope readScope(JsonReader reader) throws IOException {
        switch (reader.nextString()) {
            case "APP":
                return PlaceScope.APP;
            case "GOOGLE":
                return PlaceScope.GOOGLE;
            default:
                // not possible based on API spec:
                // https://developers.google.com/places/web-service/details#PlaceDetailsResults
                return null;
        }
    }

    List<AlternativePlaceId> readAltIdsArray(JsonReader reader) throws IOException {
        List<AlternativePlaceId> altIds = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            String placeId = null;
            PlaceScope scope = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "place_id":
                        placeId = reader.nextString();
                        break;
                    case "scope":
                        scope = readScope(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            altIds.add(new AlternativePlaceId(placeId, scope));
        }
        reader.endArray();
        return altIds;
    }

    List<PlaceReview> readReviewsArray(JsonReader reader) throws IOException {
        List<PlaceReview> reviews = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            List<RatingAspect> aspects = null;
            String authorName = null;
            String authorUrl = null;
            String language = null;
            int rating = -1;
            String text = null;
            long time = -1L;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "aspects":
                        aspects = readAspectsArray(reader);
                        break;
                    case "author_name":
                        authorName = reader.nextString();
                        break;
                    case "author_url":
                        authorUrl = reader.nextString();
                        break;
                    case "language":
                        language = reader.nextString();
                        break;
                    case "rating":
                        rating = reader.nextInt();
                        break;
                    case "text":
                        text = reader.nextString();
                        break;
                    case "time":
                        time = reader.nextLong();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            reviews.add(new PlaceReview(aspects, authorName, authorUrl, language, rating, text, time));
        }
        reader.endArray();
        return reviews;
    }

    List<RatingAspect> readAspectsArray(JsonReader reader) throws IOException {
        List<RatingAspect> aspects = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            int rating = -1;
            String type = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "rating":
                        rating = reader.nextInt();
                        break;
                    case "type":
                        type = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            aspects.add(new RatingAspect(rating, type));
        }
        reader.endArray();
        return aspects;
    }

    List<String> readTypesArray(JsonReader reader) throws IOException {
        List<String> types = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            types.add(reader.nextString());
        }
        reader.endArray();
        return types;
    }

    Status readStatus(JsonReader reader) throws IOException {
        switch (reader.nextString()) {
            case "OK":
                return Status.OK;
            case "ZERO_RESULTS":
                return Status.ZERO_RESULTS;
            case "OVER_QUERY_LIMIT":
                return Status.OVER_QUERY_LIMIT;
            case "REQUEST_DENIED":
                return Status.REQUEST_DENIED;
            case "INVALID_REQUEST":
                return Status.INVALID_REQUEST;
            default:
                return null;
        }
    }

    Place readPlace(JsonReader reader) throws IOException {
        String description = null;
        String placeId = null;
        List<MatchedSubstring> matchedSubstrings = null;
        List<DescriptionTerm> terms = null;
        List<PlaceType> types = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "description":
                    description = reader.nextString();
                    break;
                case "place_id":
                    placeId = reader.nextString();
                    break;
                case "matched_substrings":
                    matchedSubstrings = readMatchedSubstringsArray(reader);
                    break;
                case "terms":
                    terms = readDescriptionTermsArray(reader);
                    break;
                case "types":
                    types = readPlaceTypesArray(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Place(description, placeId, matchedSubstrings, terms, types);
    }

    List<MatchedSubstring> readMatchedSubstringsArray(JsonReader reader) throws IOException {
        List<MatchedSubstring> matchedSubstrings = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            int length = -1;
            int offset = -1;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "length":
                        length = reader.nextInt();
                        break;
                    case "offset":
                        offset = reader.nextInt();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            matchedSubstrings.add(new MatchedSubstring(length, offset));
        }
        reader.endArray();
        return matchedSubstrings;
    }

    List<DescriptionTerm> readDescriptionTermsArray(JsonReader reader) throws IOException {
        List<DescriptionTerm> terms = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            int offset = -1;
            String value = null;

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "offset":
                        offset = reader.nextInt();
                        break;
                    case "value":
                        value = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            terms.add(new DescriptionTerm(offset, value));
        }
        reader.endArray();
        return terms;
    }

    List<PlaceType> readPlaceTypesArray(JsonReader reader) throws IOException {
        List<PlaceType> types = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            switch (reader.nextString()) {
                case "route":
                    types.add(PlaceType.ROUTE);
                    break;
                case "geocode":
                    types.add(PlaceType.GEOCODE);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endArray();
        return types;
    }
}
