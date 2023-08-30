package com.musthave0145.mochelins.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.ItemAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.model.ReviewListRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewDetailActivity extends AppCompatActivity {


    // TODO: 화면 연결은 했음, retrofit으로 네트웍에 있는 데이터를 가져와 셋팅하자.
    // TODO: 댓글 기능은 물어보거나 더 찾아봐야 할 것 같음.

    // 사진 여러장 넘기면서 보여주는 라이브러리
    ViewPager2 viewPager2;
    ArrayList<String> imgUrl = new ArrayList<>();
    ItemAdapter adapter;


    MapView mapView;

    Integer[] imgViews = {R.id.imgBack, R.id.imgShare, R.id.imgLike,
            R.id.imgStar1, R.id.imgStar2, R.id.imgStar3, R.id.imgStar4, R.id.imgStar5};

    ImageView[] imgViewList = new ImageView[imgViews.length];

    Integer[] txtViews = {R.id. txtStoreName, R.id.txtLike, R.id.txtStar, R.id.txtCreatedAt,
            R.id.txtContent, R.id.txtTag1, R.id.txtTag2, R.id.txtTag3,
            R.id.txtAddress, R.id.txtDetailStore};
    TextView[] txtViewList = new TextView[txtViews.length];
    String token;


    ArrayList<Review> reviewArrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        viewPager2 = findViewById(R.id.viewPager2);
        mapView = findViewById(R.id.mapView);

        for(int i = 0; i < imgViews.length; i++) {
            imgViewList[i] = findViewById(imgViews[i]);
        }

        for(int i = 0; i < txtViews.length; i++) {
            txtViewList[i] = findViewById(txtViews[i]);
        }

        Review review = (Review) getIntent().getSerializableExtra("review");

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewDetailActivity.this);
        ReviewApi api = retrofit.create(ReviewApi.class);

        Call<ReviewListRes> call = api.getReviewDetail("Bearer " + token, review.id);
        call.enqueue(new Callback<ReviewListRes>() {
            @Override
            public void onResponse(Call<ReviewListRes> call, Response<ReviewListRes> response) {
                if(response.isSuccessful()){
                    ReviewListRes reviewListRes = response.body();
                    Review review1 = reviewListRes.item.get(0);

                    txtViewList[0].setText(review1.storeName);

                    if (review1.isLike == 1) {
                        imgViewList[2].setImageResource(R.drawable.baseline_favorite_24);
                    }

                    txtViewList[1].setText(review1.likeCnt);
                    // 별 번호 3 4 5 6 7
                    for (int i = 0; i < review1.rating-1; i++){
                        if (review1.rating == i) {
                            imgViewList[i+3].setImageResource(R.drawable.baseline_star_24);
                        }
                    }

                    txtViewList[2].setText(review1.rating);

                } else {

                }
            }

            @Override
            public void onFailure(Call<ReviewListRes> call, Throwable t) {

            }
        });


//        adapter = new ItemAdapter()
//
//        viewPager2.
    }


}