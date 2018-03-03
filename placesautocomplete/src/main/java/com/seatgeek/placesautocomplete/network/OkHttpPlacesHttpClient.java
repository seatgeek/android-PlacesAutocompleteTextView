package com.seatgeek.placesautocomplete.network;

import android.net.Uri;
import android.util.Log;

import com.seatgeek.placesautocomplete.Constants;
import com.seatgeek.placesautocomplete.json.PlacesApiJsonParser;
import com.seatgeek.placesautocomplete.model.PlacesApiException;
import com.seatgeek.placesautocomplete.model.PlacesApiResponse;
import com.seatgeek.placesautocomplete.model.Status;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class OkHttpPlacesHttpClient extends AbstractPlacesHttpClient {
    private final OkHttpClient okHttpClient;

    OkHttpPlacesHttpClient(PlacesApiJsonParser parser) {
        super(parser);

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
                .build();
    }

    @Override
    protected <T extends PlacesApiResponse> T executeNetworkRequest(final Uri uri, final ResponseHandler<T> responseHandler) throws IOException {
        final Request request = new Request.Builder()
                .url(uri.toString())
                .build();

        Response response = okHttpClient.newCall(request).execute();

        try {
            T body = responseHandler.handleStreamResult(response.body().byteStream());
            Status status = body.status;
            if (status != null && !status.isSuccessful()) {
                String err = body.error_message;
                throw new PlacesApiException(err != null ? err : "Unknown Places Api Error");
            } else {
                return body;
            }
        } finally {
            if (response != null) {
                try {
                    response.body().close();
                } catch (Exception e) {
                    Log.w(Constants.LOG_TAG, "Exception Closing Response body..", e);
                }
            }
        }
    }
}
