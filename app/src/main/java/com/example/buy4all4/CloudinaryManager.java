package com.example.buy4all4;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.Map;

public class CloudinaryManager {

    private static final String CLOUD_NAME = "dya4h3ime";
    private static final String API_KEY = "249752166732665";
    private static final String API_SECRET = "-QtH6X4F7Fo8ABowSQN1NkUDNHs";

    private Cloudinary cloudinary;

    public CloudinaryManager() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onError(Exception e);
    }

    public void uploadImage(Context context, Uri imageUri, UploadCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private Exception exception;

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    File file = FileUtils.getFileFromUri(context, imageUri);
                    Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                    return uploadResult.get("secure_url").toString();
                } catch (Exception e) {
                    exception = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (imageUrl != null) {
                    callback.onSuccess(imageUrl);
                } else {
                    callback.onError(exception);
                }
            }
        }.execute();
    }
}
