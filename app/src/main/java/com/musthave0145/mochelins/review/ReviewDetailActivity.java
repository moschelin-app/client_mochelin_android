package com.musthave0145.mochelins.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.ItemAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.model.ReviewRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewDetailActivity extends AppCompatActivity {


    // TODO: 댓글 기능은 물어보거나 더 찾아봐야 할 것 같음.

    // 사진 여러장 넘기면서 보여주는 라이브러리
    ViewPager2 viewPager2;
    ArrayList<String> imgUrls = new ArrayList<>();
    ItemAdapter adapter;
    LinearLayout indicatorLayout;


    MapView mapView;

    CircleImageView imgProfile;

    Integer[] imgViews = {R.id.imgBack, R.id.imgShare, R.id.imgLike,
            R.id.imgStar1, R.id.imgStar2, R.id.imgStar3, R.id.imgStar4, R.id.imgStar5};

    ImageView[] imgViewList = new ImageView[imgViews.length];

    ImageView imgMyButton;

    Integer[] txtViews = {R.id.txtStoreName, R.id.txtLike, R.id.txtStar,
                        R.id.txtPersonName, R.id.txtCreatedAt, R.id.txtMapContent, R.id.txtTag1,
                        R.id.txtTag2, R.id.txtTag3, R.id.txtViews, R.id.txtAddress, R.id.txtDetailStore};
    TextView[] txtViewList = new TextView[txtViews.length];
    String token;

    int reviewId = 0;

    RecyclerView recyclerView;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        viewPager2 = findViewById(R.id.viewPager2);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        imgProfile = findViewById(R.id.imgProfile);
        indicatorLayout = findViewById(R.id.indicatorLayout);
        imgMyButton = findViewById(R.id.imgMyButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewDetailActivity.this));



        for(int i = 0; i < imgViews.length; i++) {
            imgViewList[i] = findViewById(imgViews[i]);
        }

        for(int i = 0; i < txtViews.length; i++) {
            txtViewList[i] = findViewById(txtViews[i]);
        }

        Review review = (Review) getIntent().getSerializableExtra("review");
        reviewId = review.id;

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewRes> call = api.getReviewDetail("Bearer " + token, reviewId);
        call.enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                Review review1 = response.body().item;

                // 내 게시물인지 알 수 있는 방법이 없음.
                imgMyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ReviewDetailActivity.this, ReviewUpdateActivity.class);
                        intent.putExtra("review", review1);
                        startActivity(intent);

                    }
                });

                imgViewList[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                // 사진 여러장 보여주기
                for (int i = 0; i < review1.photos.size(); i++) {
                    imgUrls.add(review1.photos.get(i).photo);
                }
                adapter = new ItemAdapter(ReviewDetailActivity.this, imgUrls);
                viewPager2.setAdapter(adapter);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        updateIndicator(position);
                    }
                });

                // 가게 이름
                txtViewList[0].setText(review1.storeName);

                // 내가 좋아요 한 것 처리
                if (review1.isLike == 1) {
                    imgViewList[2].setImageResource(R.drawable.baseline_favorite_24);
                } else {
                    imgViewList[2].setImageResource(R.drawable.baseline_favorite_border_24_2);
                }

                // 좋아요 수
                txtViewList[1].setText(review1.likeCnt+"");

                // 별점 수 대로 별 보여주기
                for (int i = 0; i < 5; i++) {
                    if (i < review1.rating) {
                        imgViewList[i+3].setImageResource(R.drawable.baseline_star_24);
                    }
                }
                // 별점 처리
                txtViewList[2].setText(review1.rating+"");

                // 작성자
                Glide.with(ReviewDetailActivity.this).load(review1.profile).into(imgProfile);
                txtViewList[3].setText(review1.nickname);

                //  작성시간 가공해서 보여주기
                // 표준 시간 형식을 SimpleDateFormat을 사용하여 변환
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yy.MM.dd HH:mm");

                // 시간대를 한국 시간대로 변경
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
                inputFormat.setTimeZone(timeZone);
                outputFormat.setTimeZone(timeZone);

                String createdAt = "";
                try {
                    Date date = inputFormat.parse(review1.createdAt);
                    createdAt = outputFormat.format(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txtViewList[4].setText(createdAt);

                txtViewList[5].setText(review1.content);

                // 태그가 달린 만큼 보여주자.
                for(int i = 0; i < review1.tags.size(); i++){
                    if(i >= 3){
                        break;
                    }
                    txtViewList[i+6].setVisibility(View.VISIBLE);
                    txtViewList[i+6].setText("#" + review1.tags.get(i).name);
                }

                // 조회수
                txtViewList[9].setText("조회수 "+ review1.view);

                // 맵 세팅
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        LatLng storeLatLng = new LatLng(review1.storeLat, review1.storeLng);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 16));

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(storeLatLng).title(review1.storeName);
                        googleMap.addMarker(markerOptions).setTag(0);
                    }
                });

                // 가게 주소
                txtViewList[10].setText(review1.storeAddr);

                imgViewList[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(review1.isLike == 0 ){
                            likeReview();
                        } else {
                            likeDeleteReview();
                        }
                    }
                });


            }
            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {

            }
        });



    }
    // 인디케이터 업데이트 메서드
    private void updateIndicator(int position) {
        indicatorLayout.removeAllViews(); // 기존 인디케이터 제거

        int indicatorSize = getResources().getDimensionPixelSize(R.dimen.indicator_size); // 인디케이터 크기
        int indicatorMargin = getResources().getDimensionPixelSize(R.dimen.indicator_margin); // 인디케이터 간격

        // 이미지 URL 목록 크기에 따라 인디케이터를 추가합니다.
        for (int i = 0; i < adapter.getItemCount(); i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(
                    i == position ? R.drawable.ic_indicator_selected : R.drawable.ic_indicator_unselected
            );

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    indicatorSize,
                    indicatorSize
            );

            if (i > 0) {
                // 첫 번째 인디케이터에는 간격을 주지 않습니다.
                layoutParams.leftMargin = indicatorMargin; // 왼쪽 여백 설정
            }

            indicator.setLayoutParams(layoutParams);
            indicatorLayout.addView(indicator);
        }
    }

    void likeReview() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewRes> call = api.likeReview("Bearer "+ token, reviewId);
        call.enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if(response.isSuccessful()){
                    finish();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent();
                    startActivity(intent);
                    overridePendingTransition(0,0);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {

            }
        });
    }

    //좋아요 삭제 메서드
    void likeDeleteReview() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewRes> call = api.likeDeleteReview("Bearer "+ token, reviewId);
        call.enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if(response.isSuccessful()){
                    finish();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent();
                    startActivity(intent);
                    overridePendingTransition(0,0);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {

            }
        });
    }

}
