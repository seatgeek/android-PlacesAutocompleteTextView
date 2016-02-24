package com.seatgeek.placesautocomplete.model;

import java.util.List;

/**
 * A single review for a Place. For instance:
 *      {
 *          "aspects" : [
 *              {
 *                  "rating" : 3,
 *                  "type" : "quality"
 *              }
 *          ],
 *          "author_name" : "Simon Bengtsson",
 *          "author_url" : "https://plus.google.com/104675092887960962573",
 *          "language" : "en",
 *          "rating" : 5,
 *          "text" : "Just went inside to have a look at Google. Amazing.",
 *          "time" : 1338440552869
 *      }
 */
public final class PlaceReview {
    public final List<RatingAspect> aspects;
    public final String author_name;
    public final String author_url;
    public final String language;
    public final int rating;
    public final String text;
    public final long time;

    public PlaceReview(final List<RatingAspect> aspects,
                       final String author_name,
                       final String author_url,
                       final String language,
                       final int rating,
                       final String text,
                       final long time) {
        this.aspects = aspects;
        this.author_name = author_name;
        this.author_url = author_url;
        this.language = language;
        this.rating = rating;
        this.text = text;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceReview)) return false;

        PlaceReview that = (PlaceReview) o;

        if (aspects != null ? !aspects.equals(that.aspects) : that.aspects != null) return false;
        if (author_name != null ? !author_name.equals(that.author_name) : that.author_name != null) return false;
        if (author_url != null ? !author_url.equals(that.author_url) : that.author_url != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (rating != that.rating) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (time != that.time) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rating;
        result = 31 * result + (aspects != null ? aspects.hashCode() : 0);
        result = 31 * result + (author_name != null ? author_name.hashCode() : 0);
        result = 31 * result + (author_url != null ? author_url.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));;
        return result;
    }
}
