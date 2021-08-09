package com.example.alarmclock;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class RingAlarm {
    Context mContext;
    Uri uri;
    Ringtone ringtone;

    public RingAlarm(Context context) {
        mContext = context;
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);	// デフォルトのuriを取得.
        ringtone = RingtoneManager.getRingtone(mContext, uri);	// ringtoneを取得.
    }



    public void Ring(){
        ringtone.play();
    }

    public void StopRing(){
        ringtone.stop();
    }
}
