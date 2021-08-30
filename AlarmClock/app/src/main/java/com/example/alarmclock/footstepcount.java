package com.example.alarmclock;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class footstepcount extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensor;
    TextView stepTextView;

    boolean first = true;
    boolean up = false;
    float d0,d = 0f;
    int stepcount = 0;
//    フィルタリング係数 0<a<1
    float a = 0.90f;

   public footstepcount(){
       sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
       sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

   }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footstepcount);


        stepTextView = (TextView) findViewById(R.id.counter);
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
    public void clickStartButton(View view){
        sensorManager.registerListener(this,sensor,sensorManager.SENSOR_DELAY_GAME);
    }
    public void clickRestartButton(View view){
        stepTextView.setText("0歩");
        first = true;
        stepcount = 0;
    }
    public void clickStopButton(View view){
        this.onPause();
    }
}
