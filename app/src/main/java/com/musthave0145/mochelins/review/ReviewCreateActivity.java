package com.musthave0145.mochelins.review;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.adapter.ItemAdapter;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.ReviewApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.meeting.MeetingCreateActivity;
import com.musthave0145.mochelins.meeting.MeetingPlaceSelectActivity;
import com.musthave0145.mochelins.model.PlaceSelect;
import com.musthave0145.mochelins.model.ReviewRes;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewCreateActivity extends AppCompatActivity {

    TextView txtSave;
    TextView txtPlace;

    EditText editContent;
    EditText editTag;

    ImageView imgClear;
    ImageView imgBack;

    Integer[] imageViews = {R.id.imgPhoto1, R.id.imgPhoto2, R.id.imgPhoto3, R.id.imgPhoto4, R.id.imgPhoto5};

    ImageView[] imageViewList = new ImageView[imageViews.length];

    Integer[] starImageViews = {R.id.imgStar1, R.id.imgStar2, R.id.imgStar3, R.id.imgStar4, R.id.imgStar5};

    ImageView[] starImageViewList = new ImageView[starImageViews.length];

    File photoFile;
    PlaceSelect placeSelect = new PlaceSelect();

    // 사진을 여러개 담아서 처리할 어레이리스트!
    ArrayList<File> photoFiles = new ArrayList<>();

    // 파일을 여러개 담는 방법.


    // 별점을 담을 별점 전역변수
    private int currentRating = 0;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            // 로그 무조건 찍어보자!!
//                            Log.i("MeetingCreate", ((PlaceSelect)result.getData().getSerializableExtra("placeSelect")).storeName);
                            Log.i("reviewcreate", "reviewsuccess");
                            placeSelect = ((PlaceSelect)result.getData().getSerializableExtra("placeSelect"));
                            txtPlace.setText(placeSelect.storeName + "\n" + placeSelect.storeAddr);
                        }
                    });


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_create);

        txtSave = findViewById(R.id.txtSave);
        txtPlace = findViewById(R.id.txtPlace);
        editContent = findViewById(R.id.editContent);
        editTag = findViewById(R.id.editTag);
        imgBack = findViewById(R.id.imgBack);
        imgClear = findViewById(R.id.imgClear);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoFiles.clear();
                for (int i = 0; i < imageViews.length; i++) {
                    imageViewList[i].setImageResource(R.drawable.outline_camera_alt_24);
                    Log.i("리뷰크리에이트" , String.valueOf(photoFiles));
                }
            }
        });

        for(int i = 0; i < imageViews.length; i++){
            imageViewList[i] = findViewById(imageViews[i]);
        }

        for(int i = 0; i < starImageViews.length; i++){
            starImageViewList[i] = findViewById(starImageViews[i]);
        }

        for (int i = 0; i < starImageViews.length; i++) {
            final int rating = i + 1; // 별점 값은 1부터 시작

            starImageViewList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 별점을 클릭한 이미지뷰의 위치까지 업데이트
                    updateRating(rating);
                }
            });
        }

        txtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewCreateActivity.this, ReviewPlaceSelectActivity.class);
