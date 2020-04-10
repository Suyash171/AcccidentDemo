package com.example.acccidentdemo.recoderdb;

/**
 * Creted by User on 20-May-17
 */

public interface IAudioRecorderListener {
     void setLevelProgress(int progress);

    void setAudioAmplitude(double dAmplitude);

    void setWarningMessage(String sWarningMessage);
    void stopMeter();

    //Naresh
    void setMinMaxAvg(double min, double max, double avg);
}
