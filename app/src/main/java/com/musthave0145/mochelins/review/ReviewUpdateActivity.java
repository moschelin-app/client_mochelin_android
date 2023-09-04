package com.musthave0145.mochelins.review;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.Review;

public class ReviewUpdateActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txtSave;
    TextView txtPlace;

    Integer[] imgPhotos = {R.id.imgPhoto1,R.id.imgPhoto2,R.id.imgPhoto3,R.id.imgPhoto4,R.id.imgPhoto5};
    ImageView[] imgPhotoList = new ImageView[imgPhotos.length];

    Integer[] imgStars = {R.id.imgStar1, R.id.imgStar2, R.id.imgStar3, R.id.imgStar4, R.id.imgStar5};
    ImageView[] imgStarList = new ImageView[imgStars.length];

    ImageView imgClear;
    EditText editContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_update);
        Review review = (Review) getIntent().getSerializableExtra("review");

        imgBack = findViewById(R.id.imgBack);
        txtSave = findViewById(R.id.txtSave);
        txtPlace = findViewById(R.id.txtPlace);

        for(int i = 0; i < imgPhotos.length; i++){
            imgPhotoList[i] = findViewById(imgPhotos[i]);
        }

        for (int i = 0; i < imgStars.length; i++){
            imgStarList[i] = findViewById(imgStars[i]);
        }

        imgClear = findViewById(R.id.imgClear);
        editContent = findViewById(R.id.editContent);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        for (int i = 0; i < review.photos.size(); i++){
            Glide.with(ReviewUpdateActivity.this).load(review.photos.get(i).photo).into(imgPhotoList[i]);
        }

        editContent.setText(review.content);

        txtPlace.setText(review.storeName + "\n" + review.storeAddr);

        for (int i = 0; i < review.rating; i++){
            imgStarList[i].setImageResource(R.drawable.baseline_star_24);
        }



    }
}