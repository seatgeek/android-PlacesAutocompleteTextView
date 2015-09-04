package com.seatgeek.placesautocomplete.async;

public interface BackgroundJob<R> {
    public R executeInBackground() throws Exception;

    public void onSuccess(R result);

    public void onFailure(Throwable error);
}
