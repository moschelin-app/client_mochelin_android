package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.ReviewRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ReviewApi {

//    // 리뷰 작성 API
//    @Multipart
//    @POST("/review/add")
//    Call<ReviewRes> addReview(@Header("Authorization") String token,
//                              @Part MultipartBody.Part photo,
//                              @Part("content") RequestBody content,
//                              @Part("storeName") RequestBody storeName,
//                              @Part("storeLat") RequestBody storeLat,
//                              @Part("storeLng") RequestBody storeLng);
}
