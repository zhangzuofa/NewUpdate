package com.example.administrator.newupdate;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public interface DownloadListener {
    void onSucess();
    void onFailed();
    void onProgress(int progress);
    void onPaused();
    void onCanceled();
}