//                        startActivity(intent);
                launcher.launch(intent);

            }
        });





        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 리뷰 작성 버튼을 누르고 나서, 레트로핏으로 api 호출!
                // 유저가 작성한 데이터를 갖고오쟈
                String strContent = editContent.getText().toString();
                Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewCreateActivity.this);
                ReviewApi api = retrofit.create(ReviewApi.class);

                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String token = sp.getString(Config.ACCESS_TOKEN, "");

                // 해시태그 처리하기.
                String text = editTag.getText().toString();
                Pattern pattern = Pattern.compile("#\\w+"); // 해시태그 패턴
                Matcher matcher = pattern.matcher(text);

                ArrayList<String> hashtags = new ArrayList<>();
                while (matcher.find()) {
                    String hashtag = matcher.group().substring(1); // '#' 제외
                    hashtags.add(hashtag);
                }

                // TODO: 사진을 여러개 선택해서 보내줘야한다.

                // TODO: 필터에서 셋팅한 값을 불러와야 한다.
                // 사진의 갯수만큼 꺼내서 주쟈!
                List<MultipartBody.Part> parts = new ArrayList<>();

                RequestBody fileBody = RequestBody.create(photoFile, MediaType.parse("image/jpg"));
                MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", photoFile.getName(), fileBody );

                RequestBody contentBody = RequestBody.create(strContent, MediaType.parse("text/plain"));
                RequestBody ratingBody = RequestBody.create(String.valueOf(currentRating), MediaType.parse("text/plain"));

                RequestBody storeNameBody = RequestBody.create(placeSelect.storeName, MediaType.parse("text/plain"));
                RequestBody storeLatBody = RequestBody.create(String.valueOf(placeSelect.storeLat),MediaType.parse("text/plain"));
                RequestBody storeLngBody = RequestBody.create(String.valueOf(placeSelect.storeLng),MediaType.parse("text/plain"));

                RequestBody tagBody = RequestBody.create(hashtags.get(0), MediaType.parse("text/plain"));

                Call<ReviewRes> call = api.addReview("Bearer " + token, photo, contentBody,storeNameBody,storeLatBody,storeLngBody, ratingBody, tagBody);

                call.enqueue(new Callback<ReviewRes>() {
                    @Override
                    public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(ReviewCreateActivity.this, "정상적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else  {
                            Toast.makeText(ReviewCreateActivity.this, "문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewRes> call, Throwable t) {

                    }
                });
            }
        });

        imageViewList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });



    }

    private void updateRating(int rating) {
        // 현재 별점 값 업데이트
        currentRating = rating;

        // 이미지뷰 업데이트
        for (int i = 0; i < starImageViews.length; i++) {
            if (i < rating) {
                // 별점 이상의 이미지뷰는 별 이미지로 변경
                starImageViewList[i].setImageResource(R.drawable.baseline_star_24);
            } else {
                // 나머지 이미지뷰는 빈 별 이미지로 변경
                starImageViewList[i].setImageResource(R.drawable.baseline_star_border_24);
            }
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewCreateActivity.this);
        builder.setTitle(R.string.alert_title);
        builder.setItems(R.array.alert_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    // 첫번째 항목 눌렀을때
                    // 카메라로 사진찍기
                    camera();

                }else if(i == 1){
                    // 두번째 항목 눌렀을때
                    album();
                }
            }
        });
        builder.show();
    }

    private void camera() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                ReviewCreateActivity.this, android.Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReviewCreateActivity.this,
                    new String[]{android.Manifest.permission.CAMERA},
                    1000);
            Toast.makeText(ReviewCreateActivity.this, "카메라 이용 권한이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (photoFiles.size() < 5) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(ReviewCreateActivity.this.getPackageManager()) != null) {

                    // 사진의 파일명을 만들기
                    String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    File photoFile = getPhotoFile(fileName);

                    Uri fileProvider = FileProvider.getUriForFile(ReviewCreateActivity.this,
                            "com.musthave0145.Mochelins.fileprovider", photoFile);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                    startActivityForResult(i, 100);

                    // TODO: 사진을 찍어도 똑같이 선택이 되어야한다@!!@##$#@$%ㅆ$#%^^ㅕ%$#%^&*ㅒ
//                    photoFiles.add(photoFile);
//                    Log.i("리뷰크리에이트", String.valueOf(photoFiles));


                } else {
                    Toast.makeText(ReviewCreateActivity.this, "이 기기에는 카메라 앱이 없습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ReviewCreateActivity.this, "최대로 올릴 수 있는 사진은 5장입니다!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void album() {
        if (checkPermission()) {
            if (photoFiles.size() < 5) {
                displayFileChoose();
            } else {
                Toast.makeText(ReviewCreateActivity.this, "더 이상 사진을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(ReviewCreateActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return false;
        }else{
            return true;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ReviewCreateActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i("DEBUGGING5", "true");
            Toast.makeText(ReviewCreateActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Log.i("DEBUGGING6", "false");
            ActivityCompat.requestPermissions(ReviewCreateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try{
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ReviewCreateActivity.this, "권한이 허가 되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReviewCreateActivity.this, "아직 승인하지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ReviewCreateActivity.this, "권한이 허가 되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReviewCreateActivity.this, "아직 승인하지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){
            // 카메라로 바로 찍었을 때의 영역
            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(photoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            photo = rotateBitmap(photo, orientation);

            // 압축시킨다. 해상도 낮춰서
            OutputStream os;
            try {
                os = new FileOutputStream(photoFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }
            photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ArrayList<Bitmap> photos = new ArrayList<>();
            photos.add(photo);

            for (int i = 0; i < photos.size(); i++){
                if (i < imageViewList.length) {
                    // photoFiles 리스트에 첫 번째 사진이 있을 때
                    imageViewList[i].setImageBitmap(photos.get(i));
                    imageViewList[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageViewList[i].setClipToOutline(true);
                }
            }

            // 네트워크로 데이터 보낸다.
        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){
            // 앨범에서 선택할때의 부분
            Uri albumUri = data.getData( );
            String fileName = getFileName( albumUri );
            try {
                // 객체를 하나하나 로그를 찍어보고 어떤 데이터인지 확인해보고, 반복문 사용해서 넣어야 한다.
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( albumUri, "r" );
                if ( parcelFileDescriptor == null ) return;
                FileInputStream inputStream = new FileInputStream( parcelFileDescriptor.getFileDescriptor( ) );
                photoFile = new File( this.getCacheDir( ), fileName );
                FileOutputStream outputStream = new FileOutputStream( photoFile );
                IOUtils.copy( inputStream, outputStream );

                photoFiles.add(photoFile);

                Log.i("리뷰크리에이트", String.valueOf(photoFile));
                Log.i("리뷰크리에이트", String.valueOf(photoFiles));

//                //임시파일 생성
//                File file = createImgCacheFile( );
//                String cacheFilePath = file.getAbsolutePath( );

                // 압축시킨다. 해상도 낮춰서
                Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                OutputStream os;
                try {
                    os = new FileOutputStream(photoFile);
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                for (int i = 0; i < photoFiles.size(); i++){
                    if (i < imageViewList.length) {
                        imageViewList[i].setImageBitmap(BitmapFactory.decodeFile(photoFiles.get(i).getAbsolutePath()));
                        imageViewList[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageViewList[i].setClipToOutline(true);
                    }
                }

            } catch ( Exception e ) {
                e.printStackTrace( );
            }

            // 네트워크로 보낸다.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getFileName( Uri uri ) {
        Cursor cursor = getContentResolver( ).query( uri, null, null, null, null );
        try {
            if ( cursor == null ) return null;
            cursor.moveToFirst( );
            @SuppressLint("Range") String fileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );
            cursor.close( );
            return fileName;

        } catch ( Exception e ) {
            e.printStackTrace( );
            cursor.close( );
            return null;
        }
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    Dialog dialog;

    void showProgress(){
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(this));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    void dismissProgress(){
        dialog.dismiss();
    }

}