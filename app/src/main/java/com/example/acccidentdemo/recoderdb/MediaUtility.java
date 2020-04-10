package com.example.acccidentdemo.recoderdb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * USE TO HANDLE MEDIA SUCH AS AUDIO,VIDEO,IMAGE
 */

public class MediaUtility {
    public static final int REQUEST_VIDEO_CAPTURE = 345;
    public static final int CAMERA_REQUEST = 12;
    public static final int PROFILE_PHOTO = 122;
    public static final int STORAGE_PERMISSION_REQUEST = 124;

    public static void openAudioFile(Activity activity, String sFilePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(sFilePath), MediaType.VIDEO);
        activity.startActivityForResult(intent, 0);
    }

    public static void openVideoFile(Activity activity, String sFilePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(sFilePath), MediaType.VIDEO);
        activity.startActivityForResult(intent, 0);
    }

    public static void recordVideo(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        }
    }


    public static class FILE {
        @Nullable
        public static String createAudioRecordingFile() {
            try {
                // Create an image file name
                String sTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String sImageFileName = "AUDIO_" + sTimeStamp + "_";

                File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/RecorderDb/");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                return (File.createTempFile(
                        sImageFileName,  /* prefix */
                        EXTENSIONS.AUDIO,         /* suffix */
                        storageDir      /* directory */
                )).getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Nullable
        public static String createVideoRecordingFile() {
            try {
                // Create an image file name
                String sTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String sImageFileName = "VIDEO_" + sTimeStamp + "_";

                File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoRecording/");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                return (File.createTempFile(
                        sImageFileName,  /* prefix */
                        EXTENSIONS.VIDEO,         /* suffix */
                        storageDir      /* directory */
                )).getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static void deleteMediaFile(File mFile) {
            if (mFile.exists()) {
                mFile.delete();
            }
        }


        public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }

        public static boolean copyFileToAppDirectory(String sDestinationPath, String sourceFilePath) {
            try {
                InputStream inputStream;
                OutputStream outputStream;

                //            File destinationFileDir = context.getExternalFilesDir(AppConstant.SECURE_DIR + AppConstant.IMAGE_PATH);
                File destinationFileDir = new File(sDestinationPath);
                if (!destinationFileDir.exists()) {
                    destinationFileDir.mkdirs();
                }

                inputStream = new FileInputStream(sourceFilePath);
                outputStream = new FileOutputStream(destinationFileDir);

                byte[] buffer = new byte[1024];
                int nRead;
                while ((nRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, nRead);
                }
                inputStream.close();

                outputStream.flush();
                outputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }


        public static String getRealPathFromUri(Context context, Uri contentUri) {
            Cursor cursor = null;
            try {
                String[] imgData = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(contentUri, imgData, null, null, null);
                int nColumnIndex;
                if (cursor != null) {
                    nColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    return cursor.getString(nColumnIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return "";
        }
    }


    static class EXTENSIONS {
        static final String AUDIO = ".3gp";
        static final String IMAGE = ".jpg";
        static final String VIDEO = ".mp4";

    }

    public static class MediaType {
        public static final String IMAGE = "image/*";
        public static final String AUDIO = "audio/*";
        static final String VIDEO = "video/mp4";
    }

    public static class WARNING {
        public static final String MESSAGE_AUDIO_SAFE = "Don't worry,you are in a safe place";
        public static final String MESSAGE_AUDIO_HARMFUL = "Be Careful! Sound around you may be harmful to you";
        public static final int HARMFUL_AUDIO_LEVEL = 85;
        public static final int MID_AUDIO_LEVEL = 50;
        public static final int NORMAL_AUDIO_LEVEL = 0;
    }

}
