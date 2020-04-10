package com.example.acccidentdemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity {

    private ImageView mSpeechtoTextButton;
    private EditText mEditText;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_speechtotext);

        mSpeechtoTextButton = findViewById(R.id.ib_mic);
        mEditText = findViewById(R.id.tv_text);

        mSpeechtoTextButton.setOnClickListener(view -> {
            promptSpeechInput();
        });
    }

    /**
     * Showing google speech input dialog
     *
     * Need to pass local language to convert speech to text.
     * Not all devices support offline speech input.
     * You cannot pass an audio file to be recognized.
     * The intent returns an array of strings which match to out input. We can consider first one as the most accurate.
     * It only works with Android phones.
     * It is free.
     *
     * https://support.google.com/websearch/answer/6030020?co=GENIE.Platform%3DAndroid&hl=en
     *
     *
     * Based on android regulations you cannot hide system toast messages as you don't have the accesses to the system View,
     *
     * only in jailbrake android where you have access to the terminal you can try to do that.
     *
     * https://stackoverflow.com/questions/50692620/how-to-hide-toast-message-your-audio-will-be-sent-to-google-to-provide-speech-r
     *
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditText.setText(result.get(0));
                }
                break;
            }

        }
    }
}
