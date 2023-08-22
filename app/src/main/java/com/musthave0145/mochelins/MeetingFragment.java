package com.musthave0145.mochelins;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.musthave0145.mochelins.adapter.MeetingAdapter;
import com.musthave0145.mochelins.api.MeetingApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.MeetingListRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingFragment newInstance(String param1, String param2) {
        MeetingFragment fragment = new MeetingFragment();
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


    RecyclerView recyclerView;
    ProgressBar progressBar;
    MeetingAdapter adapter;
    ArrayList<Meeting> meetingArrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_meeting, container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getNetworkData();
    }

    private void getNetworkData() {
        // 중복으로 겹치지 않게 비워주쟈!
        meetingArrayList.clear();

        // 프로그레스바를 보이게 하자
        progressBar.setVisibility(View.VISIBLE);

        // 레트로핏 가져오기
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        // API 만들기
        MeetingApi api = retrofit.create(MeetingApi.class);

        // 쿼리파라미터 셋팅
        Call<MeetingListRes> call = api.getMeetingList(0,5,35.0809745,128.8808061,1.5);

        call.enqueue(new Callback<MeetingListRes>() {
            @Override
            public void onResponse(Call<MeetingListRes> call, Response<MeetingListRes> response) {
                progressBar.setVisibility(View.GONE);
                Log.i("성공", response.body().items.get(0).content);


                if(response.isSuccessful()){
                    // 200 OK
                    MeetingListRes meetingListRes = response.body();
                    meetingArrayList.addAll(meetingListRes.items);

                    adapter = new MeetingAdapter(getActivity(), meetingArrayList);
                    recyclerView.setAdapter(adapter);

                } else {
                    // 서버에 문제있는겨
                    Toast.makeText(getActivity(), "서버에 문제있음",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MeetingListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("에러", t.getMessage());


            }
        });


    }
}