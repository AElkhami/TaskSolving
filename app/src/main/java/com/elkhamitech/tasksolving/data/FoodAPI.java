package com.elkhamitech.tasksolving.data;

import com.elkhamitech.tasksolving.data.model.FoodResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodAPI {

    //get all the list
    @GET("?format=xml")
    Call<FoodResponse> getFoodData();
}
