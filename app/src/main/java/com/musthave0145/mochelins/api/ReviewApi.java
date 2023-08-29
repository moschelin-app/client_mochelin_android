package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.ReviewRes;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ReviewApi {

    // 리뷰를 가져오는 API
    @GET("/review/list")
    Call<ReviewRes> getReviewList(@Header("Authorization") String token,
                                  @Query("offset") int offset, @Query("limit") int limit,
                                  @Query("lat") double lat, @Query("lng") double lng,
                                  @Query("dis") double dis);

    // 리뷰 작성 API (최종)
    @Multipart
    @POST("/review/add")
    Call<ReviewRes> addReview(@Header("Authorization") String token,
                              @Part MultipartBody.Part photo,
                              @Part("content") RequestBody content,
                              @Part("storeName") RequestBody storeName,
                              @Part("storeLat") RequestBody storeLat,
                              @Part("storeLng") RequestBody storeLng,
                              @Part("rating") RequestBody rating,
                              @Part("tag") RequestBody tag
    );
}
