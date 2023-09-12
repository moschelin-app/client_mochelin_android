package com.musthave0145.mochelins.acountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.AccountBookApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Account;
import com.musthave0145.mochelins.model.AcountRes;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddActivity2 extends AppCompatActivity {
    TextView txtDate;
    TextInputEditText editPrice;
    TextInputEditText editStore;
    TextInputEditText editContent;
    TextInputLayout layoutPrice;
    TextInputLayout layoutStore;
    TextInputLayout layoutContent;
    TextView btnCash;
    TextView btnCard;
    TextView btnInput;
    String date;
    String price;
    String payment;
    String storeName;
    String content;
    int money1;
    private boolean isRedBackgroundCash = false;
    private boolean isRedBackgroundCard = false;

    String selectDate;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add2);
        txtDate = findViewById(R.id.txtDate);
        editPrice = findViewById(R.id.editPrice);
        btnCash = findViewById(R.id.btnCash);
        btnCard = findViewById(R.id.btnCard);
        editStore = findViewById(R.id.editStore);
        editContent = findViewById(R.id.editContent);
        btnInput = findViewById(R.id.btnInput);
        layoutPrice = findViewById(R.id.layoutPrice);
        layoutStore = findViewById(R.id.layoutStore);


        // 날짜 선택을 눌렀을 때의 동작
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar current = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(
                        AddActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                int month = i1 + 1;
                                String strMonth;
                                if(month < 10) {
                                    strMonth = "0" + month;
                                } else {
                                    strMonth = "" + month;
                                }
                                String strDay;
                                if(i2 < 10) {
                                    strDay = "0" + i2;
                                } else {
                                    strDay = "" + i2;
                                }

                                date = i + "-" + strMonth + "-" + strDay;
                                txtDate.setText(date);

                                selectDate = date;

                            }
                        },current.get(Calendar.YEAR),current.get(Calendar.MONTH),current.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            }
        });


        // 현금 또는 카드 버튼을 눌렀을 때 동작
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRedBackgroundCash) {
                    // 현재 빨간색 배경인 경우, 원래 색으로 변경
                    // 투명한 배경으로 설정
                    btnCard.setBackgroundResource(R.drawable.bubble); // Card 버튼도 투명한 배경으로 설정
                    btnCard.setTextColor(Color.BLACK);
                } else  {
                    // 현재 원래 색인 경우, 빨간색으로 변경
                    btnCash.setBackgroundResource(R.drawable.bubble1);
                    btnCash.setTextColor(Color.WHITE);
                    btnCard.setBackgroundResource(R.drawable.bubble);// 빨간색 배경으로 설정
                    payment = "현금";


                }
                isRedBackgroundCash = !isRedBackgroundCash;
                 // 클릭 상태를 토
                // Card 버튼의 배경 색상을 원래 색으로 설정

            }
        });
        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRedBackgroundCard) {
                    // 현재 빨간색 배경인 경우, 원래 색으로 변경
                    // 투명한 배경으로 설정
                    btnCash.setBackgroundResource(R.drawable.bubble); // Cash 버튼도 투명한 배경으로 설정
                    btnCash.setTextColor(Color.BLACK);
                } else {
                    // 현재 원래 색인 경우, 빨간색으로 변경
                    btnCard.setBackgroundResource(R.drawable.bubble1); // 빨간색 배경으로 설정
                    btnCard.setTextColor(Color.WHITE);
                    btnCash.setBackgroundResource(R.drawable.bubble);
                    payment = "카드";
                }

                isRedBackgroundCard = !isRedBackgroundCard; // 클릭 상태를 토
                // Cash 버튼의 배경 색상을 원래 색으로 설정

            }
        });


        btnInput.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            price = editPrice.getText().toString().trim();
            storeName = editStore.getText().toString().trim();
            content = editContent.getText().toString().trim();

            if (selectDate == "" && price == "" && storeName == "" && payment ==""){

                Toast.makeText(AddActivity2.this, "필수 항목을 확인하세요.", Toast.LENGTH_SHORT).show();

            }

            String date = selectDate;

            Retrofit retrofit = NetworkClient.getRetrofitClient(AddActivity2.this);
            AccountBookApi api = retrofit.create(AccountBookApi.class);
            Account account = new Account(storeName, price, payment,date, content );
            SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String token = sp.getString(Config.ACCESS_TOKEN, "");



            Call<AcountRes> call =api.addAccount("Bearer " + token,account);

            call.enqueue(new Callback<AcountRes>() {
                @Override
                public void onResponse(Call<AcountRes> call, Response<AcountRes> response) {
                    if(response.isSuccessful()){
                        finish();
                    }else if(response.code() ==500){

                    }
                }

                @Override
                public void onFailure(Call<AcountRes> call, Throwable t) {

                }
            });

        }
    });

    }
}