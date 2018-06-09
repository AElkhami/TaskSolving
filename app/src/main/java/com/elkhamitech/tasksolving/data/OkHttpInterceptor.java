package com.elkhamitech.tasksolving.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.elkhamitech.tasksolving.presenter.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class OkHttpInterceptor implements Interceptor {

    private Context context;

    OkHttpInterceptor(Context context){
        this.context = context;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (Utils.isNetworkAvailable(context)) {
            int maxAge = 60; // read from cache for 1 minute
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

    }

}
