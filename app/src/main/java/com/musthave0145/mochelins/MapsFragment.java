package com.musthave0145.mochelins;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.musthave0145.mochelins.api.MapApi;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.StoreApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.MapData;
import com.musthave0145.mochelins.model.MapDataListener;
import com.musthave0145.mochelins.model.MapListRes;
import com.musthave0145.mochelins.model.Store;
import com.musthave0145.mochelins.model.StoreRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;

    double lat = 37.542127;
    double lng = 126.680545;
    boolean isLocationReady;
    double dis=1.5;

    private MapDataListener mapDataListener;

    ArrayList<MapData> customMapArrayList = new ArrayList<>();
    ArrayList<Store> storeArrayList = new ArrayList<>();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        LocationManager locationManager;
        LocationListener locationListener;

        private MapData clickMapData;


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap =googleMap;
            RatingBar ratingBar = rootView.findViewById(R.id.ratingBar);
            TextView txtName = rootView.findViewById(R.id.txtMapStoreName);
            TextView txtVicinity = rootView.findViewById(R.id.txtMapAddr);
            TextView txtView = rootView.findViewById(R.id.txtMapView);
            TextView txtLike = rootView.findViewById(R.id.txtMapLike);
            TextView txtContent = rootView.findViewById(R.id.txtContent);
            ImageView photo =rootView.findViewById(R.id.storePhoto);
            Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
            MapApi api = retrofit.create(MapApi.class);
            SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String token = sp.getString(Config.ACCESS_TOKEN, "");
            Call<MapListRes> call = api.getPlaceList("Bearer " +token,lat,lng,dis);

            call.enqueue(new Callback<MapListRes>() {

                @Override
                public void onResponse(Call<MapListRes> call, Response<MapListRes> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        MapListRes mapList = response.body();
                        Log.d("안녕","안녕");
                        customMapArrayList.addAll(mapList.items);

                        for (int i=0; i<customMapArrayList.size();i++) {
                            MapData mapData = customMapArrayList.get(i);

                            LatLng location = new LatLng(mapData.storeLat, mapData.storeLng);
                            // 커스텀 마커 레이아웃 설정
                            View customMarkerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
                            ImageView imageView = customMarkerView.findViewById(R.id.imageView);
                            TextView textView = customMarkerView.findViewById(R.id.textView);

                            imageView.setImageResource(R.drawable.baseline_star_24);

                            int intMapData = (int) mapData.rating;
                            String strRating = intMapData + "";
                            textView.setText(strRating);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(viewToBitmap(customMarkerView));
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location)
                                    .title(mapData.storeName)
                                    .icon(icon);

                            Marker marker = googleMap.addMarker(markerOptions);
                            marker.setTag(i);
                            marker.showInfoWindow();

                        }
                    }
                }

                private Bitmap viewToBitmap(View view) {
                    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                    view.draw(canvas);
                    return bitmap;
                }

                @Override
                public void onFailure(Call<MapListRes> call, Throwable t) {

                }
            });

            //cardView
            CardView cardView = rootView.findViewById(R.id.cardView);
            cardView.setVisibility(View.GONE);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    cardView.setVisibility(View.GONE);
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    cardView.setVisibility(View.VISIBLE);
                    Integer markerIndexInteger = (Integer) marker.getTag();
                    if (markerIndexInteger != null) {
                        int markerIndex = markerIndexInteger.intValue();

                        Log.i("숫자", markerIndex + "");
                        if (markerIndex >= 0 && markerIndex < customMapArrayList.size()) {
                            MapData clickedMapData = customMapArrayList.get(markerIndex);
                            Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
                            StoreApi api = retrofit.create(StoreApi.class);
                            SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
                            String token = sp.getString(Config.ACCESS_TOKEN, "");

                            Call<StoreRes> call = api.getStoreList("Bearer " + token, clickedMapData.storeId);
                            Log.d("storeName", "인사");
                            call.enqueue(new Callback<StoreRes>() {
                                @Override
                                public void onResponse(Call<StoreRes> call, Response<StoreRes> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        StoreRes storeList = response.body();
                                        Store store = storeList.item;
                                        Log.d("storeName", "성공했어");
                                        int isLikeValue =store.likeCnt;
                                        String isLikeString = String.valueOf(isLikeValue);
                                        int isviewValue =(int) store.view;
                                        String isViewString = String.valueOf(isviewValue);

                                        double isRatingValue = store.rating;
                                        int isStringInt = (int) isRatingValue;
                                        //가게이름
                                        txtName.setText(store.storeName);
                                        //가게주소
                                        txtVicinity.setText(store.storeAddr);
                                        // 게시글 조회수
                                        txtView.setText(isViewString);
                                        // 게시글 좋아요 수
                                        txtLike.setText(isLikeString);
                                        // 게시글 내용
                                        txtContent.setText(store.content);
                                        //가게 사진
                                        Glide.with(MapsFragment.this).load(store.photo).into(photo);
                                        //별점
                                        ratingBar.setRating((float) isRatingValue);

                                    } else {
                                        Log.d("ApiResponse", "storeList is null");
                                    }
                                }

                                @Override
                                public void onFailure(Call<StoreRes> call, Throwable t) {
                                    Log.e("API Call Error", "Error fetching store details", t);
                                }
                            });

                        }
                    }
                    return false;
                }
            });


            // 폰의 위치를 가져오기 위해서는,시스템 서버로부터 로케이션 메니저를 받아온다
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    isLocationReady = true;
                    if(isLocationReady){
                        if(getActivity() != null){
                            LatLng currentLocation = new LatLng(37.542127,126.680545);
                            View customMarkerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
                            ImageView imageView = customMarkerView.findViewById(R.id.imageView);
                            TextView textView = customMarkerView.findViewById(R.id.textView);

                            imageView.setImageResource(R.drawable.baseline_star_24);
                            textView.setText("3");
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(viewTomBitmap(customMarkerView));


                            //커스텀 마커 레이아웃을 사용한 MarkerOption 생성
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(currentLocation)
                                    .title("Cureent Location")
                                    .icon(icon);
                            //GoogleMap에 마커 추가
                            Marker marker = googleMap.addMarker(markerOptions);
                            marker.showInfoWindow();

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            googleMap.setMinZoomPreference(17);

                        }
                    }
                }

                private Bitmap viewTomBitmap(View view) {
                    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                    view.draw(canvas);
                    return bitmap;
                }
            };
            if( ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION} ,
                        100);
                return;
            }
            // 위치기반 허용하였으므로,
            // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100000,
                    -1,
                    locationListener);
        }
    };

    private View rootView;



    String name;
    String address;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapsFragment != null) {
            mapsFragment.getMapAsync(callback);
        }



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        TextView txtName = rootView.findViewById(R.id.txtMapStoreName);
        TextView txtVicinity =rootView.findViewById(R.id.txtMapAddr);

        mapDataListener = new MapDataListener() {
            @Override
            public void onMapDataReceived(Store clickStore) {

            }
        };
        return rootView;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }


}