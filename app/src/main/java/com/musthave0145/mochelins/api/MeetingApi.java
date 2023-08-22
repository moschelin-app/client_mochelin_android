package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.MeetingListRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MeetingApi {

    // 모임 리스트 가져오는 API
    @GET("/meeting/list")
    Call<MeetingListRes> getMeetingList(@Query("offset") int offset, @Query("limit") int limit,
                                        @Query("lat") double lat, @Query("lng") double lng,
                                        @Query("dis") double dis);

    // 특정 모임의 상세내용 가져오는 API
    @GET("/meeting/{meetingId}")
    Call<MeetingListRes> getMeetingDetail(@Path("meetingId") int meetingId);

}
