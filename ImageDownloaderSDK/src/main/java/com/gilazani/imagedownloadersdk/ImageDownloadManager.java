package com.gilazani.imagedownloadersdk;

import java.util.HashMap;
import java.util.Map;

public class ImageDownloadManager {

    private int downloadCounter = 0;
    private Map<Integer, DownloadTask> downloadTasks = new HashMap<>();

    public int startDownload(String url, String destinationPath, DownloadListener listener) {
        int downloadId = ++downloadCounter;
        DownloadTask downloadTask = new DownloadTask(downloadId, url, destinationPath, listener);
        downloadTasks.put(downloadId, downloadTask);
        downloadTask.start();
        return downloadId;
    }

    public void stopDownload(int downloadId) {
        DownloadTask downloadTask = downloadTasks.get(downloadId);
        if (downloadTask != null) {
            downloadTask.stopDownload();
        }
    }

    public void resumeDownload(int downloadId) {
        DownloadTask downloadTask = downloadTasks.get(downloadId);
        if (downloadTask != null) {
            downloadTask.resumeDownload();
        }
    }

    public DownloadStatus getDownloadStatus(int downloadId) {
        DownloadTask downloadTask = downloadTasks.get(downloadId);
        return downloadTask != null ? downloadTask.getStatus() : DownloadStatus.UNKNOWN_ERROR;
    }
}




