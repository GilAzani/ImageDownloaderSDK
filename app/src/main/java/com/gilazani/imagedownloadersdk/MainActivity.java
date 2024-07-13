package com.gilazani.imagedownloadersdk;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ImageView imageView;
    private ProgressBar progressBar;
    private ImageDownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        downloadManager = new ImageDownloadManager();

        checkAndRequestPermissionAndStartDownload();
    }

    private void checkAndRequestPermissionAndStartDownload() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            startImageDownload();
        }
    }

    private void startImageDownload() {
        // Get the internal storage directory
        File internalStorageDir = getFilesDir();
        File imageFile = new File(internalStorageDir, "image.jpg");
        int downloadId = downloadManager.startDownload(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5Wxa0b-px_BUYQTHP-TCnnKk37drpZGhHPA&s",
                imageFile.getAbsolutePath(),
                new DownloadListener() {
                    @Override
                    public void onProgress(int downloadId, int progress) {
                        runOnUiThread(() -> progressBar.setProgress(progress));
                    }

                    @Override
                    public void onSuccess(int downloadId, String filePath) {
                        runOnUiThread(() -> {
                            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
                            if (myBitmap != null) {
                                imageView.setImageBitmap(myBitmap);
                            }
                        });
                    }

                    @Override
                    public void onError(int downloadId, DownloadError error) {
                        runOnUiThread(() -> progressBar.setVisibility(ProgressBar.GONE));
                        // Handle errors
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageDownload();
            } else {
                // Permission denied
                Log.d("error", "permission denied");
            }
        }
    }
}