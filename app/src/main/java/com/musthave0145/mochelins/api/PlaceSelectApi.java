package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.PlaceSelectList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceSelectApi {

    @GET("/maps/api/place/nearbysearch/json")
    Call<PlaceSelectList> getPlaceSelectList(@Query("language") String language,
                                       @Query("location") String location,
                                       @Query("radius") int radius,
                                       @Query("key") String key,
                                       @Query("keyword") String keyword);
}
