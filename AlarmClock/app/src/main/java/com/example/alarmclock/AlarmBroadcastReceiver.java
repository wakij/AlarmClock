package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

//これはアラーム信号を受け取るためのスクリプトです
//これは確認用です。
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context,"Received", Toast.LENGTH_LONG).show();

        Intent startService = new Intent(context,ring.class);
        context.startService(startService);
        Log.i("test","serviceStart");



//        Intent startActivityIntent = new Intent(context, Wakeup.class);
//        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startActivityIntent);
    }
}

