package com.musthave0145.mochelins.review;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.musthave0145.mochelins.FilterActivity;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.ReviewAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Review;
import com.musthave0145.mochelins.model.UserRes;
import com.musthave0145.mochelins.user.LoginActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
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
    ImageView imgAdd;
    ImageView imgSearch;
    ImageView imgMenuClear;
    DrawerLayout reviewDrawer;
    Integer[] cardViews = {R.id.cardRecommend, R.id.cardMe, R.id.cardReview, R.id.cardMeeting,
            R.id.cardMap, R.id.cardPlanner, R.id.cardLogout};
    CardView[] cardViewList = new CardView[cardViews.length];
    Button btnFilter;
    RecyclerView recyclerView;
    ReviewAdapter adapter;
    ArrayList<Review> reviewArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        imgMenu = rootView.findViewById(R.id.imgMenu);
        imgAdd = rootView.findViewById(R.id.imgAdd);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        imgMenuClear = rootView.findViewById(R.id.imgMenuClear);
        reviewDrawer = rootView.findViewById(R.id.reviewDrawer);

        btnFilter = rootView.findViewById(R.id.btnFilter);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 사이드 메뉴바를 열고 닫는 코드
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDrawer.openDrawer(GravityCompat.END);
            }
        });

        imgMenuClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDrawer.closeDrawer(GravityCompat.END);
            }
        });
        // 사이드 메뉴바 안에 카드뷰 연결코드
        for(int i = 0; i < cardViews.length; i++) {
            cardViewList[i] = rootView.findViewById(cardViews[i]);
        }


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewCreateActivity.class);
                startActivity(intent);
            }
        });

//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        cardViewList[2].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragmentTransaction.replace(R.id.reviewFragment, new ReviewFragment());
//                fragmentTransaction.addToBackStack(null); // 백 스택에 추가 (선택 사항)
//                fragmentTransaction.commit();
//            }
//        });
//        cardViewList[3].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragmentTransaction.replace(R.id.meetingFragment, new MeetingFragment());
//                fragmentTransaction.addToBackStack(null); // 백 스택에 추가 (선택 사항)
//                fragmentTransaction.commit();
//            }
//        });
//        cardViewList[4].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragmentTransaction.replace(R.id.mapFragment, new MapFragment());
//                fragmentTransaction.addToBackStack(null); // 백 스택에 추가 (선택 사항)
//                fragmentTransaction.commit();
//            }
//        });
//        cardViewList[5].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragmentTransaction.replace(R.id.plannerFragment, new PlannerFragment());
//                fragmentTransaction.addToBackStack(null); // 백 스택에 추가 (선택 사항)
//                fragmentTransaction.commit();
//            }
//        });

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




        // Inflate the layout for this fragment
        return rootView;
    }
}