package com.gilazani.imagedownloadersdk;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends Thread {
    private int downloadId;
    private String url;
    private String destinationPath;
    private DownloadListener listener;
    private DownloadStatus status;

    public DownloadTask(int downloadId, String url, String destinationPath, DownloadListener listener) {
        this.downloadId = downloadId;
        this.url = url;
        this.destinationPath = destinationPath;
        this.listener = listener;
        this.status = DownloadStatus.PENDING;
    }

    @Override
    public void run() {
        status = DownloadStatus.DOWNLOADING;
        try {
            if (!isImageFile(url)) {
                status = DownloadStatus.FAILED;
                listener.onError(downloadId, DownloadError.INVALID_FILE_TYPE);
                return;
            }

            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(destinationPath);
            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalBytesRead = 0;
            int fileSize = connection.getContentLength();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                int progress = (int) ((totalBytesRead / (float) fileSize) * 100);
                listener.onProgress(downloadId, progress);
            }

            outputStream.close();
            inputStream.close();
            connection.disconnect();

            status = DownloadStatus.COMPLETED;
            listener.onSuccess(downloadId, destinationPath);
        } catch (Exception e) {
            status = DownloadStatus.FAILED;
            listener.onError(downloadId, DownloadError.NETWORK_ERROR);
        }
    }

    private boolean isImageFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            String contentType = connection.getContentType();
            connection.disconnect();
            return contentType.startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }

    public void stopDownload() {
        status = DownloadStatus.PAUSED;
    }

    public void resumeDownload() {
        status = DownloadStatus.DOWNLOADING;
        this.start();
    }

    public DownloadStatus getStatus() {
        return status;
    }
}