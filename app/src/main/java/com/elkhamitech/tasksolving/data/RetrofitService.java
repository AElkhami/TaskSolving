package com.elkhamitech.tasksolving.data;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.androidhive.info/pizza/?format=xml";
    private static OkHttpInterceptor interceptor;


    public static Retrofit getRetrofitInstance(Context context){

        interceptor = new OkHttpInterceptor(context);

        //setup cache
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        //add cache to the client
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build();

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
