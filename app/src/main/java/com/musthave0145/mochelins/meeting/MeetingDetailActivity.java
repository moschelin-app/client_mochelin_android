package com.musthave0145.mochelins.meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.MeetingApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.MeetingListRes;
import com.musthave0145.mochelins.model.MeetingRes;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Header;

public class MeetingDetailActivity extends AppCompatActivity {


//    ViewPager2 viewPager2;
//    int[] images= {R.drawable.avatar1,R.drawable.avatar2,R.drawable.smile2,R.drawable.balloon};
//    ItemAdapter itemAdapter;

    Integer[] imgProfiles = {R.id.imgProfile, R.id.imgProfile1, R.id.imgProfile2, R.id.imgProfile3,
            R.id.imgProfile4, R.id.imgProfile5, R.id.imgProfile6, R.id.imgProfile7};

    CircleImageView[] imgProfileList = new CircleImageView[imgProfiles.length];

    ImageView imgPhoto;
    ImageView imgBack;


    Integer[] textViews = {R.id.txtPersonName, R.id.txtContent, R.id.txtMeetCount, R.id.txtStoreName,
                            R.id.txtMaximum, R.id.txtDutch, R.id.txtMeetingDate, R.id.txtStoreAddress};

    TextView[] textViewsList = new TextView[textViews.length];

    Meeting meeting;

    MapView mapView;
    Button btnApply;

    int meetingId;
    String token;




    // TODO: 구글맵을 셋팅하자 (마커를 찍게 했지만, 완전한 것 아님)
    // TODO: 음식점의 주소를 받아와야 한다.
    // TODO: 본인이 올린 모임글이면, 프로필 사진 오른쪽에 3점메뉴버튼을 표시하고, 수정과 삭제 메뉴를 표시해야 한다.


    // 모임참가 취소 API
    void cancleMeeting() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(MeetingDetailActivity.this);
        MeetingApi api = retrofit.create(MeetingApi.class);

        Call<MeetingRes> call = api.cancleMeeting("Bearer "+token, meetingId);
        call.enqueue(new Callback<MeetingRes>() {
            @Override
            public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
                if (response.isSuccessful()){
                    Snackbar.make(btnApply, "모임참가 취소가 되었습니다.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(btnApply, "문제가 발생하였습니다.", Snackbar.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<MeetingRes> call, Throwable t) {

            }
        });
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        meetingId = getIntent().getIntExtra("meetingId",0);
//        meeting = (Meeting) getIntent().getSerializableExtra("meeting");
//        int meetingId = meeting.id;
        for (int i = 0; i < imgProfiles.length ; i++) {
            imgProfileList[i] = findViewById(imgProfiles[i]);}

        for (int i = 0; i < textViews.length ; i++) {
            textViewsList[i] = findViewById(textViews[i]);
        }
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        imgPhoto = findViewById(R.id.imgPhoto);
        imgBack = findViewById(R.id.imgBack);
        btnApply = findViewById(R.id.btnApply);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getNetworkData();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 미팅을 참가하고 또 버튼을 눌렀을 때의 처리.(중복 추가를 방지하기위해서는 어떻게 할까)
                // TODO: 다시 누르면 모임참가 취소 API가 실행되도록 처리해보자!!!
                // TODO: 내가 신청을 안했거나 신청했다면, 버튼의 글씨가 "모임참가"로, 신청이 되어 있다면 "모임 참가 취소"로!!
                Retrofit retrofit = NetworkClient.getRetrofitClient(MeetingDetailActivity.this);
                MeetingApi api = retrofit.create(MeetingApi.class);

                Call<MeetingRes> call = api.attendMeeting("Bearer "+token, meetingId);
                call.enqueue(new Callback<MeetingRes>() {
                    @Override
                    public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
                        if (response.isSuccessful()){
                            Snackbar.make(btnApply,"모임 참가 신청이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            getNetworkData();
                        } else {
                            Snackbar.make(btnApply,"문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MeetingRes> call, Throwable t) {

                    }
                });
//                cancleMeeting();

            }
        });




        }

    void getNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(MeetingDetailActivity.this);
        MeetingApi api = retrofit.create(MeetingApi.class);

        Call<MeetingRes> call = api.getMeetingDetail(meetingId);
        call.enqueue(new Callback<MeetingRes>() {
            @Override
            public void onResponse(Call<MeetingRes> call, Response<MeetingRes> response) {
                if (response.isSuccessful()){
                    meeting = response.body().item;
                    Log.i("디테일액티비티", meeting.content);

                    SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                    token = sp.getString(Config.ACCESS_TOKEN, "");
                    //Todo: 내 계시물인지 알수있는 방법이 없음.

                    textViewsList[0].setText(meeting.nickname);

                    textViewsList[1].setText(meeting.content);


                    String count = meeting.attend + "/" + meeting.maximum;

                    Glide.with(MeetingDetailActivity.this).load(meeting.photo).into(imgPhoto);

                    Glide.with(MeetingDetailActivity.this).load(meeting.profile).into(imgProfileList[0]);





                    textViewsList[2].setText(count);

                    textViewsList[3].setText("  "+meeting.storeName+"  ");

                    textViewsList[4].setText("최대 " + meeting.maximum + "명 참여 가능");

                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng storeLatLng = new LatLng(meeting.storeLat, meeting.storeLng);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 16));

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(storeLatLng).title(meeting.storeName);
                            googleMap.addMarker(markerOptions).setTag(0);

                        }
                    });
                    //         datetime형식을 우리가 보기좋게 바꾸자!!!
                    String newDate = "";

                    try {
                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = inputDateFormat.parse(meeting.date);

                        // 날짜 형식을 변경
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("M월 d일 (E) HH:mm", Locale.KOREA);

                        // Calendar 객체를 사용하여 요일을 얻음
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREA);

                        // 변경된 날짜 형식 출력
                        String formattedDate = outputDateFormat.format(date);
                        newDate = formattedDate.replace("요일", dayOfWeek);
                    } catch (ParseException e) {
                        e.printStackTrace();

                    }
                    textViewsList[6].setText(newDate);
                    // 주소 세팅
//                    textViewsList[7].setText();

                    // 모임참가 인원 세팅

//                    Glide.with(MeetingDetailActivity.this).load(meeting.profiles.get(0)).into(imgProfileList[0]);
//                    imgProfileList[0].setVisibility(View.VISIBLE);
                    for (int i = 0 ; i < meeting.profiles.size(); i++) {
                        Log.i("테스트", meeting.profiles.get(i).toString());
                    }

                    for (int i = 0; i < meeting.profiles.size(); i++ ){
                        imgProfileList[i+1].setVisibility(View.VISIBLE);
                        Glide.with(MeetingDetailActivity.this).load(meeting.profiles.get(i)).into(imgProfileList[i+1]);
                    }



                } else {

                }
            }

            @Override
            public void onFailure(Call<MeetingRes> call, Throwable t) {

            }
        });
    }
}
        // 미팅 사진은 1개만,, 리뷰사진은 5개..
//        viewPager2 = findViewById(R.id.viewPager2);
//        itemAdapter = new ItemAdapter(images);
//        viewPager2.setAdapter(itemAdapter);
//        imgPhoto = findViewById(R.id.imgPhoto);
//        Glide.with(MeetingDetailActivity.this).load(meeting.photo).into(imgPhoto);
