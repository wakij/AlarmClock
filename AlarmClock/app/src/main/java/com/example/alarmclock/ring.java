package com.example.alarmclock;


import com.example.alarmclock.footstepcount;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

public class ring extends MainActivity {

    // メンバフィールドの定義.
    Context mContext = null;	// mContextをnullで初期化.


     footstepcount foot=null;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring);






        // mContextの初期化.
        mContext = this;	// mContextにthisをセット.
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);	// デフォルトのuriを取得.
        Ringtone ringtone = RingtoneManager.getRingtone(mContext, uri);	// ringtoneを取得.


        // button1の初期化.
        Button button1 = (Button)findViewById(R.id.button1);	// button1を取得.
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 着信音再生.
                ringtone.play();	// ringtone.playで再生.
                foot = new footstepcount();
                foot.clickStartButton(v);
            }

        });

        Button button2 = (Button)findViewById(R.id.button2);	// button1を取得.
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 着信音再生.
                if(foot.stepcount<2) {
                    ringtone.stop();    // ringtone.playで再生.
                }
            }

        });





    }


}
