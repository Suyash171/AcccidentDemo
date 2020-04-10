package com.example.acccidentdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.acccidentdemo.recoderdb.MediaUtility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class VideoRecordingActivity extends AppCompatActivity {

    private Button btnRecord,btnDelete;
    private static final int VIDEO_CAPTURE = 101;
    Random random ;
    private String outputFile;
    private VideoView videoView;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";


    private Uri fileUri;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static TextView output;
    private Uri videoUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_video_recorder);

        btnRecord = findViewById(R.id.btn_record);
        videoView = findViewById(R.id.videoView2);
        btnDelete = findViewById(R.id.button2);

        btnRecord.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(intent, VIDEO_CAPTURE);
            //captureVideo();
            // create new Intentwith with Standard Intent action that can be
            // sent to have the camera application capture an video and return it.


           /* Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            File file = getOutputMediaFile(MEDIA_TYPE_VIDEO);
            if (file != null) {
                outputFile = file.getAbsolutePath();
            }

            Uri fileUri = getOutputMediaFileUri(getApplicationContext(), file);
            // set video quality
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file

            // start the video capture Intent
            startActivityForResult(intent, VIDEO_CAPTURE);*/

        });

        btnDelete.setOnClickListener(view -> {
            if (!videoView.isPlaying()){
                if (videoUri != null){
                    getContentResolver().delete(videoUri,null,null);
                    //MediaUtility.FILE.deleteMediaFile(new File(videoUri.getPath()));
                }
            }
        });
    }


    public  File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "RTUChemVideos");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("RTUChemVideos", "Oops! Failed create "
                        + "RTUChemVideos" + " directory");
                return null;
            }
        }
        return mediaStorageDir;
    }


    public  Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() +
                ".provider", file);
    }


        public File getVideoFile() {
        //create a random name
        String randomName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File mainPath = new File(path, "/Videos");

        if (!mainPath.exists()) {
            mainPath.mkdirs();
        }

        File photoPath = new File(mainPath, randomName + ".mp4");
        return photoPath;
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));
            i++ ;
        }
        return stringBuilder.toString();
    }

    /** Create a File for saving an image or video */



  /*  public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }*/


    /** Create a file Uri for saving an image or video */
    private  Uri getOutputMediaFileUri(int type){

        return Uri.fromFile(getOutputMediaFile(type));
    }

    private void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File file = getOutputMediaFile(MEDIA_TYPE_VIDEO);
        if (file != null) {
            videoUri = Uri.parse(file.getAbsolutePath());
        }

        Uri fileUri = getOutputMediaFileUri(getApplicationContext(), file);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file

        // start the video capture Intent
        startActivityForResult(intent, VIDEO_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         videoUri = data.getData();
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                playbackRecordedVideo();
                Toast.makeText(this, "Video saved to:" + videoUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }


        // After camera screen this code will excuted
    }

    public void playbackRecordedVideo() {
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case VIDEO_CAPTURE:
                if (grantResults.length > 0) {
                    boolean videoPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (videoPermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }
}
