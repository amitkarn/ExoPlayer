package com.google.android.exoplayer.demo;

import android.app.Application;
import android.os.StatFs;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.exoplayer.demo.player.DemoPlayer;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer.util.Util;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        StatFs stat = new StatFs(getCacheDir().getPath());
        long bytesAvailable;
        if (Util.SDK_INT >= 18) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getBlockCountLong();
        } else {
            bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount();
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new CacheMonitorInterceptor())
                .connectTimeout(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .followSslRedirects(false)
                .cache(new Cache(getCacheDir(), bytesAvailable))
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        DemoPlayer.setOkHttpClient(okHttpClient);
    }
}
