package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

public class RecordVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    private static final int REQUEST_EXTERNAL_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        videoView = findViewById(R.id.img);
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(RecordVideoActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //todo 在这里申请相机、存储的权限
                ActivityCompat.requestPermissions(RecordVideoActivity.this,new String[] {Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
            } else {
                //todo 打开相机拍摄
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(takeVideoIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //todo 播放刚才录制的视频
            Uri videoUri = intent.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("RequestResult", "requestCode : "+requestCode);
        switch (requestCode) {
            case REQUEST_EXTERNAL_CAMERA: {
                //Log.d("RequestResult", "failure1");
                //todo 判断权限是否已经授予
                for(int i = 0;i<grantResults.length;i++){
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED) {
                        //Log.d("RequestResult", "failure2");
                        return;
                    }
                }
                //Log.d("RequestResult", "failure3");
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(takeVideoIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
                }
                break;
            }
        }
    }
}
