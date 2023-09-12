package com.musthave0145.mochelins.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.SearchUserAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.SearchApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.SearchUser;
import com.musthave0145.mochelins.model.SearchUserRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchUserFragment newInstance(String param1, String param2) {
        SearchUserFragment fragment = new SearchUserFragment();
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
    int offset =0;
    int limit =5;
    int count;
    SearchUserAdapter adapter;
    String pagetoken;
    ImageView imgback;
    EditText searchQuery;
    ArrayList<SearchUser> searchUserResArrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_user2,container,false);
        imgback = rootView.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ResultFragment resultFragment = new ResultFragment();
//                transaction.replace(R.id.fragmentContainerView,resultFragment);
                transaction.commit();
            }
        });
        recyclerView =rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle = new Bundle();
        String keyword = bundle.getString("keyword");
        searchQuery.setText(keyword);
        addNetworkData();

        return rootView;
    }

    private void addNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        SearchApi api = retrofit.create(SearchApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String token = sp.getString(Config.ACCESS_TOKEN, "");
        Bundle bundle = new Bundle();
        String keyword = bundle.getString("keyword");
        Call<SearchUserRes> call = api.getSUList("Bearer " + token,offset,limit,keyword);

        call.enqueue(new Callback<SearchUserRes>() {
            @Override
            public void onResponse(Call<SearchUserRes> call, Response<SearchUserRes> response) {
                if(response.isSuccessful()){

                    SearchUserRes searchUserRes = response.body();

                    // 페이징 위한 변수 처리
                    count = searchUserRes.count;
                    offset = offset + count;

                    searchUserResArrayList.addAll( searchUserRes.items );

                    adapter = new SearchUserAdapter(getActivity(), searchUserResArrayList);

                    recyclerView.setAdapter(adapter);

                }else {

                }
            }

            @Override
            public void onFailure(Call<SearchUserRes> call, Throwable t) {
            }
        });
    }
}