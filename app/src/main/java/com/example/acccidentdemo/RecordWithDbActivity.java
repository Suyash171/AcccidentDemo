package com.example.acccidentdemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.acccidentdemo.recoderdb.AudioRecordingDialog;
import com.example.acccidentdemo.recoderdb.IAmplitudeListener;
import com.example.acccidentdemo.recoderdb.IAudioRecorderListener;
import com.example.acccidentdemo.recoderdb.LevelProgressBar;
import com.example.acccidentdemo.recoderdb.Recorder;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class RecordWithDbActivity extends AppCompatActivity implements IAudioRecorderListener, IAmplitudeListener {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private Button btnplay,btnPause,btnStop;
    private TextView tvTicker,tvMesg,tvMinMaxAvg;
    private LevelProgressBar progressBarAmplitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_with_db);

        btnplay = findViewById(R.id.play);
        btnPause = findViewById(R.id.record);
        btnStop = findViewById(R.id.stop);
        tvTicker = findViewById(R.id.tv_ticker);
        tvMesg = findViewById(R.id.tv_mesg);
        tvMinMaxAvg = findViewById(R.id.tv_min_max_avg);
        progressBarAmplitude = findViewById(R.id.progressBar_audio_level);

        btnplay.setOnClickListener(view -> {

        });

        btnStop.setOnClickListener(view -> {
            Recorder.getInstance().stopRecording();
        });

        btnPause.setOnClickListener(view -> {
            if (checkPermission()){
                AudioRecordingDialog audioRecordingDialog =  new AudioRecordingDialog(this,this);
                audioRecordingDialog.display();;
                //Recorder.getInstance().startRecording(this,this);
            }else {
                requestPermission();
            }
        });
    }

    @Override
    public void setAudioAmplitude(double dAmplitude) {
        Log.d(RecordWithDbActivity.class.getName(),"Db" + dAmplitude);
        //tvTicker.setText("dB " + (int) dAmplitude);
        if (dAmplitude == Double.POSITIVE_INFINITY || dAmplitude == Double.NEGATIVE_INFINITY) {
            tvTicker.setText("0.0");
        } else {
            tvTicker.setText(String.format("%.2f dB", dAmplitude));
        }
    }

    @Override
    public void setWarningMessage(String sWarningMessage) {
        Log.d(RecordWithDbActivity.class.getName(),"Warning Message" + sWarningMessage);
        tvMesg.setText("" + sWarningMessage);
    }

    @Override
    public void stopMeter() {
        Log.d(RecordWithDbActivity.class.getName(),"Stop Meter");
    }

    @Override
    public void setMinMaxAvg(double min, double max, double avg) {
        Log.d(RecordWithDbActivity.class.getName(),"Min Sound" + min + " Maximum " + max + " Average" + avg);
        tvMinMaxAvg.setText("Minimum dB " + (int) min + " \n Maxmimum dB " + (int) max + " \n Average dB " + (int) avg);
    }

    @Override
    public void setLevelProgress(int progress) {
        Log.d("Log", "prg" + progress);
        progressBarAmplitude.setProgress(progress);
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
        ActivityCompat.requestPermissions(RecordWithDbActivity.this, new
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

    @Override
    public void setLevelOfSound(double min, double max, double avg) {
        tvMinMaxAvg.setText("Minimum dB " + (int) min + " \n Maxmimum dB " + (int) max + " \n Average dB " + (int) avg);
    }
}
