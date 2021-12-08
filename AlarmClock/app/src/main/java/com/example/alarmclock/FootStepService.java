package com.example.alarmclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

public class FootStepService extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;

    int NEED_STEP = 10;

    boolean first = true;
    boolean up = false;
    float d0, d = 0f;
    int stepcount = 0;
    float a = 0.90f;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private boolean isStop = false;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //intentからNEED_STEPを受け取る。
        NEED_STEP = intent.getIntExtra("needStep",0);
        NEED_STEP = 2;
        Log.e("needStep",String.valueOf(NEED_STEP));
        startSensor();
        Log.e("ProcessName",getApplication().getPackageName());
        mediaPlayer = SoundService.getMediaPlayer();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Destroy","FootStep Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] value = sensorEvent.values;
        float sum = (float) Math.sqrt(Math.pow(value[0], 2) + Math.pow(value[1], 2) + Math.pow(value[2], 2));

        if (first){
            first = false;
            up = true;
            d0 = a * sum;
            handler = new Handler(getMainLooper());
        }else{
            d =  a * sum + (1 - a) * d0;
            if (up && d < d0){
                up = false;
                stepcount++;
                if (!isStop)
                {
                    if (stepcount > NEED_STEP){
                        isStop = true;
                        Intent musicStop = new Intent(getApplication().getApplicationContext(),SoundService.class);
                        getApplication().stopService(musicStop);
                        stopSelf();
                    }else
                    {
                        pauseMusic();
                    }

                }

            }
            else if(!up&& d>d0){
                up = true;
                d0 = d;
            }
        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startSensor() {
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_GAME);
    }

    public void restartSensor() {
        first = true;
        stepcount = 0;
    }

    private void pauseMusic()
    {
        int step_former = stepcount;
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int step_latter = stepcount;
                if (step_former == step_latter)
                {
                    mediaPlayer.start();
                }
            }
        }, 5000);
    }
}