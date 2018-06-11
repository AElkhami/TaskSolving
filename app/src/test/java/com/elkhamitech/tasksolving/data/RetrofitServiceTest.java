package com.elkhamitech.tasksolving.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.elkhamitech.tasksolving.data.model.FoodResponse;
import com.elkhamitech.tasksolving.presenter.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

public class RetrofitServiceTest {

    private MockWebServer mockWebServer;
    private static final String BASE_URL = "https://api.androidhive.info/pizza/?format=xml";
    private OkHttpClient client;
    private Cache cache;
    private int MAX_VALUE = 10 * 1024 * 1024;


    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        cache = new Cache(new File("./build/tmp/test-ok-cache"), MAX_VALUE);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();

    }


    @Test
    public void responseCacheAccessWithOkHttpMember() throws IOException {
        assertSame(cache, getOkHttpClient().cache());
    }

    @Test
    public void getOkHttpClientTest() throws IOException {

        client = getOkHttpClient();
    }


    public OkHttpClient getOkHttpClient() throws IOException {

        client = new OkHttpClient
                .Builder()
                //cache size
                .cache(cache) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        if (Utils.isNetworkAvailable((Context) chain)) {
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

        return client;
    }

    @Test
    public void getRetrofitInstanceTest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url(BASE_URL).toString())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        /*Set a mocking response for retrofit to handle. */
        mockWebServer.enqueue(new MockResponse().setBody(""));

        FoodAPI foodAPI = retrofit.create(FoodAPI.class);

        /* Calling the Service */
        Call<FoodResponse> call = foodAPI.getFoodData();
        assertTrue(call.execute() != null);

        //Finish web server
        mockWebServer.shutdown();


    }
}