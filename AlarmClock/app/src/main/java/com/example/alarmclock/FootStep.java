package com.example.alarmclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.RequiresApi;

public class FootStep extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;

    int NEED_STEP = 1;

    boolean first = true;
    boolean up = false;
    float d0, d = 0f;
    int stepcount = 0;
    float a = 0.90f;






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
        Log.e("needStep",String.valueOf(NEED_STEP));
        startSensor();
        Log.e("ProcessName",getApplication().getPackageName());

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        }else{
            d =  a * sum + (1 - a) * d0;
            if (up && d < d0){
                up = false;
                stepcount++;
                if (stepcount > NEED_STEP){
                    Intent intent = new Intent(getApplication().getApplicationContext(),SoundService.class);
                    stopService(intent);
                    stopSelf();
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


}
