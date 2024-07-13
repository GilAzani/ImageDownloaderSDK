package com.gilazani.imagedownloadersdk;

public interface DownloadListener {
    void onProgress(int downloadId, int progress);

    void onSuccess(int downloadId, String filePath);

    void onError(int downloadId, DownloadError error);
}
