package com.google.android.exoplayer.demo;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheMonitorInterceptor implements Interceptor {
    private static final String TAG = CacheMonitorInterceptor.class.getSimpleName();
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Log.d(TAG, "response: " + response);
        Log.d(TAG, "response cache: " + response.cacheResponse());
        Log.d(TAG, "response network: " + response.networkResponse());
        return response;
    }
}
