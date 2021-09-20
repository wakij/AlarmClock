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

import androidx.annotation.RequiresApi;

public class ring extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;

    int NEED_STEP = 1;

    boolean first = true;
    boolean up = false;
    float d0,d = 0f;
    int stepcount = 0;
    //    繝輔ぅ繝ｫ繧ｿ繝ｪ繝ｳ繧ｰ菫よ焚 0<a<1
    float a = 0.90f;

    // 繝｡繝ｳ繝舌ヵ繧｣繝ｼ繝ｫ繝峨・螳夂ｾｩ.
    Context mContext = null;	// mContext繧地ull縺ｧ蛻晄悄蛹・
    Uri uri;
    Ringtone ringtone;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // mContext縺ｮ蛻晄悄蛹・
        mContext = this;	// mContext縺ｫthis繧偵そ繝・ヨ.
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);	// 繝・ヵ繧ｩ繝ｫ繝医・uri繧貞叙蠕・
        ringtone = RingtoneManager.getRingtone(mContext, uri);	// ringtone繧貞叙蠕・
        ringtone.play();
        startSensor();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onSensorChanged(SensorEvent sensorEvent){
        float[] value = sensorEvent.values;
        float sum= (float)Math.sqrt(Math.pow(value[0],2) + Math.pow(value[1],2) + Math.pow(value[2],2));


        if (first){
            first = false;
            up = true;
            d0 = a * sum;
        }else{
            //繝ｭ繝ｼ繝代せ繝輔ぅ繝ｫ繧ｿ繝ｪ繝ｳ繧ｰ 譎らｳｻ蛻励・邏ｰ縺九＞繝・・繧ｿ繧貞ｹｳ貊大喧
            d =  a * sum + (1 - a) * d0;
            if (up && d < d0){
                up = false;
                stepcount++;
                if (stepcount > NEED_STEP){
                    ringtone.stop();
                    stopSelf();
                }
            }
            else if(!up&& d>d0){
                up = true;
                d0 = d;
            }
        }
    }
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }
    public void startSensor(){
        sensorManager.registerListener(this,sensor,sensorManager.SENSOR_DELAY_GAME);
    }
    public void restartSensor(){
        first = true;
        stepcount = 0;
    }




}

