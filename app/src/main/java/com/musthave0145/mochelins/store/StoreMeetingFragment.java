package com.musthave0145.mochelins.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.ReviewAdapter;
import com.musthave0145.mochelins.adapter.StoreMeetingAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.StoreApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.MeetingListRes;
import com.musthave0145.mochelins.model.Store;
import com.musthave0145.mochelins.model.StoreRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreMeetingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreMeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreMeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreMeetingFragment newInstance(String param1, String param2) {
        StoreMeetingFragment fragment = new StoreMeetingFragment();
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
    ArrayList<Meeting> meetingArrayList = new ArrayList<>();
    StoreMeetingAdapter adapter;
    ProgressBar progressBar;
    String token;

    int storeId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_store_meeting, container, false);
        // Inflate the layout for this fragment

        recyclerView = rootView.findViewById(R.id.recyclerView);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");


        if (this.getArguments() != null){
            storeId = this.getArguments().getInt("storeId" , 0);

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
            StoreApi api = retrofit.create(StoreApi.class);
            Call<MeetingListRes> call = api.getStoreMeetingList("Bearer " + token, storeId , 0, 10);
            call.enqueue(new Callback<MeetingListRes>() {
                @Override
                public void onResponse(Call<MeetingListRes> call, Response<MeetingListRes> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (response.isSuccessful()){
                        meetingArrayList.clear();
                        meetingArrayList.addAll(response.body().items);
                        adapter = new StoreMeetingAdapter(getActivity(), meetingArrayList);
                        recyclerView.setAdapter(adapter);

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<MeetingListRes> call, Throwable t) {

                }
            });
        }



        return rootView;
    }


}