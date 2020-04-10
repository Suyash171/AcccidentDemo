package com.example.acccidentdemo;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AccidentActivity extends AppCompatActivity  {

    private Button mButtonPicture,mButtonSpeechToText,mSpeechToTextCustom,mButtonMap,mButtonRecord,mVideoPlayerButton,mFindSoundDecible,mCropper,customCropper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_accident);

        mButtonPicture = findViewById(R.id.btn_pictire);
        mSpeechToTextCustom = findViewById(R.id.btn_specch_custom);
        mButtonSpeechToText = findViewById(R.id.btn_speech_to_text);
        mButtonMap = findViewById(R.id.btn_map);
        mVideoPlayerButton = findViewById(R.id.btn_video_record);
        mButtonRecord = findViewById(R.id.btn_record);
        mFindSoundDecible = findViewById(R.id.btn_decible);
        mCropper = findViewById(R.id.btn_copper);
        customCropper = findViewById(R.id.btn_new_cropper);

        mButtonPicture.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        mButtonSpeechToText.setOnClickListener(view -> {
            Intent intent = new Intent(this, SpeechToTextActivity.class);
            startActivity(intent);
        });

        mSpeechToTextCustom.setOnClickListener(view -> {
            Intent intent = new Intent(this, SpeechToTextOwnActivity.class);
            startActivity(intent);
        });

        mButtonMap.setOnClickListener(view -> {
            Intent intent = new Intent(this, GoogleMapsActivity.class);
            startActivity(intent);
        });

        mButtonRecord.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        });

        mVideoPlayerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, VideoRecordingActivity.class);
            startActivity(intent);
        });

        mFindSoundDecible.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordWithDbActivity.class);
            startActivity(intent);
        });

        mCropper.setOnClickListener(view -> {
            Intent intent = new Intent(this, CopperActivity.class);
            startActivity(intent);
        });

        customCropper.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewCropperActivity.class);
            startActivity(intent);
        });
    }


}
