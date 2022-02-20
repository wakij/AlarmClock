package com.example.alarmclock;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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
    private int hour;
    private int minutes;


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
        NEED_STEP = intent.getIntExtra("needfootstep",0);
        NEED_STEP = 5; //テスト用
        hour = intent.getIntExtra("hour",0);
        minutes = intent.getIntExtra("minutes",0);
        startSensor();
        mediaPlayer = SoundService.getMediaPlayer();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSensor();
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
            edit_wakeup_Info(getApplicationContext(),hour,minutes);
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
//        センサーがデータを取得するたびに更新するようにしている(データのストックを行わない)
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_GAME);
    }
//    サービスが停止してもっセンサーは停止しない
    public void stopSensor()
    {
        sensorManager.unregisterListener(this);
    }


    private void pauseMusic()
    {
        int step_former = stepcount;
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        new Handler().postDelayed(new Runnable() {
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

//    DBに起床情報を保存する
    public void edit_wakeup_Info(Context context,int hour,int minute)
    {
        String estimated_time = hour + ":" + minute;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        // Calendarを使って現在の時間をミリ秒で取得
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.getTimeInMillis();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        // 現在時刻を取得
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());
        int hour_ = nowCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes_ = nowCalendar.get(Calendar.MINUTE);
        int month = nowCalendar.get(Calendar.MONTH);
        int day = nowCalendar.get(Calendar.DAY_OF_MONTH);
        String date = month + "/" + day;
        String real_time = hour_ +  ":" + minutes_;

//
////        起床予定時間と実際に起きた時間の差
//        int diff = calendar.compareTo(nowCalendar);

        DatabaseHelper helper = new DatabaseHelper(context);


        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()){
            ContentValues cv = new ContentValues();
            cv.put(DBDef.DBEntry.DATE,date);
            cv.put(DBDef.DBEntry.REAL_WAKE_UP_TIME,real_time);
            cv.put(DBDef.DBEntry.ESTIMATED_WAKE_UP_TIME,estimated_time);
            cv.put(DBDef.DBEntry.FOOTSTEP_COUNT,NEED_STEP);
            db.insert(DBDef.DBEntry.CHART_TABLE, null, cv);
        }
    }
}

