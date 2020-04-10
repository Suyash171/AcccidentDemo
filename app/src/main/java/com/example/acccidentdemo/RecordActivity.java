package com.example.acccidentdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordActivity extends AppCompatActivity {

    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {RECORD_AUDIO};
    int count = 1;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);

        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);

        stop.setEnabled(false);
        play.setEnabled(false);

        random = new Random();

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recorder/");
        dir.mkdirs();
        if (dir.exists()){
            outputFile =  dir + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp";
        }

        record.setOnClickListener(v -> {

            if (checkPermission()){
                startRecording();
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }else {
                requestPermission();
            }

        });

        stop.setOnClickListener(v -> {
            myAudioRecorder.stop();
            record.setEnabled(true);
            stop.setEnabled(false);
            play.setEnabled(true);
            startRecording();
            Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
        });

        play.setOnClickListener(v -> {

            stop.setEnabled(false);
            record.setEnabled(false);
            play.setEnabled(true);

            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // make something
            }
        });
    }

    private void startRecording(){

        // Record to the external cache directory for visibility
        //outputFile = getExternalCacheDir().getAbsolutePath();
        //outputFile += "/audiorecordtest.3gp";

        // Get the directory for the app's private pictures directory.
        /*File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recorder");
        if (!file.mkdirs()) {
            Log.e(RecordActivity.class.getName(), "Directory not created");
        }*/

        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

       /* //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +"/.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);*/
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}
