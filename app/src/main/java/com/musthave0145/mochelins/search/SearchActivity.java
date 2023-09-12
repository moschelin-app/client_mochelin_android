package com.musthave0145.mochelins.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.musthave0145.mochelins.R;

public class SearchActivity extends AppCompatActivity {


    Fragment historyFragment;
    Fragment resultFragment;
    Fragment searchFragment;

    ImageView btnBack;
    EditText searchQuery;
    ImageView btnSearch;

    FragmentContainerView fragment;

    Fragment nowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnBack = findViewById(R.id.btnBack);
        searchQuery = findViewById(R.id.searchQuery);
        btnSearch = findViewById(R.id.btnSearch);
        fragment = findViewById(R.id.fragment);


        historyFragment = new HistoryFragment();
        nowFragment = historyFragment;
        resultFragment = new ResultFragment();
        searchFragment = new SearchFragment();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowFragment == historyFragment || nowFragment == searchFragment){
                    finish();
                }else {
                    searchQuery.setText("");
                }
            }
        });

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = searchQuery.getText().toString().trim();

                if(search.isEmpty()){
                    loadFragment(historyFragment);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("search", search);
                    searchFragment.setArguments(bundle);

                    loadFragment(searchFragment);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(resultFragment);
            }
        });


    }


    boolean loadFragment(Fragment fragment){
        if(fragment != null) {
            nowFragment = fragment;

            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();


            fragmentManager.replace(R.id.fragment, fragment).commit();

            return true;
        } else {
            return false;
        }

    }
}