package com.musthave0145.mochelins.meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.musthave0145.mochelins.R;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MeetingCreateActivity extends AppCompatActivity {

    Integer[] imgButtons = {R.id.imgButton, R.id.imgView, R.id.btnPlus,
                           R.id.btnMinus};

    ImageView[] imgButtonList = new ImageView[imgButtons.length];

    TextView txtPlace;
    Button btnDate;
    Button btnTime;
    EditText editPerson;
    EditText editMoney;
    TextView txtPay;
    RelativeLayout moneyLayout;
    Switch switchPay;

    Toolbar toolbar;
    File photoFile;

    String date = "";
    String time = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_create);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        for (int i = 0; i < imgButtons.length ; i++) {
            imgButtonList[i] = findViewById(imgButtons[i]);}
        txtPlace = findViewById(R.id.txtPlace);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        editMoney = findViewById(R.id.editMoney);
        txtPay = findViewById(R.id.txtPay);
        moneyLayout = findViewById(R.id.moneyLayout);

        switchPay = findViewById(R.id.switchPay);

        editPerson = findViewById(R.id.editPerson);

        imgButtonList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });

        imgButtonList[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        txtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 장소 선택
                txtPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MeetingCreateActivity.this, MeetingPlaceSelectActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance();
//                현재의 년 월 일 (오늘날짜)가져오는 코드
//                current.get(Calendar.YEAR);
//                current.get(Calendar.MONTH);
//                current.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MeetingCreateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                // i : 년, i1 : 월(0부터시작, 화면에 보일라면 +1), i2 : 일
                                // date : 유저가 설정한 날짜 ex)

                                int month = i1 + 1;
                                String strMonth;
                                if(month < 10) {
                                    strMonth = "0" + month;
                                } else {
                                    strMonth = "" + month;
                                }


                                String strDay;
                                if(i2 < 10) {
                                    strDay = "0" + i2;
                                } else {
                                    strDay = "" + i2;
                                }

                                date = i + "-" + strMonth + "-" + strDay;
                                btnDate.setText(date);


                            } // 다른건 다 상관없고, 월만 0으로 시작한디.. 1월은 0, 2월은 1월 ...
                        },current.get(Calendar.YEAR),current.get(Calendar.MONTH),current.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();

            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar current = Calendar.getInstance();

                TimePickerDialog dialog = new TimePickerDialog(
                        MeetingCreateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                String hour;
                                if(i < 10) {
                                    hour = "0" + i;
                                } else {
                                    hour = "" + i;
                                }

                                String minute;
                                if(i1 < 10){
                                    minute = "0" + i1;
                                } else {
                                    minute = "" + i1;
                                }

                                time = hour + ":" + minute;
                                btnTime.setText(time);


                            }
                        },
                        current.get(Calendar.HOUR_OF_DAY),
                        current.get(Calendar.MINUTE),
                        true

                );
                dialog.show();

            }
        });

        imgButtonList[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strDistance = editPerson.getText().toString().trim();
                int distance = Integer.parseInt(strDistance);
                if (distance > 1) {
                    distance = distance - 1;
                    editPerson.setText(distance+"");
                }
            }
        });

        imgButtonList[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strDistance = editPerson.getText().toString().trim();
                int distance = Integer.parseInt(strDistance);
                if (distance < 10) {
                    distance = distance + 1;
                    editPerson.setText(distance+"");
                }
            }
        });

        editPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력 전에 수행
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력 중에 수행
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력 후에 수행할 작업
                String text = editable.toString();
                int value;

                try {
                    value = Integer.parseInt(text);
                    if (value < 2 || value > 10) {
                        editPerson.setError("2명과 10명 사이의 값을 입력하세요.");
                    } else {
                        editPerson.setError(null); // 에러 메시지 제거
                    }
                } catch (NumberFormatException e) {
                    editPerson.setError("올바른 숫자를 입력하세요.");
                }
            }
        });

        switchPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchPay.isChecked()) {
                    // switch가 체크되어 있는 경우
                    moneyLayout.setVisibility(View.VISIBLE);
                    txtPay.setText("사용자 지정");
                } else {
                    // switch가 체크되어 있지 않은 경우
                    moneyLayout.setVisibility(View.GONE);
                    txtPay.setText("각자 계산");
                }
            }
        });

    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingCreateActivity.this);
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

    private void camera(){
        int permissionCheck = ContextCompat.checkSelfPermission(
                MeetingCreateActivity.this, android.Manifest.permission.CAMERA);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MeetingCreateActivity.this,
                    new String[]{android.Manifest.permission.CAMERA} ,
                    1000);
            Toast.makeText(MeetingCreateActivity.this, "카메라 이용 권한이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(i.resolveActivity(MeetingCreateActivity.this.getPackageManager())  != null  ){

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(MeetingCreateActivity.this,
                        "com.musthave0145.mochelins.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else{
                Toast.makeText(MeetingCreateActivity.this, "이 기기에는 카메라 앱이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void album(){
        if(checkPermission()){
            displayFileChoose();
        }else{
            requestPermission();
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MeetingCreateActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return false;
        }else{
            return true;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MeetingCreateActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i("DEBUGGING5", "true");
            Toast.makeText(MeetingCreateActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Log.i("DEBUGGING6", "false");
            ActivityCompat.requestPermissions(MeetingCreateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
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
                    Toast.makeText(MeetingCreateActivity.this, "권한이 허가 되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MeetingCreateActivity.this, "아직 승인하지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MeetingCreateActivity.this, "권한이 허가 되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MeetingCreateActivity.this, "아직 승인하지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){

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

            imgButtonList[0].setVisibility(View.GONE);
            imgButtonList[1].setVisibility(View.VISIBLE);
            imgButtonList[1].setImageBitmap(photo);
            imgButtonList[1].setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgButtonList[1].setClipToOutline(true);

            // 네트워크로 데이터 보낸다.



        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){

            Uri albumUri = data.getData( );
            String fileName = getFileName( albumUri );
            try {

                ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( albumUri, "r" );
                if ( parcelFileDescriptor == null ) return;
                FileInputStream inputStream = new FileInputStream( parcelFileDescriptor.getFileDescriptor( ) );
                photoFile = new File( this.getCacheDir( ), fileName );
                FileOutputStream outputStream = new FileOutputStream( photoFile );
                IOUtils.copy( inputStream, outputStream );

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
                imgButtonList[0].setVisibility(View.GONE);
                imgButtonList[1].setVisibility(View.VISIBLE);
                imgButtonList[1].setImageBitmap(photo);
                imgButtonList[1].setScaleType(ImageView.ScaleType.CENTER_CROP);

//                imageView.setImageBitmap( getBitmapAlbum( imageView, albumUri ) );

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meeting_create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.btnDone) {

            finish();

        }
        return super.onOptionsItemSelected(item);

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