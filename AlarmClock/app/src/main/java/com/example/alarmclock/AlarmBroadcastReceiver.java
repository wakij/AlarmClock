package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context,"Received", Toast.LENGTH_LONG).show();

        //       Intent serviveIntent = new Intent(context, ring.class);
        //     context.startService(serviveIntent);

        Intent startActivityIntent = new Intent(context, AlarmStop.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }
}

