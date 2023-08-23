package com.musthave0145.mochelins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.musthave0145.mochelins.adapter.ItemAdapter;
import com.musthave0145.mochelins.model.Meeting;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeetingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {


//    ViewPager2 viewPager2;
//    int[] images= {R.drawable.avatar1,R.drawable.avatar2,R.drawable.smile2,R.drawable.balloon};
//    ItemAdapter itemAdapter;

    Integer[] imgProfiles = {R.id.imgProfile, R.id.imgProfile1, R.id.imgProfile2, R.id.imgProfile3,
            R.id.imgProfile4, R.id.imgProfile5, R.id.imgProfile6, R.id.imgProfile7};

    CircleImageView[] imgProfileList = new CircleImageView[imgProfiles.length];

    ImageView imgPhoto;

    Integer[] textViews = {R.id.txtPersonName, R.id.txtContent, R.id.txtMeetCount, R.id.txtStoreName,
                            R.id.txtMaximum, R.id.txtDutch, R.id.txtAge, R.id.txtMeetingDate, R.id.txtStoreAddress};

    TextView[] textViewsList = new TextView[textViews.length];

    Meeting meeting;

    MapView mapView;

    // TODO: 구글맵을 셋팅하자



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        meeting = (Meeting) getIntent().getSerializableExtra("meeting");
        // TODO: 이 방법 말고, 특정 게시물의 아이디를 가져와 이쪽에서 다시 요청하는 것으로 바꿔보자!!!


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(MeetingDetailActivity.this);


        for (int i = 0; i < imgProfiles.length ; i++) {
            imgProfileList[i] = findViewById(imgProfiles[i]);}

        for (int i = 0; i < textViews.length ; i++) {
            textViewsList[i] = findViewById(textViews[i]);
        }

        // 미팅 사진은 1개만,, 리뷰사진은 5개..
//        viewPager2 = findViewById(R.id.viewPager2);
//        itemAdapter = new ItemAdapter(images);
//        viewPager2.setAdapter(itemAdapter);
        imgPhoto = findViewById(R.id.imgPhoto);
        Glide.with(MeetingDetailActivity.this).load(meeting.photo).into(imgPhoto);



        textViewsList[1].setText(meeting.content);

        String count = meeting.attend + "/" + meeting.maximum;

        textViewsList[2].setText(count);

        textViewsList[3].setText(" "+meeting.storeName+" ");

        textViewsList[4].setText("최대 " + meeting.maximum + "명 참여 가능");

        // datetime형식을 우리가 보기좋게 바꾸자!!!

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
        textViewsList[7].setText(newDate);



        }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng myLocation = new LatLng(meeting.storeLat, meeting.storeLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
        googleMap.addMarker(new MarkerOptions().position(myLocation).title(meeting.storeName));

    }
}