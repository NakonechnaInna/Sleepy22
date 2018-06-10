package com.example.inna.sleepy2;

import android.app.Application;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

public class NoiseDetector implements Runnable {
    Thread thread;
    Context mContext;
    public boolean IsNoiseDetectorEnabled;

    NoiseDetector(Context context) {
        mContext = context;
        // Создаём новый второй поток
        thread = new Thread(this, "Поток для примера");
        Log.i("NoiseDetector", " Runnable for noise detection");
        thread.start();
    }

    @Override
    public void run() {
        IsNoiseDetectorEnabled =true;
        int minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);


        short[] buffer = new short[minSize];

        ar.startRecording();
        while (IsNoiseDetectorEnabled) {

            ar.read(buffer, 0, minSize);
            for (short s : buffer) {
                Log.i("NoiseDetector", " noise detection"+s);
                if (Math.abs(s) > 27000)   //DETECT VOLUME (IF I BLOW IN THE MIC)
                {
                    float blow_value = Math.abs(s);
                    System.out.println("Blow Value=" + blow_value);
                    ((MainActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(mContext, "ШУМНО", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                }

            }
        }
        return;

    }
}
