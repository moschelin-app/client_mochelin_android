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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.model.MapData;
import com.musthave0145.mochelins.model.MapListRes;

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


    ArrayList<MapData> customMapArrayList = new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        LocationManager locationManager;
        LocationListener locationListener;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap =googleMap;

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

                        customMapArrayList.addAll(mapList.items);

                        for (MapData mapData : customMapArrayList) {
                            LatLng location = new LatLng(mapData.storeLat, mapData.storeLng);
                            // 커스텀 마커 레이아웃 설정
                            View customMarkerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
                            ImageView imageView = customMarkerView.findViewById(R.id.imageView);
                            TextView textView = customMarkerView.findViewById(R.id.textView);

                            imageView.setImageResource(R.drawable.baseline_star_24);
                            String strRating = mapData.rating+"";
                            textView.setText(strRating);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(viewToBitmap(customMarkerView));
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location)
                                    .title(mapData.storeName)
                                    .icon(icon);

                            Marker marker = googleMap.addMarker(markerOptions);
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
                    3000,
                    -1,
                    locationListener);
        }
    };

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}