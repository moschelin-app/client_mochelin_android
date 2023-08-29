package com.musthave0145.mochelins.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.ItemAdapter;

public class ReviewDetailActivity extends AppCompatActivity {


    // TODO: 화면 연결은 했음, retrofit으로 네트웍에 있는 데이터를 가져와 셋팅하자.
    // TODO: 댓글 기능은 물어보거나 더 찾아봐야 할 것 같음.

    // 사진 여러장 넘기면서 보여주는 라이브러리
    ViewPager2 viewPager2;
    String[] imgUrls = new String[] {

    };
    ItemAdapter adapter;


    MapView mapView;

    Integer[] imgViews = {R.id.imgBack, R.id.imgShare, R.id.imgShare, R.id.imgShare,
            R.id.imgStar1, R.id.imgStar2, R.id.imgStar3, R.id.imgStar4, R.id.imgStar5};

    ImageView[] imgViewList = new ImageView[imgViews.length];

    Integer[] txtViews = {R.id. txtStoreName, R.id.txtLike, R.id.txtStar, R.id.txtCreatedAt,
            R.id.txtContent, R.id.txtTag1, R.id.txtTag2, R.id.txtTag3,
            R.id.txtAddress, R.id.txtDetailStore};
    TextView[] txtViewList = new TextView[txtViews.length];

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
//        viewPager2 = findViewById(R.id.viewPager2);
        mapView = findViewById(R.id.viewPager2);

        for(int i = 0; i < imgViews.length; i++) {
            imgViewList[i] = findViewById(imgViews[i]);
        }

        for(int i = 0; i < txtViews.length; i++) {
            txtViewList[i] = findViewById(txtViews[i]);
        }
    }
}