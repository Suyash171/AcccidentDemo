package com.example.acccidentdemo.recoderdb;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Creted by Ankit on 20-May-17
 * USE TO RECORD THE AUDIO
 */

public class Recorder {
    private static Recorder recorder;
    private IAudioRecorderListener iAudioRecorderListener;
    private ICoundDownListener iCoundDownListener;
    private MediaRecorder mMediaRecorder;
    private String sFilePath;
    private boolean isRecodingInProgress;
    private Timer timer;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    private LevelProgressBar levelProgressBar;

    //Naresh
    private List<Double> listArrAmplitudeValues;

    public static Recorder getInstance() {
        recorder = recorder != null ? recorder : new Recorder();
        return recorder;
    }

    public void startRecording(final Context context, IAudioRecorderListener iAudioRecorderListener) {
        try {
            this.iAudioRecorderListener = iAudioRecorderListener;
            this.iCoundDownListener = (ICoundDownListener) iAudioRecorderListener;
            if (!isRecodingInProgress) {
                sFilePath = MediaUtility.FILE.createAudioRecordingFile();
                mMediaRecorder = new MediaRecorder();

                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecorder.setOutputFile(sFilePath);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                mMediaRecorder.prepare();
                mMediaRecorder.start();

                listArrAmplitudeValues = new ArrayList<>();

                timer = new Timer();
                timer.scheduleAtFixedRate(new RecorderTask(context, mMediaRecorder, iAudioRecorderListener), 0, 500);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            stopRecording();
                        }
                   }, 10000);

                 /*  new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
//                        mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        // Toast.makeText(context, "Recording.....", Toast.LENGTH_SHORT).show();
                        iCoundDownListener.onSetCoundDown(millisUntilFinished / 1000);
                    }
                    public void onFinish() {
                        iCoundDownListener.onSetCoundDown( 0);
                        stopRecording();
                    }
                }.start();*/

                isRecodingInProgress = true;
                //Naresh
            }
        } catch (IOException e) {
            String LOG_TAG = "MEDIA_RECORDER";
            Log.e(LOG_TAG, "prepare() failed");
        }

    }

    public void stopRecording() {
        timer.cancel();
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            new Handler(Looper.getMainLooper()).post(() -> iAudioRecorderListener.stopMeter());
        }

       // mMediaRecorder = null;
        isRecodingInProgress = false;

        //Naresh
       // listArrAmplitudeValues.remove(0);
        double minValue = DataValidation.getMinValue(listArrAmplitudeValues);
        double maxValue = DataValidation.getMaxValue(listArrAmplitudeValues);
        double avg = DataValidation.getAverage(listArrAmplitudeValues);
        System.out.print("min =" + minValue + ", max=" + maxValue + ", avg=" + avg);
        iAudioRecorderListener.setMinMaxAvg(minValue, maxValue, avg);
    }

    public IAttachment getAudioFileDetails() {
        IAttachment iAttachment = new Attachment();
        File file = new File(sFilePath);
        iAttachment.setFileName(file.getName());
        iAttachment.setUrl(sFilePath);
        iAttachment.setType(Attachment.TYPE.AUDIO);
        iAttachment.setName(file.getName());
        return iAttachment;
    }


    public boolean isRecordingInProgress() {
        return isRecodingInProgress;
    }


    private class RecorderTask extends TimerTask {

        private MediaRecorder recorder;
        private Context context;
        private IAudioRecorderListener iAudioRecorderListener;

        public RecorderTask(Context context, MediaRecorder recorder, IAudioRecorderListener iAudioRecorderListener) {
            this.recorder = recorder;
            this.context = context;
            this.iAudioRecorderListener = iAudioRecorderListener;
        }

        public void run() {
            ((Activity) context).runOnUiThread(() -> {
                if (recorder != null) {
                    int amplitude = recorder.getMaxAmplitude();
                    if (amplitude != 0) {
                        double amplitudeDb = 20 * Math.log10((double) Math.abs(amplitude));

                        iAudioRecorderListener.setAudioAmplitude(amplitudeDb);

                        iAudioRecorderListener.setLevelProgress((int) ((amplitudeDb * 100) / 120));
                        if (amplitudeDb > MediaUtility.WARNING.HARMFUL_AUDIO_LEVEL) {
                            iAudioRecorderListener.setWarningMessage(MediaUtility.WARNING.MESSAGE_AUDIO_HARMFUL);
                        } else {
                            iAudioRecorderListener.setWarningMessage(MediaUtility.WARNING.MESSAGE_AUDIO_SAFE);
                        }
                        //Naresh
                        if (amplitudeDb != Double.POSITIVE_INFINITY && amplitudeDb != Double.NEGATIVE_INFINITY) {
                            listArrAmplitudeValues.add(amplitudeDb);
                        }
                    }
                }
            });
        }
    }
}

