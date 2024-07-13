# ImageDownloaderSDK


## Permissions Required
To use the ImageDownloaderSDK, ensure the following permissions are declared in your AndroidManifest.xml:

```xml
    <uses-permission android:name="android.permission.ACCESS_MEDIA_LOCATION"/>
    <uses-permission android:name="android.permission.INTERNET"/>
```

## Permissions
Before downloading images, ensure the necessary permissions are requested from the user. Handle permissions in your activity or fragment.

## Downloading Images
Use the ImageDownloadManager to download images:

```java
ImageDownloadManager downloadManager = new ImageDownloadManager();

int downloadId = downloadManager.startDownload(imageUrl, localFilePath, new DownloadListener() {
    @Override
    public void onProgress(int downloadId, int progress) {
        // Update progress bar or UI
    }

    @Override
    public void onSuccess(int downloadId, String filePath) {
        // Handle successful download
    }

    @Override
    public void onError(int downloadId, DownloadError error) {
        // Handle download error
    }
});
```

## Example Usage

There is an example usage of the ImageDownloaderSDK in the MainActivity class. For details, please refer to the MainActivity file in the source code.
  
