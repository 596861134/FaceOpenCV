package com.gooda.face.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gooda.face.R;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "SplashActivity";
    public static final int REQUEST_PERMISSION = 11;
    SensorManager mSensorManager;
    Sensor mOrientation;

    TextView azimuthAngle;
    TextView pitchAngle;
    TextView rollAngle;

    Button image;
    Button camera;
    Button unity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
        initSensor();

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (!checkPermission()){
                Log.d(TAG,"checkPermission:no pass!");
                requestPermission();
            }else {
                Log.d(TAG,"checkPermission:pass!");
                startNext();
            }
        }else {
            startNext();
        }

    }

    void initViews(){
        azimuthAngle = (TextView) findViewById(R.id.azimuthAngle);
        pitchAngle = (TextView) findViewById(R.id.pitchAngle);
        rollAngle = (TextView) findViewById(R.id.rollAngle);

        image = (Button) findViewById(R.id.image);
        camera = (Button) findViewById(R.id.camera);
        unity = (Button) findViewById(R.id.unity);

    }

    void initSensor(){
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    int a = (int)event.values[0];
                    azimuthAngle.setText(a + "");
                    int b = (int)event.values[1];
                    pitchAngle.setText(b + "");
                    int c = (int)event.values[2];
                    rollAngle.setText(c + "");
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            },mOrientation,SensorManager.SENSOR_DELAY_NORMAL
        );
    }


    private boolean checkPermission(){
        Log.d(TAG,"checkPermission");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"CAMERA");
            return false;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            Log.d(TAG,"WRITE_EXTERNAL_STORAGE");
            return false;
        }

        return true;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION);

        ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startNext();
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Log.e(TAG,"权限申请被拒绝");
            }
        }
    }

    private void startNext(){
//        getWindow().getDecorView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this,CameraActivity.class));
//                startActivity(new Intent(SplashActivity.this,FdStaticPicActivity.class));
//                startActivity(new Intent(SplashActivity.this,UnityActivity.class));
//                Log.d("SplashActivity","finish");
//                finish();
//            }
//        },1000);
        image.setOnClickListener(this);
        camera.setOnClickListener(this);
        unity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image:
                startActivity(new Intent(SplashActivity.this,FdStaticPicActivity.class));
                break;
            case R.id.camera:
                startActivity(new Intent(SplashActivity.this,CameraActivity.class));
                break;
            case R.id.unity:
                startActivity(new Intent(SplashActivity.this,UnityActivity.class));
                break;
        }
    }
}
