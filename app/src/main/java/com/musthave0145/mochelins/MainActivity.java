package com.musthave0145.mochelins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.meeting.MeetingCreateActivity;
import com.musthave0145.mochelins.meeting.MeetingFragment;
import com.musthave0145.mochelins.review.ReviewFragment;
import com.musthave0145.mochelins.user.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment reviewFragment;
    Fragment meetingFragment;
    Fragment mapFragment;
    Fragment plannerFragment;
//    Toolbar toolbar;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);

//        // 액티비티 시작 시 첫 번째 프래그먼트를 표시
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.reviewFragment, new ReviewFragment())
//                    .commit();
//        }

        // 회원가입이나 로그인이 되어있는 유저인지 체크해야 한다.
        // 억세스토큰이 있는지를 확인하는 코드로 작성.
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        if(token.isEmpty()){
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

            finish();
            return;
        }



        // 탭바 화면이랑 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 프레그먼트 객체 생성
        reviewFragment = new ReviewFragment();
        meetingFragment = new MeetingFragment();
        mapFragment = new MapFragment();
        plannerFragment = new PlannerFragment();

        // 탭바가 눌렸을 때 프레그먼트 이동
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                Fragment fragment = null;
                if(itemId == R.id.reviewFragment) {
                    fragment = reviewFragment;
                } else if (itemId == R.id.meetingFragment ) {
                    fragment = meetingFragment;
                } else if (itemId == R.id.mapFragment) {
                    fragment = mapFragment;
                } else if (itemId == R.id.plannerFragment) {
                    fragment = plannerFragment;
                }
                return loadFragment(fragment);
            }
        });
    }

    boolean loadFragment(Fragment fragment){
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();// 화면 전환 코드
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Config.ACCESS_TOKEN);
        editor.apply();
        finish();
    }

}