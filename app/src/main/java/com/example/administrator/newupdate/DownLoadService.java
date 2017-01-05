package com.example.administrator.newupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class DownLoadService extends Service {
    private DownloadTAsk downloadTAsk;
    private String downloadUrl;
  private DownloadListener downloadListener = new DownloadListener() {
      @Override
      public void onSucess() {

      }

      @Override
      public void onFailed() {

      }

      @Override
      public void onProgress(int progress) {
          getNOtificationManager().notify(1,getNOtification("下载.......",progress));


      }

      @Override
      public void onPaused() {

      }

      @Override
      public void onCanceled() {

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
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
