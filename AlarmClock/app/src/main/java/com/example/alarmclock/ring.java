package com.example.alarmclock;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ring extends AppCompatActivity implements SensorEventListener{

    SensorManager sensorManager;
    Sensor sensor;
    TextView stepTextView;

    boolean first = true;
    boolean up = false;
    float d0,d = 0f;
    int stepcount = 0;
    //    フィルタリング係数 0<a<1
    float a = 0.90f;

    // メンバフィールドの定義.
    Context mContext = null;	// mContextをnullで初期化.
    Uri uri;
    Ringtone ringtone;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepTextView = (TextView) findViewById(R.id.counter);

        // mContextの初期化.
        mContext = this;	// mContextにthisをセット.
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);	// デフォルトのuriを取得.
        ringtone = RingtoneManager.getRingtone(mContext, uri);	// ringtoneを取得.


        // button1の初期化.
        Button button1 = (Button)findViewById(R.id.button1);	// button1を取得.
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startSensor(); //歩数計測を始める
                // 着信音再生.
                ringtone.play();	// ringtone.playで再生.
            }

        });

        Button button2 = (Button)findViewById(R.id.button2);	// button1を取得.
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 着信音再生.
                if(stepcount>1) {
                    ringtone.stop();    // ringtone.playで再生.
                    stopSensor(); //計測を終了する
                }
            }

        });

    }
    public void onSensorChanged(SensorEvent sensorEvent){
        float[] value = sensorEvent.values;
        float sum= (float)Math.sqrt(Math.pow(value[0],2) + Math.pow(value[1],2) + Math.pow(value[2],2));


        if (first){
            first = false;
            up = true;
            d0 = a * sum;
        }else{
            //ローパスフィルタリング 時系列の細かいデータを平滑化
            d =  a * sum + (1 - a) * d0;
            if (up && d < d0){
                up = false;
                stepcount++;
                if (stepcount > 1){
                    ringtone.stop();
                    stopSensor();
                }
            }
            else if(!up&& d>d0){
                up = true;
                d0 = d;
            }
        }
        stepTextView.setText(stepcount + "歩");
    }
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }
    protected void onResume(){
        super.onResume();
    }
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    public void startSensor(){
        sensorManager.registerListener(this,sensor,sensorManager.SENSOR_DELAY_GAME);
    }
    public void restartSensor(){
        first = true;
        stepcount = 0;
    }
    public void stopSensor(){
        this.onPause();
    }
}
