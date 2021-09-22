package com.example.alarmclock;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SoundService extends Service implements MediaPlayer.OnCompletionListener{

    MediaPlayer mediaPlayer;

    public SoundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 参考 https://smartomaizu.com/ringtones/sozai/775.html
        mediaPlayer = MediaPlayer.create(this, R.raw.app_src_main_res_raw_wakeup);
        mediaPlayer.setOnCompletionListener(this);
        play();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 再生
    private void play() {
        mediaPlayer.start();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 再生が終わる度に音量を上げてループ再生
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        play();
    }
}
