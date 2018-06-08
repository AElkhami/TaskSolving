package com.elkhamitech.tasksolving.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.androidhive.info/pizza/?format=xml";


    public static Retrofit getRetrofitInstance(){

        if(retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
