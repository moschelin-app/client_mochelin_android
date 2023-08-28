package com.musthave0145.mochelins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.meeting.MeetingFragment;
import com.musthave0145.mochelins.model.UserRes;
import com.musthave0145.mochelins.review.ReviewFragment;
import com.musthave0145.mochelins.user.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView imgMenu;
    ImageView imgMenuClear;
    DrawerLayout mapDrawer;
    Integer[] cardViews = {R.id.cardRecommend, R.id.cardMe, R.id.cardReview, R.id.cardMeeting,
            R.id.cardMap, R.id.cardPlanner, R.id.cardLogout};
    CardView[] cardViewList = new CardView[cardViews.length];

    Fragment reviewFragment;
    Fragment meetingFragment;
    Fragment mapFragment;
    Fragment plannerFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        imgMenu = rootView.findViewById(R.id.imgMenu);
        imgMenuClear = rootView.findViewById(R.id.imgMenuClear);
        mapDrawer = rootView.findViewById(R.id.mapDrawer);

        // 사이드 메뉴바를 열고 닫는 코드
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapDrawer.openDrawer(GravityCompat.END);
            }
        });

        imgMenuClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapDrawer.closeDrawer(GravityCompat.END);
            }
        });
        // 사이드 메뉴바 안에 카드뷰 연결코드
        for(int i = 0; i < cardViews.length; i++) {
            cardViewList[i] = rootView.findViewById(cardViews[i]);
        }


        reviewFragment = new ReviewFragment();
        meetingFragment = new MeetingFragment();
        mapFragment = new MapFragment();
        plannerFragment = new PlannerFragment();

        cardViewList[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomNavigationItem(R.id.reviewFragment);
                loadFragment(reviewFragment);
                mapDrawer.closeDrawer(GravityCompat.END);
            }
        });
        cardViewList[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomNavigationItem(R.id.meetingFragment);
                loadFragment(meetingFragment);
                mapDrawer.closeDrawer(GravityCompat.END);
            }
        });
        cardViewList[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomNavigationItem(R.id.mapFragment);
                loadFragment(mapFragment);
                mapDrawer.closeDrawer(GravityCompat.END);
            }
        });
        cardViewList[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomNavigationItem(R.id.plannerFragment);
                loadFragment(plannerFragment);
                mapDrawer.closeDrawer(GravityCompat.END);

            }
        });
        cardViewList[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
                UserApi api = retrofit.create(UserApi.class);

                SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
                String token = sp.getString(Config.ACCESS_TOKEN, "");

                Call<UserRes> call = api.logout("Bearer " + token);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if (response.isSuccessful()){
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);

                            SharedPreferences.Editor editor = sp.edit();
                            editor.remove(Config.ACCESS_TOKEN);
                            editor.commit();

                            getActivity().finish();
                        } else {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.remove(Config.ACCESS_TOKEN);
                            editor.commit();
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });
            }
        });

        return rootView;
    }

    boolean loadFragment(Fragment fragment){
        if(fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();// 화면 전환 코드
            return true;
        } else {
            return false;
        }

    }


    private void selectBottomNavigationItem(int itemId) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView); // 바텀 네비게이션 뷰의 ID를 사용합니다.

        // 바텀 네비게이션 뷰에서 선택한 아이템을 찾아 선택합니다.
        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);

        // 아이템을 선택한 것처럼 처리합니다.
        if (item != null) {
            item.setChecked(true);
        }
    }
}