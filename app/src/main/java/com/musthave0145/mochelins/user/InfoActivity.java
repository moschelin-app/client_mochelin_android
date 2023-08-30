package com.musthave0145.mochelins.user;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.User;
import com.musthave0145.mochelins.model.UserInfoRes;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoActivity extends AppCompatActivity {

    ImageView btnBackspace;
    ImageView btnEdit;

    CircleImageView imgProfile;
    TextView txtNickname;
    TextView txtName;
    TextView txtEmail;

    User user;
    String token;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //데이터를 받아오는 코드는 여기에 작성.
                    if ( result.getResultCode() == Config.RESTART_NUM){
                        finish();
                        overridePendingTransition(0, 0);
                        Intent intent = getIntent();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btnBackspace = findViewById(R.id.btnBackspace);
        btnEdit = findViewById(R.id.btnEdit);
        imgProfile = findViewById(R.id.imgProfile);
        txtNickname = findViewById(R.id.txtNickname);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);

        btnEdit.setVisibility(View.INVISIBLE);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(InfoActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        Call<UserInfoRes> call = null;
        if((getIntent().getIntExtra("userId", -1)) != -1){
            int userId = getIntent().getIntExtra("userId", -1);
            call = api.user_info("Bearer " + token, userId);
        }else {
            call = api.my_page("Bearer " + token);
        }

        call.enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                if(response.isSuccessful()){
                    user = response.body().item;

                    txtEmail.setText(user.email);
                    txtName.setText(user.name);
                    txtNickname.setText(user.nickname);

                    Glide.with(InfoActivity.this).load(user.profile)
                            .error(R.drawable.default_profile)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(imgProfile);

                    if(user.isMine == 1){
                        btnEdit.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {

            }
        });
        


        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, EditActivity.class);
                intent.putExtra("user", user);

                launcher.launch(intent);
            }
        });

    }
}