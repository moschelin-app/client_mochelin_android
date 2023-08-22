package com.musthave0145.mochelins;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.musthave0145.mochelins.model.Review;

public class ReviewDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    Review review;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();



        review = (Review) getIntent().getSerializableExtra("review");
    }
}