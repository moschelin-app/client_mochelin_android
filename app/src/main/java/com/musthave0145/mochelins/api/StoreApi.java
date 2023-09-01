package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.MeetingRes;
import com.musthave0145.mochelins.model.ReviewListRes;
import com.musthave0145.mochelins.model.Store;
import com.musthave0145.mochelins.model.StoreRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface StoreApi {
    @GET("/maps/{storeId}")
    Call<StoreRes> getStoreList(@Header("Authorization") String token,
                             @Path("storeId") int storeId);

    // 특정 리뷰 가저오는 API
    @GET("/review/{reviewId}")
    Call<ReviewListRes> getReviewDetail(@Header("Authorization") String token,
                                        @Path("reviewId") int reviewId);
}
