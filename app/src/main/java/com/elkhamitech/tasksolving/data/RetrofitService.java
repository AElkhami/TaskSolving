package com.elkhamitech.tasksolving.data;

import android.content.Context;

import com.elkhamitech.tasksolving.presenter.Utils;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.androidhive.info/pizza/?format=xml";

    public static Retrofit getRetrofitInstance(final Context context){

        //implementing cache logic
        OkHttpClient client = new OkHttpClient
                .Builder()
                //cache size
                .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (Utils.isNetworkAvailable(context)) {
                            // read from cache for 1 minute
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        } else {
                            // tolerate stale
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }
                })
                .build();

        //initialising retrofit
        if(retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }

        return retrofit;
    }



}
