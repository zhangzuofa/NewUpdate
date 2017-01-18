package com.example.administrator.newupdate;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DownLoadService.DownloadBinder downloadBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownLoadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start_btn = (Button) findViewById(R.id.start_btn);
        Button pause_btn = (Button) findViewById(R.id.pause_btn);
        Button stop_btn = (Button) findViewById(R.id.stop_btn);
        start_btn.setOnClickListener(this);
        pause_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        Intent intent = new Intent(this,DownLoadService.class);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        }

    }


    @Override
    public void onClick(View v) {
        if (downloadBinder == null){
            return;
        }
        switch (v.getId()){
            case R.id.start_btn:
             String url ="http://dvideo.spriteapp.cn/video/2017/0116/96a4e10c-dbea-11e6-838e-90b11c479401cut_wpd.mp4http://dvideo.spriteapp.cn/video/2017/0116/96a4e10c-dbea-11e6-838e-90b11c479401cut_wpd.mp4";
                downloadBinder.startDownload(url);
                break;
            case R.id.pause_btn:
                downloadBinder.pauseDownload();
                break;
            case R.id.stop_btn:
                downloadBinder.cancelDownload();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
