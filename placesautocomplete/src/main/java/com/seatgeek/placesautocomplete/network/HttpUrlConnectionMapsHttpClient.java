package com.seatgeek.placesautocomplete.network;

import android.net.Uri;

import com.seatgeek.placesautocomplete.json.PlacesApiJsonParser;
import com.seatgeek.placesautocomplete.model.PlacesApiException;
import com.seatgeek.placesautocomplete.model.PlacesApiResponse;
import com.seatgeek.placesautocomplete.model.Status;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpUrlConnectionMapsHttpClient extends AbstractPlacesHttpClient {

    HttpUrlConnectionMapsHttpClient(final PlacesApiJsonParser parser) {
        super(parser);
    }

    @Override
    protected <T extends PlacesApiResponse> T executeNetworkRequest(final Uri uri, final ResponseHandler<T> handler) throws IOException {
        URL url = new URL(uri.toString());

        T response = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                is = conn.getInputStream();
                response = handler.handleStreamResult(is);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            if (is != null) {
                is.close();
            }
        }

        Status status = response != null ? response.status : null;

        if (isErrorResponse(response, status)) {
            String err = response != null ? response.error_message : null;
            throw new PlacesApiException(err != null ? err : "Unknown Places Api Error");
        } else {
            return response;
        }
    }

    private static <T extends PlacesApiResponse> boolean isErrorResponse(final T response, final Status status) {
        return response == null || status == null || !status.isSuccessful();
    }
}
