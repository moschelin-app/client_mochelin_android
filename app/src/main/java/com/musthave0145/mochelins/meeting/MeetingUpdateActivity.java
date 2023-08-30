package com.musthave0145.mochelins.meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.Meeting;

public class MeetingUpdateActivity extends AppCompatActivity {



    ImageView imgBack;
    TextView txtSave;
    ImageView imgButton;
    ImageView imgView;
    EditText editContent;
    TextView txtPlace;
    Button btnDate;
    Button btnTime;
    ImageView imgUp;
    ImageView imgDown;
    EditText editPerson;
    Switch switchPay;
    RelativeLayout moneyLayout;
    EditText editMoney;
    Meeting meeting;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_update);

        imgBack = findViewById(R.id.imgBack);
        txtSave = findViewById(R.id.txtSave);
        imgButton = findViewById(R.id.imgButton);
        imgView = findViewById(R.id.imgView);
        editContent = findViewById(R.id.editContent);
        txtPlace = findViewById(R.id.txtPlace);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        imgUp = findViewById(R.id.imgUp);
        imgDown = findViewById(R.id.imgDown);
        editPerson = findViewById(R.id.editPerson);
        switchPay = findViewById(R.id.switchPay);
        moneyLayout = findViewById(R.id.moneyLayout);
        editMoney = findViewById(R.id.editMoney);

        meeting = (Meeting) getIntent().getSerializableExtra("meeting");

        // 원래 사진이 있으면, 원래의 사진을 보여주고, 없으면 기본이미지로 보여주자.
        if (meeting.photo != null){
            // 글의 이미지가 원래 있었을때,
            imgButton.setVisibility(View.INVISIBLE);
            imgView.setVisibility(View.VISIBLE);
            Glide.with(MeetingUpdateActivity.this).load(meeting.photo).into(imgView);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            // 글의 이미지가 원래 없을때
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        // 원래 글을 넣어주자!!
        editContent.setText(meeting.content);

        // 장소가 선택되어 있으면, 장소를 보여주자.
        txtPlace.setText(meeting.storeName + "\n" + meeting.storeAddr);

    }

}