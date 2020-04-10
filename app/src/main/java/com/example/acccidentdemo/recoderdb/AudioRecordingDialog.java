package com.example.acccidentdemo.recoderdb;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.acccidentdemo.DialogUtility;
import com.example.acccidentdemo.R;

import java.io.File;


/**
 * Creted by User on 20-May-17
 */

public class AudioRecordingDialog implements IAudioRecorderListener, ICoundDownListener {
    private Dialog dialog;
    private Context context;
    private Recorder recorder;
    private TextView textViewAudioStatus;
    //    private ProgressBar progressBarAmplitude;
    private LevelProgressBar progressBarAmplitude;
    //    private IAttachmentListHandler iAttachmentListHandler;
    // private Button imageViewSave, imageViewDelete;
    private TextView textViewCountDown;
    private IAmplitudeListener iAmplitudeListener;

/*    public AudioRecordingDialog(Context context, IAttachmentListHandler iAttachmentListHandler) {
        this.context = context;
//        this.iAttachmentListHandler = iAttachmentListHandler;
        recorder = Recorder.getInstance();
    }*/

    public AudioRecordingDialog(Context context, IAmplitudeListener iAmplitudeListener) {
        this.context = context;
        this.iAmplitudeListener = iAmplitudeListener;
        recorder = Recorder.getInstance();
    }

    public void display() {
        init();
        setWarningMessage(MediaUtility.WARNING.MESSAGE_AUDIO_SAFE);

        if (!recorder.isRecordingInProgress()) {
            resetProgressBar();
        }
        // imageViewDelete.setVisibility(View.GONE);
//        imageViewSave.setVisibility(View.GONE);
        startRecording();
        //addListeners();
    }

    private void resetProgressBar() {
        progressBarAmplitude.setProgress(0);
        // progressBarAmplitude.setProgressDrawable(drawableNormalProgress);
    }

    private void hideFileHandlingButton() {
        // imageViewDelete.setVisibility(View.GONE);
    }

    private void displayFileHandling() {
        //imageViewDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLevelProgress(int progress) {
        Log.d("Log", "prg" + progress);
        progressBarAmplitude.setProgress(progress);


    }

    @Override
    public void setAudioAmplitude(double dAmplitude) {
        setProgressSeverityColor(dAmplitude);

        if (dAmplitude == Double.POSITIVE_INFINITY || dAmplitude == Double.NEGATIVE_INFINITY) {
            textViewAudioStatus.setText("0.0");
        } else {
            textViewAudioStatus.setText(String.format("%.2f dB", dAmplitude));
        }
    }

    /**
     *
     * @param dAmplitude
     */
    private void setProgressSeverityColor(double dAmplitude){
        switch ((int) dAmplitude){
            case MediaUtility.WARNING.NORMAL_AUDIO_LEVEL:
                progressBarAmplitude.setnFirstColor(Color.GREEN);
                progressBarAmplitude.setnSecondColor(Color.GREEN);
                break;
            case MediaUtility.WARNING.MID_AUDIO_LEVEL:
                progressBarAmplitude.setnFirstColor(Color.GREEN);
                progressBarAmplitude.setnSecondColor(Color.RED);
                break;
            case MediaUtility.WARNING.HARMFUL_AUDIO_LEVEL:
                progressBarAmplitude.setnFirstColor(Color.RED);
                progressBarAmplitude.setnSecondColor(Color.RED);
                break;
        }
    }
    @Override
    public void setWarningMessage(String sWarningMessage) {
        // textViewAudioStatus.setText(sWarningMessage);
    }

    @Override
    public void stopMeter() {
        //Naresh
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    //Naresh
    @Override
    public void setMinMaxAvg(final double min, final double max, final double avg) {

        ((Activity) context).runOnUiThread(() -> iAmplitudeListener.setLevelOfSound(min, max, avg));
    }


   /* private void addListeners() {
        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConformation();
            }
        });
        dialog.findViewById(R.id.button_handle_recording).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMediaPlayerBeforeDialogClose();
            }
        });

    }*/

    private void init() {
        if (dialog == null)
            dialog = DialogUtility.getDialog(context, R.layout.layout_audio_recoding_poupup);
        else {
            if (dialog.isShowing())
                dialog.dismiss();
            dialog.show();
        }

        dialog.setCancelable(false);
        progressBarAmplitude = dialog.findViewById(R.id.progressBar_audio_level);
        textViewAudioStatus = dialog.findViewById(R.id.textView_audio_level_warning);
        //imageViewSave = dialog.findViewById(R.id.button_save);
        //imageViewDelete = dialog.findViewById(R.id.button_delete);
        textViewCountDown = dialog.findViewById(R.id.textView_count_down);
        //imageViewSave.setText("Set");
    }

    private void checkMediaPlayerBeforeDialogClose() {
        if (!recorder.isRecordingInProgress()) {
            MediaUtility.FILE.deleteMediaFile(new File(recorder.getAudioFileDetails().getUrl()));
            if (dialog.isShowing())
                dialog.dismiss();
        } else {
            recorder.stopRecording();
            MediaUtility.FILE.deleteMediaFile(new File(recorder.getAudioFileDetails().getUrl()));
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private void stopRecording() {
        if (recorder.isRecordingInProgress()) {
            recorder.stopRecording();
            displayFileHandling();
        }
        deleteRecording();

        dialog.dismiss();
    }

    private void startRecording() {
        resetProgressBar();
        hideFileHandlingButton();
//            ((ImageView) view).setImageResource(R.drawable.ic_stop_recoding);
        setCountDownTicker();
        recorder.startRecording(context, AudioRecordingDialog.this);

    }

    /*
    private void deleteConformation() {
        new ConformationDialog(context).displayConfirmationPomp(context.getString(R.string.button_name_yes),
                context.getString(R.string.button_name_no),
                context.getString(R.string.message_audio_file_delete_conformation), new IConfirmationDialogListener() {
                    @Override
                    public void OnOkButtonClick() {
                        deleteRecording();
                    }

                    @Override
                    public void OnCancelClick() {
                    }
                });
    }*/

    private void deleteRecording() {
        if (!recorder.isRecordingInProgress()) {
            hideFileHandlingButton();
            MediaUtility.FILE.deleteMediaFile(new File(recorder.getAudioFileDetails().getUrl()));
            resetProgressBar();
//            Toast.makeText(context, context.getString(R.string.audio_file_deleted), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetCoundDown(long nRemainSecond) {
        textViewCountDown.setText(String.format("%d %s", nRemainSecond, " sec"));
    }

    /**
     *
     */
    private void setCountDownTicker() {
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                textViewCountDown.setText(String.format("%d %s", millisUntilFinished / 1000, " sec"));
            }
            public void onFinish() {

            }
        }.start();
    }
}
