package com.example.inna.sleepy2.AudioProcessServices;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.inna.sleepy2.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioProccessModule {
    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private byte[] audioData;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    private double[] absNormalizedSignal;
    private double[] finalSignal;
    private int mPeakPos;

    public AudioProccessModule() {

        bufferSize = AudioRecord.getMinBufferSize
                (RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING) * 3;

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);
    }


    public double[] calculateFFT(byte[] signal) {
        final int mNumberOfFFTPoints = 2048;
        double mMaxFFTSample;

        double temp;
        Complex[] y;
        Complex[] complexSignal = new Complex[mNumberOfFFTPoints];
        double[] absSignal = new double[mNumberOfFFTPoints / 2];

        for (int i = 0; i < mNumberOfFFTPoints; i++) {

            temp = (double) ((signal[2 * i] & 0xFF) | (signal[2 * i + 1] << 8)) / 32768.0F;

            complexSignal[i] = new Complex(temp, 0.0);
        }

        y = FFT.fft(complexSignal);

        mMaxFFTSample = 0.0;
        mPeakPos = 0;
        for (int i = 0; i < (mNumberOfFFTPoints / 2); i++) {
            absSignal[i] = y[i].abs() / mNumberOfFFTPoints / 2;//<--P2 = abs(Y/L); P1 = P2(1:L/2+1);

            if (absSignal[i] > mMaxFFTSample) {
                mMaxFFTSample = absSignal[i];
                mPeakPos = i;
            }
        }

        return absSignal;

    }

    public void startRecording() {

        recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            public void run() {
                analyzeAudioData();
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }

    private void analyzeAudioData() {
        audioData = new byte[bufferSize];

        while (isRecording) {

            recorder.read(audioData, 0, bufferSize);
            absNormalizedSignal = calculateFFT(audioData); // P1
            finalSignal = new double[absNormalizedSignal.length];
            for (int i = 1; i < absNormalizedSignal.length - 1; i++) {
                finalSignal[i] = absNormalizedSignal[i] * 2;
            }

            double f[] = new double[absNormalizedSignal.length / 2];
            for (int i = 0; i < (absNormalizedSignal.length / 2) - 1; i++) {
                f[i] = RECORDER_SAMPLERATE * i / absNormalizedSignal.length;//f = Fs*(0:(L/2))/L;
                Log.i("Plot","f:"+f[i]+", P2: "+finalSignal[i]);
            }

        }
    }

    public void stopRecording() {
        if (null != recorder) {
            isRecording = false;

            recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

    }

}