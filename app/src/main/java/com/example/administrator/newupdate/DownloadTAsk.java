package com.example.administrator.newupdate;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class DownloadTAsk extends AsyncTask<String,Integer,Integer> {

    public static final int TYPE_SUCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELD = 3;
    private DownloadListener listener;

    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTAsk(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        File file = null;
        InputStream is = null;
        RandomAccessFile savedFile = null;
        long downloadLength = 0;
        String downloadUrl = params[0];
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getParent();
        file = new File(directory + fileName);
        if (file.exists()) {
            downloadLength = file.length();
        }
        try {
            long contentlength = getContentLength(downloadUrl);
            if (contentlength == 0) {
                return TYPE_FAILED;
            } else if (contentlength == downloadLength){
                return TYPE_SUCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(downloadUrl)
                    .addHeader("RANGE","bytes=" + downloadLength + "-").
            url(downloadUrl).build();

            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file,"rw");
                savedFile.seek(downloadLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len ;
                while ((len = is.read(b)) != -1){

                    if (isCanceled){
                        return TYPE_CANCELD;
                    } else if (isPaused){
                        return TYPE_PAUSED;
                    }else {
                        total +=len;
                        savedFile.write(b,0,len);
                        int progress = (int) ((total+ downloadLength) * 100 / contentlength);
                        publishProgress(progress);
                    }
                }

                response.body().close();
                return TYPE_SUCESS;

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if (is !=null) {
                    is.close();
                }

                if (savedFile == null) {
                    savedFile.close();
                }
                if (isCanceled && file== null) {
                    file.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
       int progress = values[0];
        if (progress > lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;

        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SUCESS:
                listener.onSucess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELD:
                listener.onCanceled();
                break;
            default:
                break;
        }

    }
    public void pauseDownload(){
        isPaused = true;
    }

    public void cancelDownload(){
        isCanceled = true;
    }
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response!= null && response.isSuccessful()) {
            long contenLength = response.body().contentLength();
            return contenLength;
        }
        return 0;
    }
}