package com.example.administrator.newupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownLoadService extends Service {
    private DownloadTAsk downloadTask;
    private String downloadUrl;
  private DownloadListener downloadListener = new DownloadListener() {
      @Override
      public void onSucess() {
             downloadTask = null;
          stopForeground(true);
          getNOtificationManager().notify(1,getNOtification("下载成功",-1));
          Toast.makeText(DownLoadService.this, "下载成功", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onFailed() {
          downloadTask = null;
          stopForeground(true);
          getNOtificationManager().notify(1,getNOtification("下载失败",-1));
          Toast.makeText(DownLoadService.this, "下载失败", Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onProgress(int progress) {
          getNOtificationManager().notify(1,getNOtification("下载.......",progress));


      }

      @Override
      public void onPaused() {
          downloadTask = null;
          Toast.makeText(DownLoadService.this, "暂停", Toast.LENGTH_SHORT).show();


      }

      @Override
      public void onCanceled() {
      downloadTask = null;
          stopForeground(true);
          Toast.makeText(DownLoadService.this, "下载取消", Toast.LENGTH_SHORT).show();
      }
  };
    private NotificationManager getNOtificationManager(){
     return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNOtification(String title , int progress){
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100,progress,false);


        }
        return builder.build();
    }

    DownloadBinder mBinder = new DownloadBinder();
    @Override
    public IBinder onBind(Intent intent) {
      return mBinder;
    }
    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if (downloadTask == null){
                downloadUrl = url;
                downloadTask  = new DownloadTAsk(downloadListener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNOtification("下载...",0));
                Toast.makeText(DownLoadService.this, "下载.....", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload(){
            if (downloadTask != null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if (downloadTask != null){
                downloadTask.cancelDownload();
            }else {
                if (downloadUrl != null) {
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getParent();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                  getNOtificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownLoadService.this, "取消了", Toast.LENGTH_SHORT).show();

                }

            }

        }
    }
}
