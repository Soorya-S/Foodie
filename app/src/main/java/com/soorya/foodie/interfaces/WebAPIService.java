package com.soorya.foodie.interfaces;

import com.google.gson.JsonObject;
import com.soorya.foodie.model.FoodItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebAPIService {

    String BASE_URL = "https://android-full-time-task.firebaseio.com/";

    @GET("data.json")
    Call<List<FoodItem>> getFoodData();
}